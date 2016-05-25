package cn.dubby.what.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import cn.dubby.what.R;
import cn.dubby.what.application.MyApplication;
import cn.dubby.what.constant.URLConstant;
import cn.dubby.what.domain.circle.Circle;
import cn.dubby.what.dto.Result;
import cn.dubby.what.utils.MessagesContainer;
import cn.dubby.what.utils.ToastUtils;
import cn.dubby.what.volleyx.MyRequest;

public class FocusActivity extends AppCompatActivity {

    private NetworkImageView imageView;
    private TextView description;
    private Button focusBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
        reload();
    }

    private void initView() {
        imageView = (NetworkImageView) findViewById(R.id.logoImage);
        description = (TextView) findViewById(R.id.description);
        focusBtn = (Button) findViewById(R.id.focusBtn);
        focusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                map.put("token", MessagesContainer.TOKEN);
                map.put("cid", MessagesContainer.CURRENT_CIRCLE_ID + "");
                MyRequest request = new MyRequest(URLConstant.CIRCLE.FOCUS, map,
                        new Response.Listener<Result>() {
                            @Override
                            public void onResponse(Result response) {
                                if (response.getSuccess()) {
                                    ToastUtils.showShort(MyApplication.context, "关注成功");
                                    finish();
                                    overridePendingTransition(R.animator.zoomin, R.animator.zoomout);
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
        });
    }

    private void reload() {
        Map map = new HashMap();
        map.put("cid", MessagesContainer.CURRENT_CIRCLE_ID + "");
        MyRequest request = new MyRequest(URLConstant.CIRCLE.GET, map,
                new Response.Listener<Result>() {
                    @Override
                    public void onResponse(Result response) {
                        if (response.getSuccess()) {
                            JSONObject jsonObject = (JSONObject) response.getData();
                            try {
                                Circle circle = new Circle(jsonObject);
                                imageView.setImageUrl(circle.logo, MyApplication.getImageLoader());
                                description.setText(circle.description);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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


}
