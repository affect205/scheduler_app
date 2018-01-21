package ru.alexside;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by Alex on 19.01.2018.
 */
public class TaskResult<RESULT> implements Comparable<TaskRequest> {
    private LocalDateTime dateTime;
    private RESULT result;
    private Throwable error;
    public TaskResult(LocalDateTime dateTime, RESULT result) {
        this.dateTime = dateTime;
        this.result = result;
    }
    public TaskResult(LocalDateTime dateTime, RESULT result, Throwable error) {
        this(dateTime, result);
        this.error = error;
    }
    public LocalDateTime getDateTime() { return dateTime; }
    public RESULT getResult() { return result; }
    public Throwable getError() { return error; }
    @Override
    public int compareTo(TaskRequest o) {
        return dateTime.compareTo(o.getDateTime());
    }
    @Override
    public String toString() {
        return "TaskResult{" +
                "dateTime=" + Objects.toString(dateTime, "null") +
                ", result=" + Objects.toString(result, "null") +
                ", error=" + Objects.toString(error == null ? null : error.getMessage(), "null") +
                '}';
    }
}
