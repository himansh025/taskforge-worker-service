package com.taskforge.worker.handler;

import com.taskforge.common.model.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class DataTransformTaskHandler implements TaskHandler {

    @Override
    public TaskType getSupportedType() {
        return TaskType.DATA_TRANSFORM;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> input) {
        log.info("Executing Data Transformation with input: {}", input);
        return Map.of(
                "status", "TRANSFORMED",
                "processedRecords", 142,
                "normalizedCurrency", "USD",
                "totalAmount", 84520.50
        );
    }
}
