package cn.dubby.what.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.dubby.what.constant.SharedConstant;
import cn.dubby.what.utils.MessagesContainer;
import cn.dubby.what.utils.SharedPreferencesUtils;
import cn.dubby.what.utils.StringUtils;


public class LocationService extends Service implements AMapLocationListener {

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    public void onCreate() {
        super.onCreate();
        initAMapLocation();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                double latitude = amapLocation.getLatitude();//获取纬度
//                double longitude = amapLocation.getLongitude();//获取经度
//                amapLocation.getAccuracy();//获取精度信息
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(amapLocation.getTime());
//                String dateStr = df.format(date);//定位时间
                String address = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                String country = amapLocation.getCountry();//国家信息
//                String province = amapLocation.getProvince();//省信息
//                String city = amapLocation.getCity();//城市信息
//                String district = amapLocation.getDistrict();//城区信息
//                String street = amapLocation.getStreet();//街道信息
//                String streetNum = amapLocation.getStreetNum();//街道门牌号信息
//                String cityCode = amapLocation.getCityCode();//城市编码
//                String adCode = amapLocation.getAdCode();//地区编码

                Log.i("Location", address);


                MessagesContainer.LOCATION = address;
                if (!StringUtils.isEmpty(address)) {
                    SharedPreferencesUtils.setParam(getApplicationContext(), SharedConstant.LOCATION, address.toString());
                    mLocationClient.stopAssistantLocation();
                    mLocationClient.stopLocation();
                    stopSelf();
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("LocationService", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }

    }

    private void initAMapLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
}
