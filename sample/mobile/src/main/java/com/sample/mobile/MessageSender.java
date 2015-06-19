package com.sample.mobile;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.vergal.library.dataproxy.sender.Sender;

import static com.google.android.gms.wearable.Wearable.MessageApi;
import static com.google.android.gms.wearable.Wearable.NodeApi;

public class MessageSender implements Sender {

    private GoogleApiClient mGoogleApiClient;

    public MessageSender(final Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context).addApi(Wearable.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onInvokeStream(final byte[] data) {
        NodeApi.getConnectedNodes(mGoogleApiClient)
               .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {

                   @Override
                   public void onResult(final NodeApi.GetConnectedNodesResult result) {
                       if (result.getNodes() == null) {
                           return;
                       }

                       for (Node node : result.getNodes()) {
                           MessageApi.sendMessage(mGoogleApiClient,
                                                  node.getId(),
                                                  "data",
                                                  data);
                       }
                   }
               });
    }
}
