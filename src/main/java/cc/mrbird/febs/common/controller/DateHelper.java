package cc.mrbird.febs.common.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    public static String getDateToString(Date time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String Time = formatter.format(time);
        return Time;
    }


}
