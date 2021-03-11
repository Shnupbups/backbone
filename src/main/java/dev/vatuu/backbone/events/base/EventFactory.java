package dev.vatuu.backbone.events.base;

import java.util.function.Function;

public class EventFactory {
    private static boolean profilingEnabled = true;

    private EventFactory() { }

    /**
     * @return True if events are supposed to be profiled.
     */
    public static boolean isProfilingEnabled() {
        return profilingEnabled;
    }

    /**
     * Invalidate and re-create all existing "invoker" instances across
     * events created by this EventFactory. Use this if, for instance,
     * the profilingEnabled field changes.
     */
    // TODO: Turn this into an event?
    public static void invalidate() {
        EventFactoryImpl.invalidate();
    }

    /**
     * Create an "array-backed" Event instance.
     *
     * @param type           The listener class type.
     * @param invokerFactory The invoker factory, combining multiple listeners into one instance.
     * @param <T>            The listener type.
     * @return The Event instance.
     */
    public static <T> Event<T> createArrayBacked(Class<? super T> type, Function<T[], T> invokerFactory) {
        return EventFactoryImpl.createArrayBacked(type, invokerFactory);
    }

    /**
     * Create an "array-backed" Event instance with a custom empty invoker.
     *
     * <p>Having a custom empty invoker (of type (...) -&gt; {}) increases performance
     * relative to iterating over an empty array; however, it only really matters
     * if the event is executed thousands of times a second.
     *
     * @param type           The listener class type.
     * @param emptyInvoker   The custom empty invoker.
     * @param invokerFactory The invoker factory, combining multiple listeners into one instance.
     * @param <T>            The listener type.
     * @return The Event instance.
     */
    // TODO: Deprecate this once we have working codegen
    public static <T> Event<T> createArrayBacked(Class<T> type, T emptyInvoker, Function<T[], T> invokerFactory) {
        return EventFactoryImpl.createArrayBacked(type, emptyInvoker, invokerFactory);
    }

    /**
     * Get the listener object name. This can be used in debugging/profiling
     * scenarios.
     *
     * @param handler The listener object.
     * @return The listener name.
     */
    public static String getHandlerName(Object handler) {
        return handler.getClass().getName();
    }
}
