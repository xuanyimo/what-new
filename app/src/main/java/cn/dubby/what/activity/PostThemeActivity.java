package cn.dubby.what.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dubby.what.R;
import cn.dubby.what.application.MyApplication;
import cn.dubby.what.constant.URLConstant;
import cn.dubby.what.domain.circle.Circle;
import cn.dubby.what.dto.Result;
import cn.dubby.what.utils.MessagesContainer;
import cn.dubby.what.utils.SharedPreferencesUtils;
import cn.dubby.what.utils.StringUtils;
import cn.dubby.what.utils.ToastUtils;
import cn.dubby.what.volleyx.MyRequest;

public class PostThemeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private List<String> circleNameList;
    private List<Long> circleIdList;

    private Spinner spinner;

    private ArrayAdapter<String> adapter;

    private EditText contentEdit;

    private int circleIndex = 0;

    private CheckBox sendLocationCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_theme);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        circleNameList = new ArrayList<>();
        circleIdList = new ArrayList<>();

        initData();

        contentEdit = (EditText) findViewById(R.id.contentEdit);

        spinner = (Spinner) findViewById(R.id.circleList);
        //适配器
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, circleNameList);
        //设置样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        //提交按钮
        findViewById(R.id.postBtn).setOnClickListener(this);

        sendLocationCheckBox = (CheckBox) findViewById(R.id.sendLocationCheckBox);
    }

    private void initData() {
        Map map = new HashMap();
        map.put("token", MessagesContainer.TOKEN);
        MyRequest request = new MyRequest(URLConstant.CIRCLE.MY_LIST, map,
                new Response.Listener<Result>() {
                    @Override
                    public void onResponse(Result response) {
                        if (response.getErrorCode() == 0) {
                            circleNameList.clear();
                            circleIdList.clear();
                            try {

                                JSONArray array = new JSONArray(response.getData().toString());
                                for (int i = 0; i < array.length(); ++i) {

                                    JSONObject jsonObject = array.getJSONObject(i);
                                    Circle circle = new Circle(jsonObject);

                                    Map map = new HashMap();
                                    map.put("id", circle.serverId);
                                    map.put("logo", circle.logo);
                                    map.put("description", circle.description);
                                    map.put("focusNum", circle.focusNum);
                                    map.put("name", circle.name);

                                    circleNameList.add(circle.name);
                                    circleIdList.add(circle.serverId);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        MyApplication.getRequestQueue().add(request);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.postBtn:
                postTheme();
                break;
        }
    }

    private void postTheme() {
        String content = contentEdit.getText().toString();
        if (StringUtils.isEmpty(content)) {
            ToastUtils.showShort(getApplicationContext(), "请填写内容");
            return;
        }
        Map map = new HashMap();
        map.put("token", MessagesContainer.TOKEN);
        map.put("content", content);
        map.put("cid", circleIdList.get(circleIndex) + "");
        String location = SharedPreferencesUtils.getParam(getApplicationContext(), "location", "").toString();
        if (sendLocationCheckBox.isChecked())
            map.put("location", location);

        Log.i("post", map.toString());
        MyRequest request = new MyRequest(URLConstant.THEME.ADD, map,
                new Response.Listener<Result>() {
                    @Override
                    public void onResponse(Result response) {
                        if (response.getErrorCode() == 0) {
                            ToastUtils.showShort(PostThemeActivity.this, "发送成功");
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        MyApplication.getRequestQueue().add(request);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //nothing
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        circleIndex = position;
        Log.i("post", circleIndex + "");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.animator.zoomin, R.animator.zoomout);
    }
}
