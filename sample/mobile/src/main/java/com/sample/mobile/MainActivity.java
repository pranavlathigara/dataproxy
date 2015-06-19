package com.sample.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sample.common.TestInterface;
import com.vergal.library.dataproxy.DataProxy;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements TestInterface {

    @InjectView(R.id.test_output)
    TextView mOutput;

    final DataReceiver mDataReceiver = new DataReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        ButterKnife.inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        DataProxy.get().register(this);
        mDataReceiver.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        DataProxy.get().unregister(this);
        mDataReceiver.unregister(this);
    }

    @OnClick(R.id.test_no_params)
    void callNoParams() {
        DataProxy.get()
                 .build(TestInterface.class, new BroadcastSender(this))
                 .onTestNoParameters();
        DataProxy.get()
                 .build(TestInterface.class, new MessageSender(this))
                 .onTestNoParameters();
    }

    @OnClick(R.id.test_primitives)
    void callPrimitives() {
        DataProxy.get()
                 .build(TestInterface.class, new BroadcastSender(this))
                 .onTestPrimitive("data", 123, 321, 0.4f, 0.5d, (short) 1, 'c', true, (byte) 1);
        DataProxy.get()
                 .build(TestInterface.class, new MessageSender(this))
                 .onTestPrimitive("data", 123, 321, 0.4f, 0.5d, (short) 1, 'c', true, (byte) 1);
    }

    @OnClick(R.id.test_serializable)
    void callSerializable() {
        DataProxy.get()
                 .build(TestInterface.class, new BroadcastSender(this))
                 .onTestSerializable(Environment.getExternalStorageDirectory());
        DataProxy.get()
                 .build(TestInterface.class, new MessageSender(this))
                 .onTestSerializable(Environment.getExternalStorageDirectory());
    }

    @OnClick(R.id.test_parcelable)
    void callParcelable() {
        DataProxy.get()
                 .build(TestInterface.class, new BroadcastSender(this))
                 .onTestParcelable(new Intent("intent_action"));
        DataProxy.get()
                 .build(TestInterface.class, new MessageSender(this))
                 .onTestParcelable(new Intent("intent_action"));
    }

    @Override
    public void onTestNoParameters() {
        final StringBuilder builder = new StringBuilder();
        builder.append("onTestNoParameters").append("\n");
        mOutput.setText(builder);
    }

    @Override
    public void onTestPrimitive(String s, final int i, final long l, final float f, final double d,
                                final short st, final char c, final boolean b, final byte bt) {
        StringBuilder builder = new StringBuilder();
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
        mOutput.setText(builder);
    }

    @Override
    public void onTestSerializable(final File file) {
        StringBuilder builder = new StringBuilder();
        builder.append("onTestSerializable").append("\n");
        builder.append("File.toString()=").append(file.toString());
        mOutput.setText(builder);
    }

    @Override
    public void onTestParcelable(final Intent intent) {
        StringBuilder builder = new StringBuilder();
        builder.append("onTestParcelable").append("\n");
        builder.append("intent.getAction()=").append(intent.getAction());
        mOutput.setText(builder);
    }
}
