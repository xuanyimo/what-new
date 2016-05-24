package cn.dubby.what.domain.user;


import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cn.dubby.what.constant.ActiveAndroidConstant;
import cn.dubby.what.domain.base.BaseEntity;


/**
 * Created by bixiaofeng on 2015/11/11.
 */
@Table(name = "user", id = ActiveAndroidConstant.ID)
public class User extends BaseEntity {

    public User() {

    }

    public User(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        if (jsonObject.has("username")) {
            this.username = jsonObject.getString("username");
        }
        if (jsonObject.has("password")) {
            this.password = jsonObject.getString("password");
        }
        if (jsonObject.has("nickname")) {
            this.nickname = jsonObject.getString("nickname");
        }
        if (jsonObject.has("phone")) {
            this.phone = jsonObject.getString("phone");
        }
        if (jsonObject.has("email")) {
            this.email = jsonObject.getString("email");
        }
        if (jsonObject.has("emailChecked")) {
            this.emailChecked = jsonObject.getInt("emailChecked");
        }
        if (jsonObject.has("gender")) {
            this.gender = jsonObject.getInt("gender");
        }
        if (jsonObject.has("birthday")) {
            this.birthday = new Date(jsonObject.getLong("birthday"));
        }
        if (jsonObject.has("city")) {
            this.city = jsonObject.getString("city");
        }
        if (jsonObject.has("headImg")) {
            this.headImg = jsonObject.getString("headImg");
        }
        if (jsonObject.has("themesNum")) {
            this.themesNum = jsonObject.getLong("themesNum");
        }
        if (jsonObject.has("signTimes")) {
            this.signTimes = jsonObject.getLong("signTimes");
        }

    }


    @Column(name = "username")
    public String username;

    @Column(name = "password")
    public String password;

    @Column(name = "delete_flag")
    public Integer deleteFlag;

    @Column(name = "nickname")
    public String nickname;

    @Column(name = "phone")
    public String phone;

    @Column(name = "email")
    public String email;

    @Column(name = "email_checked")
    public Integer emailChecked;

    @Column(name = "gender")
    public Integer gender;

    @Column(name = "birthday")
    public Date birthday;

    @Column(name = "city")
    public String city;

    @Column(name = "last_login_time")
    public Date lastLoginTime;

    @Column(name = "login_times")
    public Long loginTimes;

    @Column(name = "themes_num")
    public Long themesNum;

    @Column(name = "sign_times")
    public Long signTimes;

    @Column(name = "group_num")
    public Integer groupNum;

    @Column(name = "head_img")
    public String headImg;
}
