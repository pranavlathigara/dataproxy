package com.vergal.library.dataproxy;

import com.vergal.library.dataproxy.sender.Sender;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public final class DataProxy {

    /**
     * Singleton class instance.
     */
    private static DataProxy sInstance;

    /**
     * Registered object to forward method invokes.
     */
    private final List<Object> mRegistered;

    /**
     * Get DataProxy singleton reference.
     *
     * @return singleton reference.
     */
    public static DataProxy get() {
        if (sInstance == null) {
            sInstance = new DataProxy();
        }

        return sInstance;
    }

    private DataProxy() {
        mRegistered = new ArrayList<>();
    }

    /**
     * Build a interface proxy to stream its methods. Only interface with void method is allowed.
     *
     * @param clazz  interface class.
     * @param sender sender to post method stream.
     * @return Interface mock.
     */
    @SuppressWarnings("unchecked")
    public <T> T build(final Class<T> clazz, final Sender sender) {
        validateClass(clazz);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                                          new Class[]{clazz},
                                          new ProxyInvocationHandler(clazz, sender));
    }

    /**
     * Register object to post method stream.
     *
     * @param object
     */
    public void register(final Object object) {
        if (!mRegistered.contains(object)) {
            mRegistered.add(object);
        }
    }

    /**
     * Unregister object to post method stream.
     *
     * @param object
     */
    public void unregister(final Object object) {
        mRegistered.remove(object);
    }

    /**
     * Inject method data stream that will try to call the correct method in  the all registered
     * objects.
     *
     * @param data data stream.
     */
    public void inject(byte[] data) {
        final MethodModel model = MethodModel.fromBytes(data);
        if (model != null) {
            invoke(model);
        }
    }

    /**
     * Validates specified class.
     *
     * @param clazz class to validate.
     */
    private void validateClass(Class<?> clazz) {
        if (!clazz.isInterface()) {
            throw new DataProxyException("Only interfaces is allowed");
        } else {
            final Method[] methods = clazz.getMethods();
            if (methods != null) {
                for (final Method method : methods) {
                    if (method.getReturnType() != Void.TYPE) {
                        throw new RuntimeException("Only void method is allowed");
                    }
                }
            }
        }
    }

    /**
     * Try to invoke corresponds method of specified MethodModel.
     *
     * @param model model.
     */
    private void invoke(final MethodModel model) {
        if (model.isValid()) {
            for (final Object object : mRegistered) {
                if (model.getCallerClass().isInstance(object)) {
                    try {
                        final Method method = object.getClass().getMethod(model.getMethodName(),
                                                                          model.getArgsClasses());
                        method.invoke(object, model.getArgs());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}