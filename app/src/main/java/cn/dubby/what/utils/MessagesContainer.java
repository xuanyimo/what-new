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

    public static String CURRENT_USER_IMAGE;

    public static User CURRENT_USER;

    public static String TOKEN;

    public static String LOCATION;

    public static String getUserImage() {
        if (!StringUtils.isEmpty(CURRENT_USER_IMAGE))
            return CURRENT_USER_IMAGE;
        CURRENT_USER_IMAGE = (String) SharedPreferencesUtils.getParam(MyApplication.context, SharedConstant.USER_PICTURE, "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1464019385&di=91f148fe63af2efecabdc2c8ea1ca811&src=http://p7.qhimg.com/t01cd54e217033aab22.jpg");
        return CURRENT_USER_IMAGE;
    }

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
