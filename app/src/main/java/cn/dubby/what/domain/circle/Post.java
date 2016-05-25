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
@Table(name = "post", id = ActiveAndroidConstant.ID)
public class Post extends BaseEntity {

    public Post() {
    }

    public Post(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        if (jsonObject.has("tid"))
            tid = jsonObject.getLong("tid");
        if (jsonObject.has("content"))
            content = jsonObject.getString("content");
        if (jsonObject.has("rid"))
            rid = jsonObject.getLong("rid");
        if (jsonObject.has("deleteFlag"))
            deleteFlag = jsonObject.getInt("deleteFlag");
    }

    @Column(name = "tid")
    public Long tid;

    @Column(name = "content")
    public String content;

    //回复的上一回复的ID
    @Column(name = "rid")
    public Long rid;

    @Column(name = "delete_flag")
    public Integer deleteFlag;

}
