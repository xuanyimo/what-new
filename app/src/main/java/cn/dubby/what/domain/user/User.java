package cn.dubby.what.domain.user;


import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

import cn.dubby.what.constant.ActiveAndroidConstant;
import cn.dubby.what.domain.base.BaseEntity;


/**
 * Created by bixiaofeng on 2015/11/11.
 */
@Table(name = "user", id = ActiveAndroidConstant.ID)
public class User extends BaseEntity {
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
