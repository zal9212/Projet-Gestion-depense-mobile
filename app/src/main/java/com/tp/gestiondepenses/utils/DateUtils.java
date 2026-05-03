package com.tp.gestiondepenses.utils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String getCurrentMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getCurrentYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getCurrentMonthName() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.FRANCE);
        String name = sdf.format(new Date());
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static String formatCurrency(double amount) {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        return format.format(amount) + " FCFA";
    }
}
