package com.csi.csidoora;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

public class CONSTANTS {
    //URL
    private final String LOGIN_URL = "http://192.168.1.72:8000/csi/login/";
    private final String CODE_URL = "http://192.168.1.72:8000/csi/get_code/";
    //FILES
    private final String DEVICE_FILE_NAME = "device.csi";
    private String DEVICE_CODE = "";
    private final int DEVICE_CODE_LEN = 36;
    //Context
    Context ctx;
    public CONSTANTS(Context ctx) throws IOException {
        this.ctx = ctx;
        try{
            FileInputStream fi = this.ctx.openFileInput(getDEVICE_FILE_NAME());
            System.out.println("Found file");
            int n;
            StringBuffer sb = new StringBuffer();
            byte[] data = new byte[DEVICE_CODE_LEN];
            while ((n = fi.read(data)) != -1) {
                sb.append(new String(data, 0, n));
            }
            DEVICE_CODE = sb.toString();
        }catch(FileNotFoundException f){
            try{
                FileOutputStream fo = this.ctx.openFileOutput(getDEVICE_FILE_NAME(), Context.MODE_PRIVATE);
                fo.write(createDeviceCode().getBytes());
                fo.flush();
                fo.close();
            }catch(FileNotFoundException fe){

            }
        }
    }
    public String getLOGIN_URL(){
        return LOGIN_URL;
    }
    public String getCODE_URL(){
        return CODE_URL;
    }
    public String getDEVICE_FILE_NAME(){
        return DEVICE_FILE_NAME;
    }
    private String createDeviceCode(){
        return UUID.randomUUID().toString();
    }
    public String getDEVICE_CODE(){
        return DEVICE_CODE;
    }
}
