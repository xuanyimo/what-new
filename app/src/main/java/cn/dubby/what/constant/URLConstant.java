package cn.dubby.what.constant;

/**
 * Created by dubby on 16/5/11.
 */
public class URLConstant {

    //BASE URL
    private static String BASE = "http://115.28.168.116/what/";

    //User相关
    public static class USER {
        public static String POST_LOGIN = BASE + "user/login.do";
        public static String POST_REGISTER = BASE + "user/register.do";
    }


    //Theme相关
    public static class THEME {
        //根据circleId查出最新的20条Theme
        public static String FOCUS_LIST = BASE + "theme/list.do";
        public static String ADD = BASE + "theme/add.do";
        public static String RECOMMEND_LIST = BASE + "theme/recommend.do";
    }


    //Circle相关
    public static class CIRCLE {
        public static String FIND_LIST = BASE + "circle/find/list.do";
        public static String MY_LIST = BASE + "circle/my/list.do";
    }

    //文件上传相关
    public static String FILE_UPLOAD = BASE + "test/file";

    //
}
