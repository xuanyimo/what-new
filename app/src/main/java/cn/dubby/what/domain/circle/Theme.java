package cn.dubby.what.domain.circle;


import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import cn.dubby.what.constant.ActiveAndroidConstant;
import cn.dubby.what.domain.base.BaseEntity;

/**
 * Created by Administrator on 2015/11/21.
 */
@Table(name = "theme", id = ActiveAndroidConstant.ID)
public class Theme extends BaseEntity {

    public Theme() {

    }

    public Theme(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        if (jsonObject.has("uid"))
            this.uid = jsonObject.getLong("uid");

        if (jsonObject.has("circleId"))
            this.circleId = jsonObject.getLong("circleId");

        if (jsonObject.has("cid"))
            this.cid = jsonObject.getLong("cid");

        if (jsonObject.has("content"))
            this.content = jsonObject.getString("content");

        if (jsonObject.has("postsNum"))
            this.postsNum = jsonObject.getLong("postsNum");

        if (jsonObject.has("viewNum"))
            this.viewNum = jsonObject.getLong("viewNum");

        if (jsonObject.has("praiseNum"))
            this.praiseNum = jsonObject.getLong("praiseNum");

        if (jsonObject.has("location"))
            this.location = jsonObject.getString("location");
    }

    @Column(name = "uid")
    public Long uid;

    @Column(name = "circle_id")
    public Long circleId;

    @Column(name = "cid")
    public Long cid;

    @Column(name = "content")
    public String content;

    @Column(name = "posts_num")
    public Long postsNum;

    @Column(name = "view_num")
    public Long viewNum;

    @Column(name = "praise_num")
    public Long praiseNum;

//    public List<Post> posts;

    @Column(name = "delete_flag")
    public Integer deleteFlag;

    @Column(name = "location")
    public String location;
}
