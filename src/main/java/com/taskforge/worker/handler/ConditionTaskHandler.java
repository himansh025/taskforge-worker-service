package com.taskforge.worker.handler;

import com.taskforge.common.model.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class ConditionTaskHandler implements TaskHandler {

    @Override
    public TaskType getSupportedType() {
        return TaskType.CONDITION;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> input) {
        log.info("Evaluating Condition node with input: {}", input);
        return Map.of(
                "conditionResult", true,
                "selectedBranch", "approval_not_required"
        );
    }
}
