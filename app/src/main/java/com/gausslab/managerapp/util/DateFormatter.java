package com.gausslab.managerapp.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormatter
{
    public static String formatTimestampToDate(long timestamp)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        return formatter.format(timestamp);
    }
}