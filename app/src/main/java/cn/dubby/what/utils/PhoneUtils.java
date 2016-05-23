package cn.dubby.what.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dubby on 16/5/1.
 */
public class PhoneUtils {
    private static PhoneUtils mPhoneUtils;
    private TelephonyManager tm;
    private Context mContext;

    private PhoneUtils(Context context) {
        mContext = context;
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static PhoneUtils getInstance(Context context) {
        if (mPhoneUtils == null) {
            synchronized (PhoneUtils.class) {
                if (mPhoneUtils == null) {
                    mPhoneUtils = new PhoneUtils(context);
                }
            }
        }
        return mPhoneUtils;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * Firmware/OS 版本号
     *
     * @return
     */
    public static String getVersionRelease() {
        return Build.VERSION.RELEASE;
    }

    /**
     * SDK版本号
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getSdkApi() {
        return Build.VERSION.SDK;
    }

    /**
     * 获取手机屏幕分辨率
     *
     * @param activity
     * @return
     */
    public static String DisplayMetrics(Activity activity) {
        android.util.DisplayMetrics dm = new android.util.DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //获得手机的宽度和高度像素单位为px
        return "DisplayMetrics:" + dm.widthPixels + "* " + dm.heightPixels;
    }

    // 获取手机CPU信息
    public static String getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""}; // 1-cpu型号 //2-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (Exception e) {
        }
        return "1-cpu型号:" + cpuInfo[0] + "2-cpu频率:" + cpuInfo[1];
    }


    /**
     * 去掉 +86|86 短信中心号和手机号码
     *
     * @param str
     * @return
     */
    public static String getSub(String str) {
        String subStr = "";
        try {
            if (str == null) {
                return "";
            }
            int len = str.length();
            if (len > 11) {
                subStr = str.substring(len - 11);
            } else {
                subStr = str;
            }
        } catch (Exception ioe) {

        }
        return subStr;
    }

    /**
     * imei
     *
     * @return
     */
    public String getImei() {
        return tm.getDeviceId();
    }

    /**
     * 获取手机号
     *
     * @return
     */
    public String getPhone() {
        return tm.getLine1Number();
    }

    /**
     * IMSI 全称为 International Mobile Subscriber Identity，中文翻译为国际移动用户识别码。
     * 它是在公众陆地移动电话网（PLMN）中用于唯一识别移动用户的一个号码。在GSM网络，这个号码通常被存放在SIM卡中
     *
     * @return
     */
    public String getSubscriberId() {
        if (isSimReady(mContext)) {
            return tm.getSubscriberId();
        }
        return "";
    }


    /**
     * 判断SIM卡是否准备好
     *
     * @param context
     * @return
     */
    public boolean isSimReady(Context context) {
        try {

            int simState = tm.getSimState();
            if (simState == TelephonyManager.SIM_STATE_READY) {
                return true;
            }
        } catch (Exception e) {
            Log.w("PhoneHelper", "021:" + e.toString());
        }
        return false;
    }

    /**
     * 获取当前网络状况
     *
     * @return 如果网络已经连接，并且可用返回true, 否则false
     */
    public static boolean getNetworkState(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo networkinfo = connectivity.getActiveNetworkInfo();
                if (networkinfo != null) {
                    if (networkinfo.isAvailable() && networkinfo.isConnected()) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 判断是否模拟器。如果返回TRUE， 则当前是模拟器,模拟器IMEI是：00000000000000 运营商不让支付
     *
     * @param context
     * @return
     */
    public boolean isEmulator(Context context) {
        try {
            String imei = tm.getDeviceId();
            if (imei != null && imei.equals("000000000000000")) {
                return true;
            }
            return (Build.MODEL.equals("sdk"))
                    || (Build.MODEL.equals("google_sdk"));
        } catch (Exception ioe) {
            Log.w("PhoneHelper", "009:" + ioe.toString());
        }
        return false;
    }

    /**
     * 获取当前APP名称和版本号
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getAppInfo(Context context) {
        context = context.getApplicationContext();
        String applicationName = "";
        String versionName = "";
        String packageName = "";
        int versionCode;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);

            applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
            versionName = packageInfo.versionName;
            packageName = packageInfo.packageName;
            versionCode = packageInfo.versionCode;
            return "applicationName：" + applicationName + " packageName:" + packageName + " versionName:" + versionName + "  versionCode:" + versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前APP名称和版本号
     *
     * @param context
     * @return applicationName  packageName versionName versionCode
     */
    public static Map<String, String> getAppInfoMap(Context context) {
        String applicationName = "";
        String versionName = "";
        String packageName = "";
        int versionCode;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            ApplicationInfo applicationInfo = packageManager
                    .getApplicationInfo(context.getPackageName(), 0);

            applicationName = (String) packageManager
                    .getApplicationLabel(applicationInfo);
            versionName = packageInfo.versionName;
            packageName = packageInfo.packageName;
            versionCode = packageInfo.versionCode;
            Map<String, String> map = new HashMap<String, String>();
            map.put("appName", applicationName);
            map.put("packageName", packageName);
            map.put("versionName", versionName);
            map.put("versionCode", versionCode + "");
            return map;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取MetaDataValue
     *
     * @param name
     * @param def
     * @return
     */
    public String getMetaDataValue(String name, String def) {
        String value = getMetaDataValue(name);
        return (value == null) ? def : value;
    }

    private String getMetaDataValue(String name) {
        Object value = null;
        PackageManager packageManager = mContext.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(name);
            }
        } catch (Exception e) {
        }
        return value.toString();
    }
}
