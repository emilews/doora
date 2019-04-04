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

    }
}
