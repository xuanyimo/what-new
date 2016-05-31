package cn.dubby.what.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

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
import cn.dubby.what.constant.SharedConstant;
import cn.dubby.what.constant.URLConstant;
import cn.dubby.what.domain.user.User;
import cn.dubby.what.dto.FormImage;
import cn.dubby.what.dto.Result;
import cn.dubby.what.utils.MessagesContainer;
import cn.dubby.what.utils.SharedPreferencesUtils;
import cn.dubby.what.utils.ToastUtils;
import cn.dubby.what.volleyx.MyRequest;
import cn.dubby.what.volleyx.PostUploadRequest;

public class UserInfoActivity extends AppCompatActivity {

    private CircleNetworkImageView imageView;
    private EditText email;
    private EditText phone;
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();
        reload();
    }

    private void initView() {
        imageView = (CircleNetworkImageView) findViewById(R.id.imageView);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        btn = (Button) findViewById(R.id.btn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, MainActivity.REQUEST_CODE_FOR_PICTURE_SELECT);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long userId = (long) SharedPreferencesUtils.getParam(MyApplication.context, SharedConstant.USER_ID, 0l);
                final Map parameter4UserInfo = new HashMap();
                parameter4UserInfo.put("token", MessagesContainer.TOKEN);
                parameter4UserInfo.put("uid", userId + "");
                parameter4UserInfo.put("phone", phone.getText().toString());
                MyRequest request1 = new MyRequest(URLConstant.USER.UPDATE, parameter4UserInfo, new Response.Listener<Result>() {
                    @Override
                    public void onResponse(Result response) {
                        if (response.getErrorCode() == 0) {
                            ToastUtils.showShort(MyApplication.context, "修改成功");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.showShort(MyApplication.context, error.getMessage());
                    }
                });

                MyApplication.addToRequestQueue(request1);

            }
        });

    }

    private void reload() {
        long userId = (long) SharedPreferencesUtils.getParam(MyApplication.context, SharedConstant.USER_ID, 0l);
        final Map parameter4UserInfo = new HashMap();
        parameter4UserInfo.put("uid", userId + "");
        MyRequest request1 = new MyRequest(URLConstant.USER.INFO, parameter4UserInfo, new Response.Listener<Result>() {
            @Override
            public void onResponse(Result response) {
                if (response.getErrorCode() == 0) {
                    JSONObject jsonObject = (JSONObject) response.getData();
                    if (jsonObject == null)
                        return;
                    try {
                        User user = new User(jsonObject);
                        imageView.setImageUrl(user.headImg, MyApplication.getImageLoader());
                        email.setText(user.email);
                        phone.setText(user.phone);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showShort(MyApplication.context, error.getMessage());
            }
        });

        MyApplication.addToRequestQueue(request1);
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MainActivity.REQUEST_CODE_FOR_PICTURE_SELECT:
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
                                            MyRequest request1 = new MyRequest(URLConstant.USER.UPDATE, map,
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

                                            MyApplication.getRequestQueue().add(request1);
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
