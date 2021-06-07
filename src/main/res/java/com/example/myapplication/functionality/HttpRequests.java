package com.example.myapplication.functionality;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequests {
    public Object get(String urlStr, Object defaultValue){
        try{
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if(httpURLConnection.getResponseCode() != 200){
                return defaultValue;
            }

            String data = httpURLConnection.getResponseMessage();
            return data;
        } catch(Throwable throwable){}
        return defaultValue;
    }
}
