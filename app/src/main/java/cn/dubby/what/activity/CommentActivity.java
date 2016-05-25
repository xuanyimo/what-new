package cn.dubby.what.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
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
import cn.dubby.what.component.NoScrollListView;
import cn.dubby.what.component.dialog.CustomDialog;
import cn.dubby.what.constant.URLConstant;
import cn.dubby.what.domain.circle.Post;
import cn.dubby.what.domain.circle.Theme;
import cn.dubby.what.domain.user.User;
import cn.dubby.what.dto.Result;
import cn.dubby.what.utils.MessagesContainer;
import cn.dubby.what.utils.StringUtils;
import cn.dubby.what.utils.TimeFormat;
import cn.dubby.what.utils.ToastUtils;
import cn.dubby.what.volleyx.MyRequest;

public class CommentActivity extends AppCompatActivity {

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
        if (MessagesContainer.CURRENT_THEME_ID <= 0)
            return;

        final Map map = new HashMap();
        map.put("tid", MessagesContainer.CURRENT_THEME_ID + "");

        MyRequest request = new MyRequest(URLConstant.COMMENT.LIST, map, new Response.Listener<Result>() {
            @Override
            public void onResponse(Result response) {
                if (response.getErrorCode() == 0) {
                    //先设置时间和内容
                    data.clear();
                    JSONArray jsonArray = (JSONArray) response.getData();
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Post post = new Post(jsonObject);
                            Map m = new HashMap();
                            m.put("content", post.content);
                            m.put("time", TimeFormat.format(post.createTime));
                            m.put("id", post.serverId);
                            m.put("createBy", post.createBy);

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

    private void initView() {
        setContentView(R.layout.activity_comment);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        freshListView = (FreshListView) findViewById(R.id.listView);
        adapter = new AdapterWithNetwork(this, data, R.layout.component_comment_list_item,
                new String[]{"image", "user_name", "content", "time"},
                new int[]{R.id.image, R.id.user_name, R.id.content, R.id.time});

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

            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框
                CustomDialog.Builder builder = new CustomDialog.Builder(CommentActivity.this);
                builder.setTitle("我要评论");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //设置你的操作事项
                        String content = ((CustomDialog) dialog).contentEd.getText().toString();
                        if (StringUtils.isEmpty(content))
                            return;
                        final Map map = new HashMap();
                        map.put("tid", MessagesContainer.CURRENT_THEME_ID + "");
                        map.put("token", MessagesContainer.TOKEN);
                        map.put("content", content);

                        MyRequest request = new MyRequest(URLConstant.COMMENT.ADD, map, new Response.Listener<Result>() {
                            @Override
                            public void onResponse(Result response) {
                                if (response.getErrorCode() == 0) {
                                    ToastUtils.showShort(CommentActivity.this, "评论成功");
                                    reload();
                                } else {
                                    ToastUtils.showShort(CommentActivity.this, "评论失败");
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ToastUtils.showShort(CommentActivity.this, "评论失败:" + error.getMessage());
                            }
                        });

                        MyApplication.addToRequestQueue(request);
                    }
                });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
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
