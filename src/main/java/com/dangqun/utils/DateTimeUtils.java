package com.dangqun.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wcy
 */
public class DateTimeUtils {
    public static String getTime(){
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return sf.format(date);
    }
}
