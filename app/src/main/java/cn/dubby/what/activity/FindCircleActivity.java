package cn.dubby.what.activity;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import cn.dubby.what.R;
import cn.dubby.what.application.MyApplication;
import cn.dubby.what.component.DividerGridItemDecoration;
import cn.dubby.what.component.MainContentRecyclerAdapter;
import cn.dubby.what.constant.SharedConstant;
import cn.dubby.what.domain.circle.Circle;
import cn.dubby.what.utils.SharedPreferencesUtils;
import cn.dubby.what.utils.ToastUtils;

public class FindCircleActivity extends AppCompatActivity {

    private Button searchBtn;
    private EditText keyEd;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<Circle> data;
    private MainContentRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initView();
        loadData();
    }

    private void initView() {
        setContentView(R.layout.activity_find_circle);
        setTitle("圈子广场");

        searchBtn = (Button) findViewById(R.id.searchBtn);
        keyEd = (EditText) findViewById(R.id.keyEd);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置分割线
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));

    }

    private void loadData() {
        data = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            Circle circle = new Circle();
            circle.logo = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2102217541,3129842170&fm=58";
            circle.description = "Java是一个纯粹的面向对象的程序设计语言，它继承了 C++语言面向对象技术的核心。Java舍弃了C语言中容易引起错误的指针（以引用取代）、运算符重载（operator overloading）、多重继承（以接口取代）等特性，增加了垃圾回收器功能用于回收不再被引用的对象所占据的内存空间，使得程序员不用再为内存管理而担忧。在 Java 1.5 版本中，Java 又引入了泛型编程（Generic Programming）、类型安全的枚举、不定长参数和自动装/拆箱等语言特性。";
            data.add(circle);
        }
        adapter = new MainContentRecyclerAdapter(this, data);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToastUtils.showShort(FindCircleActivity.this, "refresh...");
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

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setRefreshing(true);
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

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.animator.zoomin,R.animator.zoomout);
    }


}
