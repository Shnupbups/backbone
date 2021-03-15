package dev.vatuu.backbone.events.base;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class ArrayBackendEvent<T> extends Event<T> {

    private final Class<? super T> type;
    private final Function<T[], T> invokerFactory;
    private final T dummyInvoker;
    private final Lock lock = new ReentrantLock();
    private T[] handlers;

    ArrayBackendEvent(Class<? super T> type, T dummyInvoker, Function<T[], T> invokerFactory) {
        this.type = type;
        this.dummyInvoker = dummyInvoker;
        this.invokerFactory = invokerFactory;
        update();
    }

    @SuppressWarnings("unchecked")
    void update() {
        if(handlers == null) {
            if(dummyInvoker != null)
                invoker = dummyInvoker;
            else
                invoker = invokerFactory.apply((T[])Array.newInstance(type, 0));
        } else if(handlers.length == 1)
            invoker = handlers[0];
        else
            invoker = invokerFactory.apply(handlers);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T register(T listener) {
        if (listener == null)
            throw new NullPointerException("Tried to register a null listener!");

        lock.lock();

        try {
            if (handlers == null) {
                handlers = (T[]) Array.newInstance(type, 1);
                handlers[0] = listener;
            } else
                handlers = ArrayUtils.add(handlers, listener);

            update();
        } finally {
            lock.unlock();
        }

        return listener;
    }

    @Override
    public void unregister(T listener) {
        if(listener == null || handlers == null)
            return;

        lock.lock();

        try {
           int index = ArrayUtils.indexOf(handlers, listener);
            if(index != ArrayUtils.INDEX_NOT_FOUND) {
                if(handlers.length == 1)
                    handlers = null;
                else
                    handlers = ArrayUtils.remove(handlers, index);
                update();
            }
        } finally {
            lock.unlock();
        }
    }
}
