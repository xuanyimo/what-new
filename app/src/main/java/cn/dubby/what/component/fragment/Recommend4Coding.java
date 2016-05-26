package cn.dubby.what.component.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import cn.dubby.what.R;
import cn.dubby.what.application.MyApplication;
import cn.dubby.what.component.AdapterWithNetwork;
import cn.dubby.what.constant.URLConstant;
import cn.dubby.what.domain.circle.Theme;
import cn.dubby.what.domain.user.User;
import cn.dubby.what.dto.Result;
import cn.dubby.what.utils.MessagesContainer;
import cn.dubby.what.utils.TimeFormat;
import cn.dubby.what.volleyx.MyRequest;

/**
 * Created by dubby on 16/5/24.
 */
public class Recommend4Coding extends Fragment {

    private View rootView;

    private LinkedList<Map<String, Object>> data = new LinkedList<>();
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AdapterWithNetwork adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recommend_coding, container, false);
        initView();
        reload();
        return rootView;
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        listView = (ListView) rootView.findViewById(R.id.listView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
        adapter = new AdapterWithNetwork(rootView.getContext(), data, R.layout.component_theme_list_item,
                new String[]{"image", "user_name", "content", "time", "location", "id"},
                new int[]{R.id.image, R.id.user_name, R.id.content, R.id.time, R.id.location, R.id.idTv});
        listView.setAdapter(adapter);
    }

    private void reload() {
        MyRequest request = new MyRequest(URLConstant.THEME.RECOMMEND_LIST, null, new Response.Listener<Result>() {
            @Override
            public void onResponse(Result response) {
                if (response.getErrorCode() == 0) {
                    //先设置时间和内容
                    data.clear();
                    JSONArray jsonArray = (JSONArray) response.getData();
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Theme theme = new Theme(jsonObject);
                            Map m = new HashMap();
                            m.put("content", theme.content);
                            m.put("time", TimeFormat.format(theme.createTime));
                            m.put("id", theme.serverId);
                            m.put("createBy", theme.createBy);
                            if (jsonObject.has("location")) {
                                m.put("location", jsonObject.getString("location"));
                            }
                            data.add(m);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    //在设置头像和用户名
                    for (final Map them : data) {
                        long id = Long.parseLong(them.get("createBy").toString());

                        Map parameter4UserInfo = new HashMap();
                        parameter4UserInfo.put("uid", id + "");
                        MyRequest request = new MyRequest(URLConstant.USER.INFO, parameter4UserInfo, new Response.Listener<Result>() {
                            @Override
                            public void onResponse(Result response) {
                                if (response.getErrorCode() == 0) {
                                    JSONObject jsonObject = (JSONObject) response.getData();
                                    if (jsonObject == null)
                                        return;
                                    try {
                                        User user = new User(jsonObject);
                                        them.put("image", user.headImg);
                                        them.put("user_name", user.email);
                                        Log.i("image", user.headImg);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

                        MyApplication.addToRequestQueue(request);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.addToRequestQueue(request);
    }
}
