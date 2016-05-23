package cn.dubby.what.utils;

import cn.dubby.what.application.MyApplication;
import cn.dubby.what.constant.SharedConstant;
import cn.dubby.what.domain.user.User;

/**
 * Created by dubby on 16/5/21.
 */
public class MessagesContainer {
    public static long CURRENT_THEME_ID;

    public static long CURRENT_CIRCLE_ID;

    public static User CURRENT_USER;

    public static String TOKEN;

    public static String LOCATION;

    public static String getEmail() {
        if (CURRENT_USER != null && !StringUtils.isEmpty(CURRENT_USER.email))
            return CURRENT_USER.email;
        else {
            long id = (long) SharedPreferencesUtils.getParam(MyApplication.context, SharedConstant.USER_ID, 0l);
            String email = (String) SharedPreferencesUtils.getParam(MyApplication.context, SharedConstant.EMAIL, "");
            if (id != 0 && !StringUtils.isEmpty(email)) {
                CURRENT_USER = new User();
                CURRENT_USER.email = email;
                CURRENT_USER.serverId = id;
                return email;
            }
            return null;
        }
    }

    public static int getOrientation() {
        int o = (int) SharedPreferencesUtils.getParam(MyApplication.context, SharedConstant.ORIENTATION, 1);
        return o;
    }
}
