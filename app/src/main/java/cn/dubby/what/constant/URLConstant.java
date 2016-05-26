package cn.dubby.what.constant;

/**
 * Created by dubby on 16/5/11.
 */
public class URLConstant {

    //BASE URL
    private static String BASE = "http://115.28.168.116/what/";
//    private static String BASE = "http://172.18.165.5:8080/what/";

    //User相关
    public static class USER {
        public static String POST_LOGIN = BASE + "user/login.do";
        public static String POST_REGISTER = BASE + "user/register.do";
        public static String UPDATE = BASE + "user/update.do";
        public static String INFO = BASE + "user/info.do";
    }


    //Theme相关
    public static class THEME {
        //根据circleId查出最新的20条Theme
        public static String LIST = BASE + "theme/list.do";
        public static String ADD = BASE + "theme/add.do";
        public static String RECOMMEND_LIST = BASE + "theme/recommend.do";
        public static String COLLECT = BASE + "theme/collect.do";
        public static String COLLECT_LIST = BASE + "theme/list/collection.do";
        public static String CANCEL_COLLECT = BASE + "theme/collect/cancel.do";
    }

    public static class COMMENT {
        public static String LIST = BASE + "post/list.do";
        public static String ADD = BASE + "post/comment.do";
    }


    //Circle相关
    public static class CIRCLE {
        public static String FOCUS = BASE + "circle/focus.do";
        public static String GET = BASE + "circle/get.do";
        public static String FIND_LIST = BASE + "circle/find/list.do";
        public static String MY_LIST = BASE + "circle/my/list.do";
        public static String CANCEL_FOCUS = BASE + "circle/focus/cancel.do";
    }

    //文件上传相关
    public static String FILE_UPLOAD = BASE + "file/img.do";

    //
}
