package com.example.ultrahang;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "appointment_channel";
    private final int NOTIFICATION_ID = 10;
    private NotificationManager mManager;
    private Context context;
    public NotificationHelper(Context context) {
        this.context = context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }

    private void createChannel() {
        if (mManager == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationChannel channel = new NotificationChannel
                (CHANNEL_ID, "Appointment Notification", NotificationManager.IMPORTANCE_HIGH);

        channel.enableLights(true);
        channel.setLightColor(Color.BLUE);
        channel.enableVibration(true);
        channel.setDescription("5D ultrahang értesítések");
        mManager.createNotificationChannel(channel);
    }

    public void send(String message) {
        if (mManager == null)
            return;
        Intent intent = new Intent(context, AppointmentActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Időpontfoglalás")
                .setContentText(message)
                .setContentIntent(pendingIntent);

        mManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancel() {
        mManager.cancel(NOTIFICATION_ID);
    }

}
