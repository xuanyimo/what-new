package cn.dubby.what.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dubby on 16/5/25.
 */
public class TimeFormat {

    public static String format(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        StringBuffer sb = new StringBuffer();
        sb.append(calendar.get(Calendar.YEAR));
        sb.append("-");
        sb.append(calendar.get(Calendar.MONTH));
        sb.append("-");
        sb.append(calendar.get(Calendar.DAY_OF_MONTH));
        sb.append(" ");

        sb.append(calendar.get(Calendar.HOUR_OF_DAY));
        sb.append(":");
        sb.append(calendar.get(Calendar.MINUTE));
        return sb.toString();
    }

}
