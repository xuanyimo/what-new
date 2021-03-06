package cn.dubby.what.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dubby.what.R;
import cn.dubby.what.application.MyApplication;
import cn.dubby.what.component.DividerGridItemDecoration;
import cn.dubby.what.component.MainContentRecyclerAdapter;
import cn.dubby.what.constant.SharedConstant;
import cn.dubby.what.constant.URLConstant;
import cn.dubby.what.domain.circle.Circle;
import cn.dubby.what.dto.Result;
import cn.dubby.what.utils.MessagesContainer;
import cn.dubby.what.utils.SharedPreferencesUtils;
import cn.dubby.what.utils.ToastUtils;
import cn.dubby.what.volleyx.MyRequest;

public class FindCircleActivity extends AppCompatActivity {

    private EditText keyEd;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<Circle> data;
    private MainContentRecyclerAdapter adapter;

    private boolean isSearching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        loadData();
    }

    private void initView() {
        setContentView(R.layout.activity_find_circle);
        setTitle("圈子广场");

        keyEd = (EditText) findViewById(R.id.keyEd);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置分割线
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));

        data = new ArrayList<>();
        adapter = new MainContentRecyclerAdapter(this, data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView idTv = (TextView) ((View) (v.getParent())).findViewById(R.id.idTv);

                MessagesContainer.CURRENT_CIRCLE_ID = Long.parseLong(idTv.getText().toString());

                startActivity(new Intent(FindCircleActivity.this, ThemeListActivity.class));
                overridePendingTransition(R.animator.zoomin, R.animator.zoomout);
            }
        });
        adapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextView idTv = (TextView) ((View) (v.getParent())).findViewById(R.id.idTv);

                MessagesContainer.CURRENT_CIRCLE_ID = Long.parseLong(idTv.getText().toString());
                return false;
            }
        });


        this.registerForContextMenu(recyclerView);
//        int total = adapter.getItemCount();
//        for (int i = 0; i < total; ++i) {
//            this.registerForContextMenu(recyclerView.getChildAt(i));
//
//        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN);

        keyEd.addTextChangedListener(textWatcher);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // set context menu title
        menu.setHeaderTitle("部落操作");
        // add context menu item
        menu.add(0, 1, Menu.NONE, "关注");
        menu.add(0, 2, Menu.NONE, "取消关注");
        menu.add(0, 3, Menu.NONE, "举报");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                //关注
                focusCircle();
                break;
            case 2:
                //取消关注
                cancelFocusCircle();
                break;
            case 3:
                //举报
                ToastUtils.showShort(FindCircleActivity.this, "点击了举报");

                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    private void focusCircle() {
        Map map = new HashMap();
        map.put("token", MessagesContainer.TOKEN);
        map.put("cid", MessagesContainer.CURRENT_CIRCLE_ID + "");
        MyRequest request = new MyRequest(URLConstant.CIRCLE.FOCUS, map,
                new Response.Listener<Result>() {
                    @Override
                    public void onResponse(Result response) {
                        if (response.getSuccess()) {
                            ToastUtils.showShort(MyApplication.context, "关注成功");
//                            finish();
//                            overridePendingTransition(R.animator.zoomin, R.animator.zoomout);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        MyApplication.addToRequestQueue(request);
    }

    private void cancelFocusCircle() {
        Map map = new HashMap();
        map.put("token", MessagesContainer.TOKEN);
        map.put("cid", MessagesContainer.CURRENT_CIRCLE_ID + "");
        MyRequest request = new MyRequest(URLConstant.CIRCLE.CANCEL_FOCUS, map,
                new Response.Listener<Result>() {
                    @Override
                    public void onResponse(Result response) {
                        if (response.getSuccess()) {
                            ToastUtils.showShort(MyApplication.context, "取消关注成功");
//                            finish();
//                            overridePendingTransition(R.animator.zoomin, R.animator.zoomout);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        MyApplication.addToRequestQueue(request);
    }


    private void loadData() {
        Map map = new HashMap();
        map.put("k", keyEd.getText().toString());

        MyRequest request = new MyRequest(URLConstant.CIRCLE.FIND_LIST, map, new Response.Listener<Result>() {
            @Override
            public void onResponse(Result response) {
                if (response.getErrorCode() == 0) {
                    data.clear();
                    JSONArray jsonArray = (JSONArray) response.getData();
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Circle circle = new Circle(jsonObject);

                            data.add(circle);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    isSearching = false;
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
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.animator.zoomin, R.animator.zoomout);
    }


    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.i("textWatcher", "beforeTextChanged");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i("textWatcher", "onTextChanged");
            loadData();
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.i("textWatcher", "afterTextChanged");
        }
    };

}
