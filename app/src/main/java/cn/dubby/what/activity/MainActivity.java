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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.dubby.what.R;
import cn.dubby.what.application.MyApplication;
import cn.dubby.what.component.CircleNetworkImageView;
import cn.dubby.what.component.DividerGridItemDecoration;
import cn.dubby.what.component.DividerItemDecoration;
import cn.dubby.what.component.MainContentRecyclerAdapter;
import cn.dubby.what.constant.SharedConstant;
import cn.dubby.what.constant.URLConstant;
import cn.dubby.what.domain.circle.Circle;
import cn.dubby.what.dto.FormImage;
import cn.dubby.what.utils.MessagesContainer;
import cn.dubby.what.utils.SharedPreferencesUtils;
import cn.dubby.what.utils.StringUtils;
import cn.dubby.what.utils.ToastUtils;
import cn.dubby.what.volleyx.PostUploadRequest;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int REQUEST_CODE_FOR_PICTURE_SELECT = 11111;
    private CircleNetworkImageView imageView;
    private TextView loginNameTv;
    private NavigationView navigationView;
    private FloatingActionButton fab;
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


        initView();
        loadData();

        //检查是否已登录
        checkLoginStatus();
    }

    private void loadData() {
        loginNameTv.setText(MessagesContainer.getEmail());
        imageView.setImageUrl("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1464019385&di=91f148fe63af2efecabdc2c8ea1ca811&src=http://p7.qhimg.com/t01cd54e217033aab22.jpg", MyApplication.getImageLoader());

        data = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            Circle circle = new Circle();
            circle.logo = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2102217541,3129842170&fm=58";
            circle.description = "Java是一个纯粹的面向对象的程序设计语言，它继承了 C++语言面向对象技术的核心。Java舍弃了C语言中容易引起错误的指针（以引用取代）、运算符重载（operator overloading）、多重继承（以接口取代）等特性，增加了垃圾回收器功能用于回收不再被引用的对象所占据的内存空间，使得程序员不用再为内存管理而担忧。在 Java 1.5 版本中，Java 又引入了泛型编程（Generic Programming）、类型安全的枚举、不定长参数和自动装/拆箱等语言特性。";
            data.add(circle);
        }
        adapter = new MainContentRecyclerAdapter(this, data);
        adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v.findViewById(R.id.description);
                ToastUtils.showShort(MainActivity.this, tv.getText());
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        //初始化视图
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundColor(Color.BLUE);
//        fab.setRippleColor();
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PostThemeActivity.class));
                overridePendingTransition(R.animator.zoomin, R.animator.zoomout);

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置分割线
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToastUtils.showShort(MainActivity.this, "refresh...");
                new AsyncTask<Void, Void, Integer>() {
                    @Override
                    protected Integer doInBackground(Void... params) {
                        int position = data.size();
                        try {
                            Thread.sleep(1000 * 2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < 1; ++i) {
                            Circle circle = new Circle();
                            circle.logo = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2102217541,3129842170&fm=58";

                            String location = (String) SharedPreferencesUtils.getParam(MyApplication.context, SharedConstant.LOCATION, "暂时无法获得位置信息");
                            circle.description = location;
                            data.add(circle);
                        }
                        return position;
                    }

                    @Override
                    protected void onPostExecute(Integer position) {
                        adapter.notifyItemInserted(position);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }.execute(null, null, null);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN);
    }

    private void checkLoginStatus() {
        //判断是否登录
        if (MessagesContainer.TOKEN == null) {
            String token = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "token", "");
            if (StringUtils.isEmpty(token)) {
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                MessagesContainer.TOKEN = token;
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
            case R.id.exit:
                finish();
                System.exit(0);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_FOR_PICTURE_SELECT:
                if (resultCode == -1) {
                    Uri uri = data.getData();
                    Log.e("uri", uri.toString());
                    ContentResolver cr = getContentResolver();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        imageView.setImageBitmap(bitmap);

                        final FormImage image = new FormImage(bitmap);
                        List<FormImage> list = new ArrayList<>();
                        list.add(image);
                        final PostUploadRequest request = new PostUploadRequest(URLConstant.FILE_UPLOAD, list, new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                if (response != null) {
                                    //todo 从服务器上获取图片的URL地址,并保存进用户信息
//                                    String[] rs = response.toString().split("/");
//                                    String fileUrl = "http://192.168.56.1:8080/pictures/" + rs[rs.length - 1];
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), SharedConstant.USER_PICTURE, fileUrl);
//                                    Log.i("volley", fileUrl);
//                                    imageView.setImageUrl(fileUrl, MyApplication.getImageLoader());
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
