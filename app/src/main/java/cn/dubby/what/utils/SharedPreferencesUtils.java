package cn.dubby.what.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by dubby on 16/5/1.
 */
public class SharedPreferencesUtils {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "PSSDK";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context, String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key,
                                  Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 获取复杂的对象
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getObjectParam(Context context, String key, Object defaultObject) {
        String string = (String) getParam(context, key, defaultObject);
        if (string != null && !string.equals("")) {
            return String2Object(string);
        }
        return null;
    }

    /**
     * 设置负责的对象
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setObjectParam(Context context, String key, Object object) {
        setParam(context, key, Object2String(object));
    }

    /**
     * 将Object转化为String
     *
     * @param object
     * @return
     */
    private static String Object2String(Object object) {

        String string = null;
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = null;
        try {

            // 将得到的字符数据装载到ObjectOutputStream
            os = new ObjectOutputStream(bos);
            // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
            os.writeObject(object);
            // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
            string = new String(
                    Base64.encode(bos.toByteArray(), Base64.DEFAULT));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return string;
    }

    /**
     * 将String转化为Object
     *
     * @param string
     * @return
     */
    private static Object String2Object(String string) {
        Object object = null;
        byte[] mobileBytes = Base64.decode(string.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream bis = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bis);
            object = ois.readObject();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }
}
