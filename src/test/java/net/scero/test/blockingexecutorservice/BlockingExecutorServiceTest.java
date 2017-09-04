package net.scero.test.blockingexecutorservice;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class BlockingExecutorServiceTest {
    private int results;

    /**
     * Prueba las funcionalidades de la clase con callback
     * 
     * @throws InterruptedException
     */
    @Ignore
    @Test
    public void test() throws InterruptedException {
        results = 0;
        // Define 3 hilos de ejcución y una cola con un máximo de 3 tareas más
        BlockingExecutorService<Integer> executor = new BlockingExecutorService<>(3, 4, new IExecutorServiceCallback<Integer>() {
            @Override
            public void run(Integer result) {
                results++;
            }
        });
        List<TestTask> testTasks = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            TestTask testTask = new TestTask(i);
            testTasks.add(testTask);
            executor.submit(testTask);
        }

        assertTrue(results == 0);
        assertTrue(executor.getTasksActive() == 3);
        assertTrue(executor.getTasksQueue() == 4);
        assertTrue(executor.getTasksInProcess() == 7);

        // Esta operación debería bloquear el executor hasta que termine una tarea
        finishDelay(testTasks.get(0), 1);
        TestTask testTask = new TestTask(7);
        testTasks.add(testTask);
        executor.submit(testTask);
        assertTrue(executor.getTasksActive() == 3);
        assertTrue(executor.getTasksQueue() == 4);
        assertTrue(executor.getTasksInProcess() == 7);

        // Espera a que solo queden 3 resultados pdtes de ejecución 
        for (int i = 1; i < 5; i++) {
            finishDelay(testTasks.get(i), 1);
        }
        executor.waitTo(3);
        assertTrue(executor.getTasksInProcess() == 3);
        Thread.sleep(500); // Espera que se puedan ejecutar todos los callbacks
        assertTrue(results == 5);

        for (int i = 5; i < testTasks.size(); i++) {
            finishDelay(testTasks.get(i), 1);
        }
        executor.waitTerminateAll();
        assertTrue(executor.getTasksInProcess() == 0);
        Thread.sleep(500); // Espera que se puedan ejecutar todos los callbacks
        assertTrue(results == 8);
    }

    @Ignore
    @Test
    public void test2() throws InterruptedException {
        BlockingExecutorService<Integer> executor = new BlockingExecutorService<>(2, 1);
        List<TestTask> testTasks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TestTask testTask = new TestTask(i);
            testTasks.add(testTask);
            executor.submit(testTask);
        }
        assertTrue(executor.getTasksActive() == 2);
        assertTrue(executor.getTasksQueue() == 1);
        assertTrue(executor.getTasksInProcess() == 3);

        testTasks.get(0).finish();
        executor.waitTo(2);

        assertTrue(executor.getTasksActive() == 2);
        assertTrue(executor.getTasksQueue() == 0);
        assertTrue(executor.getTasksInProcess() == 2);
    }

    private void finishDelay(TestTask testTask, long seconds) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(seconds * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                testTask.finish();
            }
        }).start();
    }

    class TestTask implements Callable<Integer> {
        private final int valor;
        private final Semaphore semaphore;


        public TestTask(int valor) {
            this.valor = valor;
            this.semaphore = new Semaphore(0);
        }

        @Override
        public Integer call() throws Exception {
            int value = valor + 1;
            if (value > 5) {
                value = 5;
            }
            semaphore.acquire();
            return valor;
        }

        public void finish() {
            semaphore.release();
        }
    }
}
