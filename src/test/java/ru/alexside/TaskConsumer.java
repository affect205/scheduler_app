package ru.alexside;

/**
 * Created by Alex on 21.01.2018.
 */
public class TaskConsumer {
    private volatile boolean stop;

    public TaskConsumer(final TaskManager taskManager) {
        new Thread(() -> {
            while (!stop) {
                taskManager.onComplete(result -> System.out.println("COMPLETE " + result.toString()));
            }
        }).start();
    }
}
