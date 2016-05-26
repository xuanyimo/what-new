package cn.dubby.what.constant;

import android.util.Pair;

/**
 * Created by dubby on 16/5/26.
 */
public class ErrorCode {
    public static final Pair<Integer, String> PHONE_FORMAT_ERROR = new Pair<Integer, String>(-11, "The format of phone is error!");
    public static final Pair<Integer, String> CIRCLE_NOT_EXIST = new Pair<Integer, String>(-10, "This circle what you are focusing on is not exist!");
    public static final Pair<Integer, String> TOKEN_ERROR = new Pair<Integer, String>(-9, "The token is error!");
    public static final Pair<Integer, String> TOKEN_EMPTY = new Pair<Integer, String>(-8, "The token is empty!");
    public static final Pair<Integer, String> USER_HAD_EXIST = new Pair<Integer, String>(-7, "The email has been registered!");
    public static final Pair<Integer, String> THEME_NOT_EXIST = new Pair<Integer, String>(-6, "The theme is not exist!");
    public static final Pair<Integer, String> NOT_LOGIN = new Pair<Integer, String>(-5, "Login first!");
    public static final Pair<Integer, String> PARAMS_ERROR = new Pair<Integer, String>(-4, "Parameters are error!");
    public static final Pair<Integer, String> PASSWORD_FORMAT_ERROR = new Pair<Integer, String>(-3, "The format of password is error!");
    public static final Pair<Integer, String> NOT_A_EMAIL = new Pair<Integer, String>(-2, "It is not a email!");
    public static final Pair<Integer, String> USER_NOT_EXIST = new Pair(-1, "User is not exist!");
    public static final Pair<Integer, String> SUCCESS = new Pair(0, "success!");
    public static final Pair<Integer, String> ERROR = new Pair<Integer, String>(1, "error!");
}
