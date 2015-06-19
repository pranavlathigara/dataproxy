package com.sample.common;

import android.content.Intent;

import java.io.File;

public interface TestInterface {

    void onTestNoParameters();

    void onTestPrimitive(String s, int i, long l, float f, double d, short st, char c,
                         boolean b, byte bt);

    void onTestSerializable(File file);

    void onTestParcelable(Intent intent);
}
