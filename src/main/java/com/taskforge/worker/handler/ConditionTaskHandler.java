package com.taskforge.worker.handler;

import com.taskforge.common.model.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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

        double totalAmount = 18450.0;
        Object amountObj = input.get("totalAmount");
        if (amountObj instanceof Number n) {
            totalAmount = n.doubleValue();
        } else if (amountObj != null) {
            try {
                totalAmount = Double.parseDouble(amountObj.toString());
            } catch (Exception ignored) {}
        }

        double threshold = 10000.0;
        Object thresholdObj = input.get("threshold");
        if (thresholdObj instanceof Number n) {
            threshold = n.doubleValue();
        } else if (thresholdObj != null) {
            try {
                threshold = Double.parseDouble(thresholdObj.toString());
            } catch (Exception ignored) {}
        }

        boolean passed = totalAmount >= threshold;
        String branch = passed ? "Threshold Passed" : "Flag for Review";

        log.info("Condition evaluated: totalAmount=${} >= threshold=${} => branch={}", totalAmount, threshold, branch);

        Map<String, Object> output = new HashMap<>(input);
        output.put("conditionResult", passed);
        output.put("branch", branch);
        output.put("selectedBranch", branch);
        output.put("threshold", threshold);
        output.put("totalAmount", totalAmount);

        return output;
    }
}
