package cn.dubby.what.volleyx;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import cn.dubby.what.dto.Result;

/**
 * Created by dubby on 16/5/22.
 */
public class MyRequest extends Request<Result> {

    private final Response.Listener<Result> mListener;
    private Map<String, String> mParameter;

    public MyRequest(int method, String url, Map<String, String> parameter, Response.Listener<Result> listener,
                     Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        mParameter = parameter;
    }

    public MyRequest(String url, Map<String, String> parameter, Response.Listener<Result> listener,
                     Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mParameter = parameter;
    }

    @Override
    protected Response<Result> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, "utf-8");

            Log.i("MyRequest", jsonString);

            Result result = new Result();
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.has("success"))
                result.setSuccess(jsonObject.getBoolean("success"));
            else
                result.setSuccess(false);

            if (jsonObject.has("data"))
                result.setData(jsonObject.get("data"));
            if (jsonObject.has("errorCode"))
                result.setErrorCode(jsonObject.getInt("errorCode"));
            else
                result.setErrorCode(-1);
            if (jsonObject.has("msg"))
                result.setMsg(jsonObject.getString("msg"));

            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(Result response) {
        mListener.onResponse(response);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParameter;
    }
}
