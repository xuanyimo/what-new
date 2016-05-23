package cn.dubby.what.domain.base;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by bixiaofeng on 2015/11/11.
 */
public class BaseEntity extends Model {

    public BaseEntity() {

    }

    public BaseEntity(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("id"))
            this.serverId = jsonObject.getLong("id");

        if (jsonObject.has("createTime"))
            this.createTime = new Date(jsonObject.getLong("createTime"));

        if (jsonObject.has("modifiedTime"))
            this.modifiedTime = new Date(jsonObject.getLong("modifiedTime"));

        if (jsonObject.has("createBy"))
            this.createBy = jsonObject.getLong("createBy");

        if (jsonObject.has("modifiedBy"))
            this.modifiedBy = jsonObject.getLong("modifiedBy");
    }

    //这是服务器上的id
    @Column(name = "server_id")
    public Long serverId;

    @Column(name = "create_time")
    public Date createTime;
    @Column(name = "modified_time")
    public Date modifiedTime;
    @Column(name = "create_by")
    public Long createBy;
    @Column(name = "modified_by")
    public Long modifiedBy;

}
