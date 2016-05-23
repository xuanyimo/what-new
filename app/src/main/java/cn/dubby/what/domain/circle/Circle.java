package cn.dubby.what.domain.circle;


import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import cn.dubby.what.constant.ActiveAndroidConstant;
import cn.dubby.what.domain.base.BaseEntity;

/**
 * Created by Administrator on 2015/12/12.
 */
@Table(name = "circle", id = ActiveAndroidConstant.ID)
public class Circle extends BaseEntity {

    public Circle() {

    }

    public Circle(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        if (jsonObject.has("id"))
            this.serverId = jsonObject.getLong("id");
        if (jsonObject.has("focusNum"))
            this.focusNum = jsonObject.getLong("focusNum");
        if (jsonObject.has("themesNum"))
            this.themesNum = jsonObject.getLong("themesNum");
        if (jsonObject.has("researchNum"))
            this.researchNum = jsonObject.getLong("researchNum");
        if (jsonObject.has("deleteFlag"))
            this.deleteFlag = jsonObject.getInt("deleteFlag");
        if (jsonObject.has("description"))
            this.description = jsonObject.getString("description");
        if (jsonObject.has("logo"))
            this.logo = jsonObject.getString("logo");
        if (jsonObject.has("name"))
            this.name = jsonObject.getString("name");
    }

    @Column(name = "focus_num")
    public Long focusNum;

    @Column(name = "themes_num")
    public Long themesNum;

    @Column(name = "research_num")
    public Long researchNum;

    @Column(name = "delete_flag")
    public Integer deleteFlag;

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;

    @Column(name = "logo")
    public String logo;

}
