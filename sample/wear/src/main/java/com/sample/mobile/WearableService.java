package com.sample.mobile;

import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.sample.common.TestInterface;
import com.vergal.library.dataproxy.DataProxy;

import java.io.File;

public class WearableService extends WearableListenerService implements TestInterface {

    private Toast mToast;

    @Override
    public void onCreate() {
        super.onCreate();
        DataProxy.get().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DataProxy.get().unregister(this);
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        if (messageEvent.getPath().contains("data")) {
            DataProxy.get().inject(messageEvent.getData());
        }
    }

    @Override
    public void onTestNoParameters() {
        final StringBuilder builder = new StringBuilder();
        builder.append("onTestNoParameters").append("\n");
        notification(builder.toString());
    }

    @Override
    public void onTestPrimitive(String s, final int i, final long l, final float f, final double d,
                                final short st, final char c, final boolean b, final byte bt) {
        final StringBuilder builder = new StringBuilder();
        builder.append("onTestPrimitive").append("\n");
        builder.append("String=").append(s).append("\n");
        builder.append("int=").append(i).append("\n");
        builder.append("long=").append(l).append("\n");
        builder.append("float=").append(f).append("\n");
        builder.append("double=").append(d).append("\n");
        builder.append("short=").append(st).append("\n");
        builder.append("char=").append(c).append("\n");
        builder.append("boolean=").append(b).append("\n");
        builder.append("byte=").append(bt).append("\n");
        notification(builder.toString());
    }

    @Override
    public void onTestSerializable(final File file) {
        final StringBuilder builder = new StringBuilder();
        builder.append("onTestSerializable").append("\n");
        builder.append("File.toString()=").append(file.toString());
        notification(builder.toString());
    }

    @Override
    public void onTestParcelable(final Intent intent) {
        final StringBuilder builder = new StringBuilder();
        builder.append("onTestParcelable").append("\n");
        builder.append("intent.getAction()=").append(intent.getAction());
        notification(builder.toString());
    }

    private void notification(final String message) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
