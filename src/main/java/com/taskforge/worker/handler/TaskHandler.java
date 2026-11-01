package com.taskforge.worker.handler;

import com.taskforge.common.model.TaskType;

import java.util.Map;

public interface TaskHandler {
    TaskType getSupportedType();
    Map<String, Object> execute(Map<String, Object> input) throws Exception;
}
