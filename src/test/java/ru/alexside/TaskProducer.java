package ru.alexside;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * Created by Alex on 21.01.2018.
 */
public class TaskProducer {
    private volatile boolean stop;

    public TaskProducer(final TaskManager taskManager, final long delayMin, final long delayMax, final long runMin, final long runMax) {
        new Thread(() -> {
            while (!stop) {
                try {
                    long delay = current().nextLong(delayMin, delayMax);// вычисляем время задержки
                    Thread.sleep(delay);
                    long timestamp = current().nextLong(runMin, runMax);// вычисляем время запуска задачи
                    LocalDateTime dateTime = ofInstant(ofEpochMilli(timestamp), ZoneId.systemDefault());
                    taskManager.addTask(dateTime, () -> {
                        try {
                            Thread.sleep(current().nextLong(200L, 3000L));
                        } catch (Exception ex) {}
                        return new TaskResult<>(dateTime, current().nextInt(1, 100));
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
}
