package com.csi.csidoora;

import android.content.Context;

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
    private final String CODE_FILE_NAME = "code.csi";
    private String DEVICE_CODE = "";
    private String CODE = "";
    private final int DEVICE_CODE_LEN = 36;
    private final int CODE_LEN = 4;

    private static final CONSTANTS ourInstance = new CONSTANTS();
    public static CONSTANTS getInstance(){return ourInstance;}

    private CONSTANTS() {
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
    public String getCODE_FILE_NAME(){ return DEVICE_FILE_NAME; }
    public String getCODE(){ return CODE; }
    public void sessionCode(Context ctx){
        //To get the device session code, not the actual code
        try{
            FileInputStream fi = ctx.openFileInput(getDEVICE_FILE_NAME());
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
                VolleyService vs = VolleyService.getInstance();
                FileOutputStream fo = ctx.openFileOutput(getCODE_FILE_NAME(), Context.MODE_PRIVATE);
                fo.write(vs.getCode(ctx).getBytes());
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
    public void setCode(Context ctx, String s){
        this.CODE = s;
        try{
            VolleyService vs = VolleyService.getInstance();
            FileOutputStream fo = ctx.openFileOutput(getCODE_FILE_NAME(), Context.MODE_PRIVATE);
            fo.write(this.CODE.getBytes());
            fo.flush();
            fo.close();
        }catch(FileNotFoundException fe){

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
