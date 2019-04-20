package com.csi.csidoora;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class BackgroundService extends IntentService {
    //---------------------------------------------------------------------
    //This is the CONSTANTS part, made like this because the Java GC collects
    //the CONSTANTS object and we can't access it anymore from the service
    //---------------------------------------------------------------------

    //URL
    private static final String LOGIN_URL_FIRST_PASS = "http://192.168.1.72:8000/csi/first/";
    private static final String LOGIN_URL_SECOND_PASS = "http://192.168.1.72:8000/csi/second/";
    private static final String CODE_URL = "http://192.168.1.72:8000/csi/get_code/";
    //FILES
    private static final String DEVICE_FILE_NAME = "device.csi";
    private static final String CODE_FILE_NAME = "code.csi";
    private static String DEVICE_CODE = "";
    private static String CODE = "";
    private static final int DEVICE_CODE_LEN = 36;
    private static final int CODE_LEN = 4;
    private static boolean userExists = false;
    private static String userName = "";


    public static String getLOGIN_URL_FIRST_PASS(){
        return LOGIN_URL_FIRST_PASS;
    }
    public static String getLOGIN_URL_SECOND_PASS(){
        return LOGIN_URL_SECOND_PASS;
    }
    public static String getCODE_URL(){
        return CODE_URL;
    }
    public static String getDEVICE_FILE_NAME(){
        return DEVICE_FILE_NAME;
    }
    private static String createDeviceCode(){
        return UUID.randomUUID().toString();
    }
    public static String getDEVICE_CODE(){
        return DEVICE_CODE;
    }
    public static String getCODE_FILE_NAME(){ return CODE_FILE_NAME; }
    public static String getCODE(){ return CODE; }
    public static boolean getLoggedIn(){
        return LoggedIn;
    }


    private static boolean LoggedIn;

    public BackgroundService() {
        super("BackgroundService");
        LoggedIn = false;
    }

    public static boolean getUserExists() {
        return userExists;
    }

    public static String getUserName() {
        return userName;
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sessionIDCode(BackgroundService.this);
                doorCode(BackgroundService.this);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(BackgroundService.this,
                        "csi")
                        .setSmallIcon(R.drawable.logo_notif_white)
                        .setContentTitle("Door Code")
                        .setContentText(getCODE())
                        .setOngoing(true)
                        .setPriority(NotificationCompat.PRIORITY_MAX);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(BackgroundService.this);
                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(1, builder.build());
            }
        },0, 1000);
    }

    //Getting session ID code from memory (aka the device.csi file)
    //If it finds the file, then proceeds to get the code which is 36
    //characters long
    //If file is not found (catch statement) then we create the file and
    //create a new code using UUID random method
    public static void sessionIDCode(Context ctx){
        //To get the device session code, not the actual code
        try{
            FileInputStream fi = ctx.openFileInput(getDEVICE_FILE_NAME());
            System.out.println("Found session code file");
            int n;
            StringBuffer sb = new StringBuffer();
            byte[] data = new byte[DEVICE_CODE_LEN];
            while ((n = fi.read(data)) != -1) {
                sb.append(new String(data, 0, n));
            }
            DEVICE_CODE = sb.toString();
            System.out.println(DEVICE_CODE);
        }catch(FileNotFoundException f){
            try{
                FileOutputStream fo = ctx.openFileOutput(getDEVICE_FILE_NAME(), Context.MODE_PRIVATE);
                fo.write(createDeviceCode().getBytes());
                fo.flush();
                fo.close();
            }catch(FileNotFoundException fe){

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void doorCode(Context ctx) {
        getCodeFromWeb(ctx);
        //To get the actual code if saved
        try{
            FileInputStream fi = ctx.openFileInput(getCODE_FILE_NAME());
            System.out.println("Found code file");
            int n;
            StringBuffer sb2 = new StringBuffer();
            byte[] data = new byte[CODE_LEN];
            while ((n = fi.read(data)) != -1) {
                sb2.append(new String(data, 0, n));
            }
            CODE = sb2.toString();
        }catch(FileNotFoundException f){
            try{
                FileOutputStream fo = ctx.openFileOutput(getCODE_FILE_NAME(), Context.MODE_PRIVATE);
                fo.write(BackgroundService.getCODE().getBytes());
                fo.flush();
                fo.close();
            }catch(FileNotFoundException fe){

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Sets the code in-memory then writes it to the code.csi file
    public static void setCode(Context ctx, String s){
        CODE = s;
        try{
            FileOutputStream fo = ctx.openFileOutput(getCODE_FILE_NAME(), Context.MODE_PRIVATE);
            fo.write(CODE.getBytes());
            fo.flush();
            fo.close();

        }catch(FileNotFoundException fe){
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //If the code.csi file is found, then the user was logged in before, therefore, we can
    //just go to the home activity without opening MainActivity
    public static void wasLogged(Context ctx){
        try {
            FileInputStream fi = ctx.openFileInput(getCODE_FILE_NAME());
            System.out.println("Found code file");
            LoggedIn = true;
        }catch(Exception e){
            LoggedIn = false;
        }
    }
    //We ask the server if the email belongs to a user in the DB, if it does, then we get the
    //name of the user and show it on the login screen
    public static void LogInFirstPass(final Context ctx, final String email) {
        RequestQueue r = Volley.newRequestQueue(ctx);
        StringRequest sr = new StringRequest(Request.Method.POST, BackgroundService.getLOGIN_URL_FIRST_PASS(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("Name")){
                            userExists = true;
                            SuccessFirstPass(ctx, response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }){

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                return params;
            }
        };
        r.add(sr);
        r.start();
    }
    //Now that we know the user exists, then we ask for the password of that account
    //If it is the wrong password, then we use a Toast Notification to let the user know
    //We send the email, the password and the session ID code to the server
    public static void LogInSecondPass(final Context ctx, final String email, final String pswd) {
        RequestQueue r = Volley.newRequestQueue(ctx);
        StringRequest sr = new StringRequest(Request.Method.POST, BackgroundService.getLOGIN_URL_SECOND_PASS(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        if(response.contains("Success.")){
                            SuccessSecondPass();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }){

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", pswd);
                params.put("device", getDEVICE_CODE());
                return params;
            }
        };
        r.add(sr);
        r.start();
    }
    //If the password is correct, then we set the flag as true
    private static void SuccessSecondPass(){
        LoggedIn = true;
    }
    //We separate the name from the JSON response and give it back to show on the UI
    private static void SuccessFirstPass(Context ctx, String name) {
        String[] a = name.split(":");
        char[] e = a[1].toCharArray();
        String finalName = "";
        StringBuilder sb = new StringBuilder(finalName);
        for(int i = 0; i < e.length; i++){
            if(e[i] == '"') continue;
            if (e[i] == '}') break;
            sb.append(e[i]);
        }
        userName = sb.toString();
    }

    public void getCodeFromWeb(Context ctx) {
        RequestQueue r = Volley.newRequestQueue(BackgroundService.this);
        StringRequest sr = new StringRequest(Request.Method.POST, getCODE_URL(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("code")) {
                            String[] a = response.split(" ");
                            setCode(BackgroundService.this, a[1].substring(1, 5));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("device", getDEVICE_CODE());
                return params;
            }
        };
        r.add(sr);
        r.start();
    }
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

}

