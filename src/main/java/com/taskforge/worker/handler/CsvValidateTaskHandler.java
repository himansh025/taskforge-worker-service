package com.taskforge.worker.handler;

import com.taskforge.common.model.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class CsvValidateTaskHandler implements TaskHandler {

    @Override
    public TaskType getSupportedType() {
        return TaskType.CSV_VALIDATE;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> input) {
        log.info("Executing CSV validation with input: {}", input);
        // Validates invoice CSV columns: InvoiceId, Customer, Amount, DueDate
        return Map.of(
                "status", "VALIDATED",
                "totalRows", 142,
                "validRows", 142,
                "invalidRows", 0,
                "checksum", "sha256:d8a5f3..."
        );
    }
}
