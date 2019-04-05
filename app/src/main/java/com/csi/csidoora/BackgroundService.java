package com.csi.csidoora;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends IntentService {


    public BackgroundService() {
        super("BackgroundService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        final CONSTANTS constants = CONSTANTS.getInstance();
        final VolleyService vs = VolleyService.getInstance();
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                vs.getCode(BackgroundService.this);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(BackgroundService.this,
                        "csi")
                        .setSmallIcon(R.drawable.logo_notif_white)
                        .setContentTitle("Door Code")
                        .setContentText(constants.getCODE())
                        .setOngoing(true)
                        .setPriority(NotificationCompat.PRIORITY_MAX);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(BackgroundService.this);
                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(1, builder.build());
            }
        },0, 10000);
    }
    public void getCode() {
        final CONSTANTS constants = CONSTANTS.getInstance();
        RequestQueue r = Volley.newRequestQueue(BackgroundService.this);
        StringRequest sr = new StringRequest(Request.Method.POST, constants.getCODE_URL(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("code")) {
                            String[] a = response.split(" ");
                            constants.setCode(BackgroundService.this, a[1].substring(1, 5));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }) {

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("device", constants.getDEVICE_CODE());
                return params;
            }
        };
        r.add(sr);
        r.start();
    }
}
