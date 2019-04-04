package com.csi.csidoora;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;

public class BackgroundService extends IntentService {
    ;

    public BackgroundService() {
        super("BackgroundService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        Notification n  = new Notification.Builder(this)
                .setContentTitle("Whip And Weep")
                .setContentText("Whip is On!")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setOngoing(true)
                .build();
        notificationManager.notify(34, n);
    }
}
