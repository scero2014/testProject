package net.scero.test.db;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.mybatis.spring.MyBatisSystemException;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase generica para acceder a los resultados de una Query de BD como si se tratase de un Iterador
 * @author jnogueira
 * @data 2017-09-07
 *
 * @param <T>
 */
@Slf4j
public abstract class ThreadResultHandler<T> implements ResultHandler<T> ,Runnable {
    //---- Variables ----//
    private final LinkedBlockingQueue<Object> data;
    private final Nullable                    END = new Nullable();
    private Thread                            readerThread;
    private boolean                           runningQuery;
    private long                              sizeRead;

    //---- Constructors ----//
    public ThreadResultHandler() {
        this(100);
    }
    
    public ThreadResultHandler(int bufferSize) {
        data = new LinkedBlockingQueue<Object>(bufferSize);
        runningQuery = true;
        sizeRead = 0;
    }

    //---- Public Methods ----//
    public ThreadResultHandler<T> start() {
        readerThread = new Thread(this);
        readerThread.start();
        return this;
    }

    @Override
    public void run() {
        try {
            query();
        } catch (Exception e) {
            log.error("Error in query", e);
        } finally {
            runningQuery = false;
        }

        try {
            data.put(END);
        } catch (InterruptedException e) {
            log.error("Error insert null data");
        }
    }

    @Override
    public void handleResult(ResultContext<? extends T> context) {
        try {
            data.put(context.getResultObject());
        } catch (Exception e) {
            log.error("Error handling Mybatis result: ", e);
            throw new MyBatisSystemException(e);
        }
    }

    /**
     * Se define la Query que deberá lanzarse, el último parámetro debe ser esta clase (this), para que sea tomada como handler.
     *   
     * Ejemplo:
     *    mapper.nombreDelMetodoDeLaQuery(param1, param2, this);
     *    
     */
    public abstract void query() throws Exception;

    /**
     * Devuelve el siguiente elemento. Si no hay elementos pendientes devuelve null
     * @return
     */
    public T next() {
        Object result;

        if (runningQuery || !data.isEmpty()) {
            if (!data.isEmpty()) {
                // Se toma el siguiente elemento pendiente
                result = data.poll();
            } else {
                // Se espera a que el productor introduzca valores
                try {
                    result = data.take();
                    if (result != null) {
                        sizeRead++;
                    }
                } catch (InterruptedException e) {
                    result = null;
                }
            }
        } else {
            result = null;
        }
        return result != null && !(result instanceof ThreadResultHandler.Nullable) ? (T) result : null;
    }

    /**
     * Detiene el hilo asíncrono de lectura y los consumidores de la cola de datos
     */
    public void release() {
        runningQuery = false;
        interruptReader();
    }

    //---- Private Methods ----//
    private void interruptReader() {
        readerThread.interrupt();
    }    
    
    //---- Auxiliar Classes ----//
    class Nullable {
    }
}
