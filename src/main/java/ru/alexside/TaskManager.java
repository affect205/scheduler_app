package ru.alexside;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by Alex on 19.01.2018.
 */
public class TaskManager {
    public static final long DELAY = 500L;
    public static final long PERIOD = 3000L;
    public static final int QUEUE_CAPACITY = 1000;
    public static final int POOL_CAPACITY = 10;

    private BlockingQueue<TaskRequest> inputQueue;
    private BlockingQueue<TaskResult> outputQueue;
    private ExecutorService fixedPool;
    private ExecutorService execPool;
    private ScheduledExecutorService scheduledPool;

    private volatile boolean started;

    public TaskManager() {
        inputQueue = new PriorityBlockingQueue<>(QUEUE_CAPACITY);
        outputQueue = new PriorityBlockingQueue<>(QUEUE_CAPACITY);
        fixedPool = newFixedThreadPool(POOL_CAPACITY);
        execPool = newFixedThreadPool(POOL_CAPACITY);
        scheduledPool = newScheduledThreadPool(1);
    }

    public synchronized void start() {
        if (!started) {
            scheduledPool.scheduleAtFixedRate(() -> {
                System.out.printf("Scheduler is running... in(%s), out(%s)\n",
                        inputQueue.size(), outputQueue.size());
                //inputQueue.forEach(System.out::println);
                System.out.println("----------------------------------------------------");

                LocalDateTime now = LocalDateTime.now();
                TaskRequest request;
                while ((request = inputQueue.poll()) != null && now.compareTo(request.getDateTime()) >= 0) {// обрабатываем все задачи с подходящим временем
                    final TaskRequest supplyRequest = request;
                    supplyAsync(() -> {
                        try {
                            Collection<Future<TaskResult>> result = execPool.invokeAll(singletonList(supplyRequest.getTask()));
                            return result.iterator().next().get();
                        } catch (Throwable ex) {// в случае ошибки также вернем результат
                            return new TaskResult<>(supplyRequest.getDateTime(), null, ex);
                        }
                    }, fixedPool).thenAccept(outputQueue::add); // добавляем результат в очередь
                }

            }, DELAY, PERIOD, MILLISECONDS);
        }
        started = true;
    }

    public boolean isStarted() {
        return started;
    }

    public void addTask(LocalDateTime dateTime, Callable<TaskResult> callable) {
        runAsync(() -> {
            TaskRequest request = new TaskRequest(dateTime, callable);
            System.out.println("CREATED " + request);
            inputQueue.add(request);
        });
    }

    public void onComplete(Consumer<TaskResult> onComplete) {
        try {
            onComplete.accept(outputQueue.take());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
