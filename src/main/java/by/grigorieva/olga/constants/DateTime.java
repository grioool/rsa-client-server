package by.grigorieva.olga.constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
    public static String getTime(){
        return getDate("HH:mm:ss");
    }

    public static String getDate(String format){
        SimpleDateFormat formatted = new SimpleDateFormat(format);
        Date date = new Date(System.currentTimeMillis());
        return formatted.format(date);
    }
}
