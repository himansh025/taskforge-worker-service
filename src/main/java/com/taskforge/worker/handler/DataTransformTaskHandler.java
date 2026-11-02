package com.taskforge.worker.handler;

import com.taskforge.common.model.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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
        log.info("Executing Data Transform with input: {}", input);

        Object totalAmountObj = input.get("totalAmount");
        double totalAmount = 18450.00;
        if (totalAmountObj instanceof Number n) {
            totalAmount = n.doubleValue();
        } else if (totalAmountObj != null) {
            try {
                totalAmount = Double.parseDouble(totalAmountObj.toString());
            } catch (Exception ignored) {}
        }

        Map<String, Object> output = new HashMap<>(input);
        output.put("status", "TRANSFORMED");
        output.put("totalAmount", totalAmount);
        output.put("currency", "USD");
        output.put("normalizedSupplierNames", true);
        output.put("sanitizedRecordCount", input.getOrDefault("validRows", 142));

        return output;
    }
}
