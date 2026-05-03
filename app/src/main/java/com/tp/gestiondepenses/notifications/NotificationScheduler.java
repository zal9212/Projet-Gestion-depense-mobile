package com.tp.gestiondepenses.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

public class NotificationScheduler {

    // ---------- Public entry point: schedule everything from prefs ----------

    public static void scheduleAll(Context context, SharedPreferences prefs) {
        if (prefs.getBoolean("notif_daily_enabled", false)) {
            scheduleDaily(context, prefs);
        } else {
            cancelAlarm(context, NotificationReceiver.ACTION_DAILY, 101);
        }

        if (prefs.getBoolean("notif_monthly_enabled", false)) {
            scheduleMonthly(context, prefs);
        } else {
            cancelAlarm(context, NotificationReceiver.ACTION_MONTHLY, 102);
        }

        if (prefs.getBoolean("notif_yearly_enabled", false)) {
            scheduleYearly(context, prefs);
        } else {
            cancelAlarm(context, NotificationReceiver.ACTION_YEARLY, 103);
        }
    }

    // ---------- Daily ----------

    public static void scheduleDaily(Context context, SharedPreferences prefs) {
        int hour   = prefs.getInt("notif_daily_hour", 20);
        int minute = prefs.getInt("notif_daily_minute", 0);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // If time already passed today → tomorrow
        if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        setExactAlarm(context, NotificationReceiver.ACTION_DAILY, 101, cal.getTimeInMillis());
    }

    // ---------- Monthly (1st of month at 08:00) ----------

    public static void scheduleMonthly(Context context, SharedPreferences prefs) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
            cal.add(Calendar.MONTH, 1);
        }

        setExactAlarm(context, NotificationReceiver.ACTION_MONTHLY, 102, cal.getTimeInMillis());
    }

    // ---------- Yearly (Jan 1st at 09:00) ----------

    public static void scheduleYearly(Context context, SharedPreferences prefs) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 9);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
            cal.add(Calendar.YEAR, 1);
        }

        setExactAlarm(context, NotificationReceiver.ACTION_YEARLY, 103, cal.getTimeInMillis());
    }

    // ---------- Helpers ----------

    private static void setExactAlarm(Context context, String action, int requestCode, long triggerAt) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;

        PendingIntent pi = buildPendingIntent(context, action, requestCode);

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                // Android 12+ : vérifier la permission avant d'utiliser setExactAndAllowWhileIdle
                if (am.canScheduleExactAlarms()) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi);
                } else {
                    // Fallback : alarme inexacte (pas de crash, mais moins précise)
                    am.set(AlarmManager.RTC_WAKEUP, triggerAt, pi);
                }
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi);
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, triggerAt, pi);
            }
        } catch (SecurityException e) {
            // Ne jamais crasher l'app à cause des notifications
            e.printStackTrace();
        }
    }

    private static void cancelAlarm(Context context, String action, int requestCode) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;
        am.cancel(buildPendingIntent(context, action, requestCode));
    }

    private static PendingIntent buildPendingIntent(Context context, String action, int requestCode) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }
}
