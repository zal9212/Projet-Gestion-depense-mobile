package com.tp.gestiondepenses.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.ui.activities.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID_DAILY   = "fintrack_daily";
    public static final String CHANNEL_ID_MONTHLY = "fintrack_monthly";
    public static final String CHANNEL_ID_YEARLY  = "fintrack_yearly";

    public static final String ACTION_DAILY   = "com.tp.gestiondepenses.DAILY_NOTIF";
    public static final String ACTION_MONTHLY = "com.tp.gestiondepenses.MONTHLY_NOTIF";
    public static final String ACTION_YEARLY  = "com.tp.gestiondepenses.YEARLY_NOTIF";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) return;

        createChannels(context);

        SharedPreferences prefs = context.getSharedPreferences("FinanceTrackPrefs", Context.MODE_PRIVATE);

        switch (action) {
            case Intent.ACTION_BOOT_COMPLETED:
                // Re-schedule all alarms after device reboot
                NotificationScheduler.scheduleAll(context, prefs);
                break;

            case ACTION_DAILY:
                if (prefs.getBoolean("notif_daily_enabled", false)) {
                    sendNotification(context,
                            CHANNEL_ID_DAILY,
                            1001,
                            "📊 Résumé du jour",
                            "Consultez le bilan de vos dépenses et revenus d'aujourd'hui.");
                    // Re-schedule for next day
                    NotificationScheduler.scheduleDaily(context, prefs);
                }
                break;

            case ACTION_MONTHLY:
                if (prefs.getBoolean("notif_monthly_enabled", false)) {
                    sendNotification(context,
                            CHANNEL_ID_MONTHLY,
                            1002,
                            "📅 Résumé mensuel",
                            "Découvrez le bilan financier du mois écoulé.");
                    NotificationScheduler.scheduleMonthly(context, prefs);
                }
                break;

            case ACTION_YEARLY:
                if (prefs.getBoolean("notif_yearly_enabled", false)) {
                    sendNotification(context,
                            CHANNEL_ID_YEARLY,
                            1003,
                            "🏆 Bilan annuel",
                            "Découvrez votre bilan financier de l'année écoulée.");
                    NotificationScheduler.scheduleYearly(context, prefs);
                }
                break;
        }
    }

    private void sendNotification(Context context, String channelId, int notifId, String title, String body) {
        Intent tapIntent = new Intent(context, MainActivity.class);
        tapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(context, notifId, tapIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_nav_home)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pi)
                .setAutoCancel(true);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) nm.notify(notifId, builder.build());
    }

    private void createChannels(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;
        NotificationManager nm = context.getSystemService(NotificationManager.class);
        if (nm == null) return;

        nm.createNotificationChannel(new NotificationChannel(
                CHANNEL_ID_DAILY, "Résumé journalier", NotificationManager.IMPORTANCE_DEFAULT));
        nm.createNotificationChannel(new NotificationChannel(
                CHANNEL_ID_MONTHLY, "Résumé mensuel", NotificationManager.IMPORTANCE_DEFAULT));
        nm.createNotificationChannel(new NotificationChannel(
                CHANNEL_ID_YEARLY, "Bilan annuel", NotificationManager.IMPORTANCE_DEFAULT));
    }
}
