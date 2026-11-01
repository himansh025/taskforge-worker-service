package com.taskforge.worker.handler;

import com.taskforge.common.model.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class GenerateReportTaskHandler implements TaskHandler {

    @Override
    public TaskType getSupportedType() {
        return TaskType.GENERATE_REPORT;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> input) {
        log.info("Generating processing report with input: {}", input);
        return Map.of(
                "reportUrl", "s3://taskforge-artifacts/reports/execution-summary.pdf",
                "generatedAt", java.time.Instant.now().toString(),
                "fileSizeKb", 42
        );
    }
}
