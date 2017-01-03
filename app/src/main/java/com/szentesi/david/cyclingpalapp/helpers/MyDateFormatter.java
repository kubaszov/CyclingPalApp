package com.szentesi.david.cyclingpalapp.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// using Java version of SimpleDateFormat as the Android version is only compatible with
// API version 24 and above
public class MyDateFormatter {

    public static String retrieveDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}
