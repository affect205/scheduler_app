package ru.alexside;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Created by Alex on 19.01.2018.
 */
public class TaskRequest implements Comparable<TaskRequest> {
    private LocalDateTime dateTime;
    private Callable<TaskResult> task;
    public TaskRequest(LocalDateTime dateTime, Callable<TaskResult> task) {
        this.dateTime = dateTime;
        this.task = task;
    }
    public LocalDateTime getDateTime() { return dateTime; }
    public Callable<TaskResult> getTask() { return task; }
    @Override
    public int compareTo(TaskRequest o) {
        return dateTime.compareTo(o.getDateTime());
    }
    @Override
    public String toString() {
        return "TaskRequest{" +
                "dateTime=" + Objects.toString(dateTime, "null") +
                '}';
    }
}
