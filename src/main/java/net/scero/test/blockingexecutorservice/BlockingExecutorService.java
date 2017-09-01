package net.scero.test.blockingexecutorservice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Contola un ExecutorService implemetando una LinkedBlockingQueue como cola de peticiones entrantes
 * 
 * @author jnogueira
 * @data 2016-11-09
 *
 * @param <T>
 */
@Slf4j
public class BlockingExecutorService<T> {
    //---- Variables ----//
    private final ThreadPoolExecutor executor;
    private final int poolLimit;
    private final CompletionService<T> completionService;
    @Setter
    private IExecutorServiceCallback<T> callback;

    //---- Constructors ----//
    /**
     * Constructor
     * 
     * @param nThreads Número de hilos de pool del ExecutorService
     * @param poolLimit Número máximo de tareas que se pueden encolar
     */
    public BlockingExecutorService(int nThreads, int poolLimit) {
        this(nThreads, poolLimit, null);
    }

    /**
     * Constructor
     * 
     * @param nThreads Número de hilos de pool del ExecutorService
     * @param poolLimit Número máximo de tareas que se pueden encolar
     * @param callback Callback a ejecutar cada vez que se recoge un resultado
     */
    public BlockingExecutorService(int nThreads, int poolLimit, IExecutorServiceCallback<T> callback) {
        this.poolLimit = poolLimit;
        executor = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(poolLimit));
        executor.setRejectedExecutionHandler(new RejectedPolicy());
        completionService = new ExecutorCompletionService<>(executor);
        this.callback = callback;
    }

    //---- Public Methods----//
    /**
     * Envía una tarea al ExecutorService
     * 
     * @param task
     */
    public void submit(Callable<T> task) {
        completionService.submit(task);
    }

    /**
     * Espera que únicamente haya un "number" tareas en proceso y en la cola
     * 
     * @param number
     * @return
     */
    public List<T> waitTo(int number) {
        List<T> results = new ArrayList<>();
        while (getTasksInProcess() > number) {
            results.add(waitOneTask());
        }
        return results;
    }

    /**
     * Espera que terminen todas las tareas
     * 
     * @return
     */
    public List<T> waitTerminateAll() {
        return waitTo(0);
    }

    /**
     * Espera que finalize una tarea, operación bloqueante
     * 
     * @return
     */
    protected T waitOneTask() {
        T result;
        try {
            Future<T> future = completionService.take();
            result = future.get();
        } catch (ExecutionException e) {
            log.error("Error in Execution of task", e);
            result = null;
        } catch (InterruptedException e) {
            log.info("Interrupt task");
            result = null;
        }
        if (result != null && callback != null) {
            callback.run(result);
        }
        return result;
    }

    /**
     * Procesa el resultado de todas las tareas que hayan terminado, operación no bloqueante
     * 
     * @return
     */
    public List<T> getTerminateAll() {
        List<T> results = new ArrayList<>();
        T result;
        while ((result = getOneTask()) != null) {
            results.add(result);
        }
        return results;
    }

    /**
     * Recoge el resultado de una tarea, operación no bloqueante.
     * Devuelve null si no hay ninguna tarea terminada
     */
    protected T getOneTask() {
        Future<T> future = completionService.poll();
        T result;
        if (future != null) {
            try {
                result = future.get();
            } catch (InterruptedException e) {
                log.error("Error in Execution of task", e);
                result = null;
            } catch (ExecutionException e) {
                log.info("Interrupt task", e);
                result = null;
            }
            if (result != null && callback != null) {
                callback.run(result);
            }
        } else {
            result = null;
        }
        return result;
    }

    public boolean allowTasks() {
        return executor.getQueue().size() < poolLimit;
    }

    //---- Getters ----//
    /**
     * Indica el número de tareas en proceso
     * 
     * @return
     */
    public int getTasksInProcess() {
        return executor.getActiveCount() + executor.getQueue().size();
    }

    /**
     * Indica la cantidad de tareas en ejecución
     * 
     * @return
     */
    public int getTasksActive() {
        return executor.getActiveCount();
    }

    /**
     * Indica la cantidad de tareas en cola
     * 
     * @return
     */
    public int getTasksQueue() {
        return executor.getQueue().size();
    }

    //---- Auxiliar Classes ----//
    /**
     * Política a ejecutar cuando se desborde la cola de tareas. Se esperará que termine alguna y se ejecutará el callback o se encolará su resultado
     * 
     * @author jnogueira
     * @data 2016-11-09
     *
     */
    class RejectedPolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            do {
                waitOneTask();
            } while (executor.getQueue().size() >= poolLimit);
            executor.getQueue().add(r);
        }
    }
}
