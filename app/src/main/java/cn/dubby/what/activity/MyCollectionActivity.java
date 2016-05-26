package cn.dubby.what.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
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
import cn.dubby.what.constant.URLConstant;
import cn.dubby.what.domain.circle.Theme;
import cn.dubby.what.domain.user.User;
import cn.dubby.what.dto.Result;
import cn.dubby.what.utils.MessagesContainer;
import cn.dubby.what.utils.TimeFormat;
import cn.dubby.what.utils.ToastUtils;
import cn.dubby.what.volleyx.MyRequest;

public class MyCollectionActivity extends AppCompatActivity {

    private FreshListView freshListView;
    private LinkedList<Map<String, Object>> data = new LinkedList<>();
    private AdapterWithNetwork adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        reload();
    }

    private void initView() {
        setContentView(R.layout.activity_my_collection);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        freshListView = (FreshListView) findViewById(R.id.freshListView);
        adapter = new AdapterWithNetwork(this, data, R.layout.component_theme_list_item,
                new String[]{"image", "user_name", "content", "time", "location", "id"},
                new int[]{R.id.image, R.id.user_name, R.id.content, R.id.time, R.id.location, R.id.idTv});

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
                startActivity(new Intent(MyCollectionActivity.this, CommentActivity.class));
                overridePendingTransition(R.animator.zoomin, R.animator.zoomout);
            }
        });

        freshListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //注册上下文菜单
                MyCollectionActivity.this.registerForContextMenu(freshListView);
                MessagesContainer.CURRENT_THEME_ID = (long) data.get(position - 1).get("id");
                return false;
            }
        });
    }

    private void reload() {
        final Map map = new HashMap();
        map.put("token", MessagesContainer.TOKEN);

        MyRequest request = new MyRequest(URLConstant.THEME.COLLECT_LIST, map, new Response.Listener<Result>() {
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
                    freshListView.onRefreshComplete();
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // set context menu title
        menu.setHeaderTitle("主题操作");
        // add context menu item
        menu.add(0, 1, Menu.NONE, "取消关注");
        menu.add(0, 2, Menu.NONE, "举报");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                //取消关注
                cancelCollectTheme();
                break;
            case 2:
                //取消
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.animator.zoomin, R.animator.zoomout);
    }

    private void cancelCollectTheme() {
        Map map = new HashMap();
        map.put("tid", MessagesContainer.CURRENT_THEME_ID + "");
        map.put("token", MessagesContainer.TOKEN);
        MyRequest request = new MyRequest(URLConstant.THEME.CANCEL_COLLECT, map,
                new Response.Listener<Result>() {
                    @Override
                    public void onResponse(Result response) {
                        if (response.getSuccess())
                            ToastUtils.showShort(MyCollectionActivity.this, "取消关注成功");
                        else
                            ToastUtils.showShort(MyCollectionActivity.this, "取消关注失败");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        MyApplication.addToRequestQueue(request);
    }
}
