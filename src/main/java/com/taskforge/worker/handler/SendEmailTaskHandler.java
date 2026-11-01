package com.taskforge.worker.handler;

import com.taskforge.common.model.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class SendEmailTaskHandler implements TaskHandler {

    @Override
    public TaskType getSupportedType() {
        return TaskType.SEND_EMAIL;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> input) {
        log.info("Sending notification email (local Mailpit sink) with input: {}", input);
        return Map.of(
                "delivered", true,
                "recipient", "finance-team@taskforge.local",
                "subject", "TaskForge Workflow Completed Successfully"
        );
    }
}
