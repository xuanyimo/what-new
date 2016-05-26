package cn.dubby.what.activity;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dubby.what.R;
import cn.dubby.what.application.MyApplication;
import cn.dubby.what.component.CircleNetworkImageView;
import cn.dubby.what.component.DividerGridItemDecoration;
import cn.dubby.what.component.DividerItemDecoration;
import cn.dubby.what.component.MainContentRecyclerAdapter;
import cn.dubby.what.constant.ErrorCode;
import cn.dubby.what.constant.SharedConstant;
import cn.dubby.what.constant.URLConstant;
import cn.dubby.what.domain.circle.Circle;
import cn.dubby.what.domain.user.User;
import cn.dubby.what.dto.FormImage;
import cn.dubby.what.dto.Result;
import cn.dubby.what.utils.MessagesContainer;
import cn.dubby.what.utils.SharedPreferencesUtils;
import cn.dubby.what.utils.StringUtils;
import cn.dubby.what.utils.ToastUtils;
import cn.dubby.what.utils.TokenChecker;
import cn.dubby.what.volleyx.MyRequest;
import cn.dubby.what.volleyx.PostUploadRequest;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int REQUEST_CODE_FOR_PICTURE_SELECT = 11111;
    private CircleNetworkImageView imageView;
    private TextView loginNameTv;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private RecyclerView recyclerView;
    private MainContentRecyclerAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private List<Circle> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //准备界面
        super.onCreate(savedInstanceState);

        //检查是否已登录
        checkLoginStatus();

        initView();
        loadData();


    }

    private void reload() {
        if (StringUtils.isEmpty(MessagesContainer.TOKEN))
            return;
        loginNameTv.setText(MessagesContainer.CURRENT_USER.email);
        imageView.setImageUrl(MessagesContainer.CURRENT_USER.headImg, MyApplication.getImageLoader());
        Log.i("token", MessagesContainer.TOKEN);
        Map map = new HashMap();
        map.put("token", MessagesContainer.TOKEN + "");

        MyRequest request = new MyRequest(URLConstant.CIRCLE.MY_LIST, map, new Response.Listener<Result>() {
            @Override
            public void onResponse(Result response) {
                if (TokenChecker.check(response)) {
                    ToastUtils.showLong(MyApplication.context, "登陆会话已过期,请重新登陆");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
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
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.addToRequestQueue(request);
    }

    private void loadData() {
        reload();
    }

    private void initView() {
        //初始化视图
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        imageView = (CircleNetworkImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        loginNameTv = (TextView) navigationView.getHeaderView(0).findViewById(R.id.loginNameTv);

        //绑定监听
        imageView.setOnClickListener(this);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置分割线
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN);

        data = new ArrayList<>();
        adapter = new MainContentRecyclerAdapter(this, data);
        adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView idTv = (TextView) ((View) (v.getParent())).findViewById(R.id.idTv);

                MessagesContainer.CURRENT_CIRCLE_ID = Long.parseLong(idTv.getText().toString());
                startActivity(new Intent(MainActivity.this, ThemeListActivity.class));
                overridePendingTransition(R.animator.zoomin, R.animator.zoomout);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void checkLoginStatus() {
        //判断是否登录
        if (MessagesContainer.TOKEN == null) {
            String token = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "token", "");
            if (StringUtils.isEmpty(token)) {
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                MessagesContainer.TOKEN = token;
                long id = (long) SharedPreferencesUtils.getParam(getApplicationContext(), SharedConstant.USER_ID, 0L);
                String email = (String) SharedPreferencesUtils.getParam(getApplicationContext(), SharedConstant.EMAIL, "");
                String image = (String) SharedPreferencesUtils.getParam(getApplicationContext(), SharedConstant.USER_PICTURE, "");
                if (id != 0 && !StringUtils.isEmpty(email)) {
                    User user = new User();
                    user.serverId = id;
                    user.email = email;
                    user.headImg = image;
                    MessagesContainer.CURRENT_USER = user;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.find_circle:
                startActivity(new Intent(this, FindCircleActivity.class));
                overridePendingTransition(R.animator.zoomin, R.animator.zoomout);
                break;
            case R.id.collection:
                startActivity(new Intent(this, MyCollectionActivity.class));
                overridePendingTransition(R.animator.zoomin, R.animator.zoomout);
                break;
            case R.id.recommend:
                startActivity(new Intent(this, RecommendActivity.class));
                overridePendingTransition(R.animator.zoomin, R.animator.zoomout);
                break;
            case R.id.exit:
                MessagesContainer.CURRENT_USER = null;
                MessagesContainer.TOKEN = null;
                MessagesContainer.CURRENT_USER_IMAGE = null;

                SharedPreferencesUtils.setParam(MyApplication.context, "token", "");
                SharedPreferencesUtils.setParam(MyApplication.context, SharedConstant.USER_PICTURE, "");
                SharedPreferencesUtils.setParam(MyApplication.context, SharedConstant.USER_ID, 0l);
                SharedPreferencesUtils.setParam(MyApplication.context, SharedConstant.USER, new User());
                checkLoginStatus();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView:
                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, REQUEST_CODE_FOR_PICTURE_SELECT);
                break;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_FOR_PICTURE_SELECT:
                if (resultCode == -1) {
                    Uri uri = data.getData();
                    Log.e("uri", uri.toString());
                    ContentResolver cr = getContentResolver();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//                        imageView.setImageBitmap(bitmap);

                        final FormImage image = new FormImage(bitmap);
                        List<FormImage> list = new ArrayList<>();
                        list.add(image);
                        final PostUploadRequest request = new PostUploadRequest(URLConstant.FILE_UPLOAD, list, new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                if (response != null) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.toString());
                                        Result result = new Result(jsonObject);

                                        if (result.getSuccess()) {
                                            String fileURL = result.getData().toString();
                                            if (!fileURL.startsWith("http://"))
                                                fileURL = "http://" + fileURL;
                                            Log.i("file", fileURL);
                                            SharedPreferencesUtils.setParam(MyApplication.context, SharedConstant.USER_PICTURE, fileURL);
                                            MessagesContainer.CURRENT_USER_IMAGE = fileURL;
                                            imageView.setImageUrl(fileURL, MyApplication.getImageLoader());

                                            //保存用户信息到服务器
                                            Map map = new HashMap();
                                            map.put("token", MessagesContainer.TOKEN);
                                            map.put("headImg", fileURL);
                                            MyRequest request = new MyRequest(URLConstant.USER.UPDATE, map,
                                                    new Response.Listener<Result>() {
                                                        @Override
                                                        public void onResponse(Result response) {
                                                            if (response.getErrorCode() == 0) {
                                                                ToastUtils.showLong(MyApplication.context, "头像上传成功");
                                                            } else {
                                                                ToastUtils.showLong(MyApplication.context, "头像上传失败");
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            ToastUtils.showLong(MyApplication.context, "头像上传失败:" + error.getMessage());
                                                        }
                                                    });

                                            MyApplication.getRequestQueue().add(request);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

                        MyApplication.getRequestQueue().add(request);

                    } catch (FileNotFoundException e) {
                        Log.e("Exception", e.getMessage(), e);
                    }
                }
                break;
            case IntentIntegrator.REQUEST_CODE:
                if (data.hasExtra(Intents.Scan.RESULT) && data.hasExtra(Intents.Scan.RESULT_FORMAT)) {
                    Log.i("RESULT", data.getStringExtra(Intents.Scan.RESULT));
                    Log.i("RESULT_FORMAT", data.getStringExtra(Intents.Scan.RESULT_FORMAT));
                    String id = data.getStringExtra(Intents.Scan.RESULT);
                }
                break;
        }
    }

}
