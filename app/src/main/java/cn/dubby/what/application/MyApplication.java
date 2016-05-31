package cn.dubby.what.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;

import com.activeandroid.ActiveAndroid;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import cn.dubby.what.exception.CrashHandler;
import cn.dubby.what.service.LocationService;
import cn.dubby.what.volleyx.cache.ImageCacheUtil;
import cn.dubby.what.volleyx.cache.LruBitmapCache;

/**
 * Created by dubby on 16/4/30.
 */
public class MyApplication extends Application {
    private static ImageLoader imageLoader = null;
    private static RequestQueue requestQueue = null;

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();


        requestQueue = Volley.newRequestQueue(getApplicationContext());
//        imageLoader = new ImageLoader(requestQueue, new LruBitmapCache());
        imageLoader = new ImageLoader(requestQueue, new ImageCacheUtil());

        //定位服务没有开启
        startLocationService();

        // 异常处理，不需要处理时注释掉这两句即可！
        CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
        crashHandler.init(getApplicationContext());

        //orm框架ActiveAndroid
        ActiveAndroid.initialize(this);


    }

    private void startLocationService() {
        startService(new Intent(getApplicationContext(), LocationService.class));
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

    public static void addToRequestQueue(Request request) {
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    public static DisplayMetrics getDisplayMetrics() {
        return context.getResources().getDisplayMetrics();
    }
}
