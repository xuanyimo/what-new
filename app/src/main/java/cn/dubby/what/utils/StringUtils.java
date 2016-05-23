package cn.dubby.what.utils;

/**
 * Created by dubby on 16/5/12.
 */
public class StringUtils {
    public static boolean isEmpty(String string) {
        if (string == null || "".equals(string))
            return true;
        return false;
    }
}
