package cn.dubby.what.dto;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dubby on 16/5/22.
 */
public class Result {

    public Result() {

    }

    public Result(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("success"))
            success = jsonObject.getBoolean("success");
        if (jsonObject.has("errorCode"))
            errorCode = jsonObject.getInt("errorCode");
        if (jsonObject.has("data"))
            data = jsonObject.get("data");
        if (jsonObject.has("msg"))
            msg = jsonObject.getString("msg");
    }

    private Boolean success;
    private Integer errorCode;
    private Object data;
    private String msg;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
