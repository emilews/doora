package com.csi.csidoora;

public class CONSTANTS {
    private final String LOGIN_URL = "http://192.168.1.72/csi/login/";
    private final String CODE_URL = "http://192.168.1.72/csi/get_code/";

    public CONSTANTS(){}
    public String getLOGIN_URL(){
        return LOGIN_URL;
    }
    public String getCODE_URL(){
        return CODE_URL;
    }
}
