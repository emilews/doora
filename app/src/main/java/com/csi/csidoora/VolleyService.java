package com.csi.csidoora;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VolleyService {
    private boolean LoggedIn = false;
    private static final VolleyService ourInstance = new VolleyService();

    public static VolleyService getInstance() {
        return ourInstance;
    }

    private VolleyService() {
    }

    public void LogIn(Context ctx, final String email, final String pass) throws IOException {
        final CONSTANTS constants = new CONSTANTS(ctx);
        RequestQueue r = Volley.newRequestQueue(ctx);
        StringRequest sr = new StringRequest(Request.Method.POST, constants.getLOGIN_URL(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("Success")){
                            Success();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }){

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", pass);
                params.put("device", constants.getDEVICE_CODE());
                return params;
            }
        };
        r.add(sr);
        r.start();
    }
    private void Success(){
        LoggedIn = true;
    }
    public String getCode(Context ctx) throws IOException {
        final CONSTANTS constants = new CONSTANTS(ctx);
        RequestQueue r = Volley.newRequestQueue(ctx);
        StringRequest sr = new StringRequest(Request.Method.POST, constants.getCODE_URL(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("code")){
                            setCode(response, constants);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }){

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("device", constants.getDEVICE_CODE());
                return params;
            }
        };
        r.add(sr);
        r.start();
        return constants.getCODE();
    }
    public void setCode(String s, CONSTANTS c){
        String[] a = s.split(" ");
        c.setCode(a[1].substring(1, 5));
    }
    public boolean getLoggedIn(){
        return LoggedIn;
    }
}
