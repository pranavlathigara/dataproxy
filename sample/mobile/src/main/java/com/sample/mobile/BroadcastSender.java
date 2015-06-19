package com.sample.mobile;

import android.content.Context;
import android.content.Intent;

import com.vergal.library.dataproxy.sender.Sender;

public class BroadcastSender implements Sender {

    private Context mContext;

    public BroadcastSender(final Context context) {
        mContext = context;
    }

    @Override
    public void onInvokeStream(final byte[] data) {
        Intent intent = new Intent("broadcast");
        intent.putExtra("data", data);
        mContext.sendBroadcast(intent);
    }
}
