package net.scero.test.blockingexecutorservice;

/**
 * Interface para definir un callback para la clase BlockingExecutorService
 * 
 * @author jnogueira
 * @data 2016-11-09
 *
 * @param <T>
 */
@FunctionalInterface
public interface IExecutorServiceCallback<T> {
    /**
     * @param result
     */
    public void run(T result);
}
