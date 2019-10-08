package com.example.autoirrigation.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static Date parseDateString(String dateString, String dateFormat) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, new Locale("ZN"));
        Date date = sdf.parse(dateString);
        return date;
    }
}
