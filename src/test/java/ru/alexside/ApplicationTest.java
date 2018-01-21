package ru.alexside;

/**
 * Created by Alex on 19.01.2018.
 */
public class ApplicationTest {
    public static void main(String[] args) throws Exception {
        TaskManager taskManager = new TaskManager();
        taskManager.start();
        new TaskProducer(taskManager, 100, 1000, 1515628800000L, 1516665600000L);
        new TaskConsumer(taskManager);
    }
}
