package com.vergal.library.dataproxy;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to hold method information and parameters.
 */
class MethodModel implements Serializable {

    /**
     * Map to convert object class to primitives class.
     */
    private static final Map<String, Class<?>> PRIMITIVES = new HashMap<String, Class<?>>() {{
        put(Integer.class.getName(), int.class);
        put(Long.class.getName(), long.class);
        put(Float.class.getName(), float.class);
        put(Double.class.getName(), double.class);
        put(Short.class.getName(), short.class);
        put(Boolean.class.getName(), boolean.class);
        put(Character.class.getName(), char.class);
        put(Byte.class.getName(), byte.class);
    }};

    /**
     * Interface caller class.
     */
    private Class<?> mCallerClass;

    /**
     * Called method name.
     */
    private String mMethodName;

    /**
     * Method args class.
     */
    private Class<?>[] mArgsClasses;

    /**
     * Method args objects.
     */
    private Object[] mArgs;

    /*.*.*.*.*.*.*.*.*.* SETTERS *.*.*.*.*.*.*.*.*.*/

    public void setCallerClass(Class<?> clazz) {
        mCallerClass = clazz;
    }

    public void setMethodName(String methodName) {
        mMethodName = methodName;
    }

    public void setArgs(Object[] args) {
        if (args != null && args.length > 0) {
            mArgs = args;
            mArgsClasses = new Class[mArgs.length];

            toParcelHolder();
            toPrimitives();
        }
    }

    /*.*.*.*.*.*.*.*.*.* GETTERS *.*.*.*.*.*.*.*.*.*/

    public Object[] getArgs() {
        toParcelable();
        return mArgs;
    }

    public Class<?>[] getArgsClasses() {
        return mArgsClasses;
    }

    public String getMethodName() {
        return mMethodName;
    }

    public Class<?> getCallerClass() {
        return mCallerClass;
    }

    public boolean isValid() {
        return mCallerClass != null && mMethodName != null;
    }

    /*.*.*.*.*.*.*.*.*.* INTERNALS *.*.*.*.*.*.*.*.*.*/

    /**
     * Check method args and convert all parcelable into a serializable holder.
     */
    private void toParcelHolder() {
        if (mArgs != null && mArgsClasses != null)
            for (int i = 0; i < mArgs.length; i++) {
                mArgsClasses[i] = mArgs[i].getClass();
                if (Parcelable.class.isInstance(mArgs[i])) {
                    final ParcelHolder data = new ParcelHolder();
                    data.setData(mArgs[i], mArgsClasses[i]);
                    mArgs[i] = data;
                }
            }
    }

    /**
     * Check methods args and convert all ParcelHolder back to parcelable type.
     */
    private void toParcelable() {
        if (mArgs != null) {
            for (int i = 0; i < mArgs.length; i++) {
                if (ParcelHolder.class.isInstance(mArgs[i])) {
                    final ParcelHolder data = (ParcelHolder) mArgs[i];
                    mArgs[i] = data.getData();
                }
            }
        }
    }

    /**
     * Check method args classes and convert all primitive object representation class to
     * primitive class.
     */
    private void toPrimitives() {
        if (mArgsClasses != null) {
            for (int i = 0; i < mArgsClasses.length; i++) {
                final Class<?> clazz = PRIMITIVES.get(mArgsClasses[i].getName());
                if (clazz != null) {
                    mArgsClasses[i] = clazz;
                }
            }
        }
    }

    /*.*.*.*.*.*.*.*.*.* CONVERTERS *.*.*.*.*.*.*.*.*.*/

    /**
     * Convert this object to byte array.
     *
     * @return byte array.
     */
    public byte[] toBytes() {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] data = null;

        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(this);
            data = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                bos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return data;
    }

    /**
     * Try to convert byte array into a method model.
     *
     * @param data data to convert.
     * @return null if could not convert.
     */
    public static MethodModel fromBytes(final byte[] data) {
        final ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInput in = null;
        MethodModel model = null;
        try {
            in = new ObjectInputStream(bis);
            model = (MethodModel) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return model;
    }

     /*.*.*.*.*.*.*.*.*.* INNER CLASSES *.*.*.*.*.*.*.*.*.*/

    /**
     * Serializable class that hold parcelable values as byte array.
     */
    private static class ParcelHolder implements Serializable {

        /**
         * Parcelable class.
         */
        private Class<?> mParcelableClass;

        /**
         * Parcelable object as byte array.
         */
        private byte[] mParcelableData;

        /**
         * Set parcelable data and convert it to byte array.
         *
         * @param parcelableObject parcelable object.
         * @param parcelableClass  parcelable object class.
         */
        public void setData(final Object parcelableObject, final Class<?> parcelableClass) {
            mParcelableClass = parcelableClass;

            //Convert parcelable to byte array.
            Parcelable parcelable = (Parcelable) parcelableObject;
            final Parcel parcel = Parcel.obtain();
            parcelable.writeToParcel(parcel, 0);
            mParcelableData = parcel.marshall();
            parcel.recycle();
        }

        /**
         * Return parcelable object converted from a byte array.
         *
         * @return null if could not convert it back.
         */
        public Object getData() {
            try {
                //Convert byte array to back to parcelable object.
                final Field field = mParcelableClass.getField("CREATOR");
                final Parcelable.Creator<?> creator = (Parcelable.Creator<?>) field.get(null);
                final Parcel parcel = Parcel.obtain();
                parcel.unmarshall(mParcelableData, 0, mParcelableData.length);
                parcel.setDataPosition(0);
                return creator.createFromParcel(parcel);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
