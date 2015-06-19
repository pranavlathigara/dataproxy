package com.vergal.library.dataproxy;

import com.vergal.library.dataproxy.sender.Sender;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Class to get interface invoked method.
 */
class ProxyInvocationHandler implements InvocationHandler {

    /**
     * Sender for this handler.
     */
    private final Sender mSender;

    /**
     * Caller interface.
     */
    private final Class<?> mCallerClass;

    public ProxyInvocationHandler(final Class<?> clazz, final Sender sender) {
        mCallerClass = clazz;
        mSender = sender;
    }

    @Override
    public Object invoke(final Object proxy, final Method method,
                         final Object[] args) throws Throwable {

        //Convert method into a model.
        final MethodModel model = new MethodModel();
        model.setCallerClass(mCallerClass);
        model.setMethodName(method.getName());
        model.setArgs(args);

        //Send method stream to sender.
        mSender.onInvokeStream(model.toBytes());

        return null;
    }
}
