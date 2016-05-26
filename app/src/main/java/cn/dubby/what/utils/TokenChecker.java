package cn.dubby.what.utils;

import cn.dubby.what.application.MyApplication;
import cn.dubby.what.constant.ErrorCode;
import cn.dubby.what.constant.SharedConstant;
import cn.dubby.what.domain.user.Message;
import cn.dubby.what.dto.Result;

/**
 * Created by dubby on 16/5/26.
 */
public class TokenChecker {
    public static boolean check(Result result) {
        if (result.getErrorCode() == ErrorCode.TOKEN_ERROR.first) {
            MessagesContainer.CURRENT_USER = null;
            MessagesContainer.TOKEN = null;
            MessagesContainer.CURRENT_USER_IMAGE = null;

            SharedPreferencesUtils.setParam(MyApplication.context, SharedConstant.USER_PICTURE, "");
            SharedPreferencesUtils.setParam(MyApplication.context, SharedConstant.USER_ID, 0l);
            SharedPreferencesUtils.setParam(MyApplication.context, SharedConstant.USER, null);
            return true;
        }
        return false;
    }
}
