package com.taskforge.worker.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskforge.common.event.EventEnvelope;
import com.taskforge.common.event.TaskCompletedData;
import com.taskforge.common.event.TaskFailedData;
import com.taskforge.common.event.TaskRequestedData;
import com.taskforge.common.model.TaskType;
import com.taskforge.worker.handler.TaskHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TaskConsumer {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Map<TaskType, TaskHandler> handlerMap = new HashMap<>();

    public TaskConsumer(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate, List<TaskHandler> handlers) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
        for (TaskHandler handler : handlers) {
            handlerMap.put(handler.getSupportedType(), handler);
        }
    }

    @KafkaListener(topics = "${taskforge.kafka.topics.task-requested:task.requested.v1}", groupId = "worker-service-group")
    @SneakyThrows
    public void onTaskRequested(String message) {
        EventEnvelope<TaskRequestedData> envelope = objectMapper.readValue(message, new TypeReference<>() {});
        TaskRequestedData data = envelope.getData();
        log.info("Worker received task requested: node={}, type={}", data.getNodeId(), data.getTaskType());

        TaskHandler handler = handlerMap.get(data.getTaskType());
        if (handler == null) {
            sendFailure(envelope, data, "UNSUPPORTED_TASK_TYPE", "No handler registered for " + data.getTaskType(), false);
            return;
        }

        try {
            Map<String, Object> output = handler.execute(data.getInput());
            sendCompletion(envelope, data, output);
        } catch (Exception e) {
            log.error("Task execution failed for node {}: {}", data.getNodeId(), e.getMessage());
            sendFailure(envelope, data, "EXECUTION_ERROR", e.getMessage(), true);
        }
    }

    @SneakyThrows
    private void sendCompletion(EventEnvelope<TaskRequestedData> requestEnvelope, TaskRequestedData data, Map<String, Object> output) {
        TaskCompletedData completedData = TaskCompletedData.builder()
                .executionId(data.getExecutionId())
                .taskExecutionId(data.getTaskExecutionId())
                .nodeId(data.getNodeId())
                .attempt(data.getAttempt())
                .output(output)
                .build();

        EventEnvelope<TaskCompletedData> envelope = EventEnvelope.<TaskCompletedData>builder()
                .eventType("task.completed")
                .workspaceId(requestEnvelope.getWorkspaceId())
                .correlationId(data.getExecutionId())
                .causationId(requestEnvelope.getEventId())
                .producer("worker-service")
                .data(completedData)
                .build();

        kafkaTemplate.send("task.completed.v1", data.getTaskExecutionId(), objectMapper.writeValueAsString(envelope));
        log.info("Sent task.completed event for taskExecution {}", data.getTaskExecutionId());
    }

    @SneakyThrows
    private void sendFailure(EventEnvelope<TaskRequestedData> requestEnvelope, TaskRequestedData data, String code, String message, boolean retryable) {
        TaskFailedData failedData = TaskFailedData.builder()
                .executionId(data.getExecutionId())
                .taskExecutionId(data.getTaskExecutionId())
                .nodeId(data.getNodeId())
                .attempt(data.getAttempt())
                .errorCode(code)
                .errorMessage(message)
                .retryable(retryable)
                .build();

        EventEnvelope<TaskFailedData> envelope = EventEnvelope.<TaskFailedData>builder()
                .eventType("task.failed")
                .workspaceId(requestEnvelope.getWorkspaceId())
                .correlationId(data.getExecutionId())
                .causationId(requestEnvelope.getEventId())
                .producer("worker-service")
                .data(failedData)
                .build();

        kafkaTemplate.send("task.failed.v1", data.getTaskExecutionId(), objectMapper.writeValueAsString(envelope));
        log.info("Sent task.failed event for taskExecution {}", data.getTaskExecutionId());
    }
}
