package com.sample.mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.vergal.library.dataproxy.DataProxy;

public class DataReceiver extends BroadcastReceiver {

    /**
     * Register this broadcastReceiver
     */
    public void register(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("broadcast");
        context.registerReceiver(this, intentFilter);
    }

    /**
     * Unregister this broadcastReceiver
     */
    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        DataProxy.get().inject(intent.getByteArrayExtra("data"));
    }
}
