package cn.dubby.what.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

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
import cn.dubby.what.component.FreshListView;
import cn.dubby.what.component.dialog.CustomDialog;
import cn.dubby.what.constant.URLConstant;
import cn.dubby.what.domain.circle.Theme;
import cn.dubby.what.dto.Result;
import cn.dubby.what.utils.MessagesContainer;
import cn.dubby.what.utils.StringUtils;
import cn.dubby.what.utils.ToastUtils;
import cn.dubby.what.volleyx.MyRequest;

public class ThemeListActivity extends AppCompatActivity {

    private FreshListView freshListView;
    private LinkedList<Map<String, Object>> data = new LinkedList<>();
    private AdapterWithNetwork adapter;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void reload() {
        Log.i("CURRENT_CIRCLE_ID", "" + MessagesContainer.CURRENT_CIRCLE_ID);
        if (MessagesContainer.CURRENT_CIRCLE_ID <= 0)
            return;

        final Map map = new HashMap();
        map.put("cid", MessagesContainer.CURRENT_CIRCLE_ID + "");

        MyRequest request = new MyRequest(URLConstant.THEME.LIST, map, new Response.Listener<Result>() {
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
                            m.put("time", theme.createTime);
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
                    freshListView.onRefreshComplete();
                    //在设置头像和用户名
                    for (Map them : data) {
                        long id = Long.parseLong(them.get("id").toString());

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

    private void initView() {
        setContentView(R.layout.activity_theme_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        freshListView = (FreshListView) findViewById(R.id.freshListView);
        adapter = new AdapterWithNetwork(this, data, R.layout.component_theme_list_item,
                new String[]{"image", "user_name", "content", "time", "location"},
                new int[]{R.id.image, R.id.user_name, R.id.content, R.id.time, R.id.location});

        freshListView.setAdapter(adapter);
        freshListView.setonRefreshListener(new FreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });

        freshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessagesContainer.CURRENT_THEME_ID = (long) data.get(position - 1).get("id");
                startActivity(new Intent(ThemeListActivity.this, CommentActivity.class));
                overridePendingTransition(R.animator.zoomin, R.animator.zoomout);
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThemeListActivity.this, PostThemeActivity.class));
                overridePendingTransition(R.animator.zoomin, R.animator.zoomout);
            }
        });
    }

    private void initData() {
        reload();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.animator.zoomin, R.animator.zoomout);

    }
}
