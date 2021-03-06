package com.example.myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyServerResponse {

    public MyServerResponseEnum responseCode = MyServerResponseEnum.BAD_SIGNATURE;
    public String id = "";
    public String stax_id = "";

    public MyServerResponse(){

    }

    public MyServerResponse (String payLoad, String code){
        try {
            JSONObject root = new JSONObject(payLoad);
            JSONArray data = root.getJSONArray("data");
            JSONObject item = data.getJSONObject(0);
            this.id = item.getString("id");
            this.stax_id = item.getString("stax_id");

            setResponseCode(code);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setResponseCode(String code){
        if (code.equals("200")) this.responseCode = MyServerResponseEnum.OK;
        else if (code.equals("500")) this.responseCode =  MyServerResponseEnum.RESPONSE500;
        else if (code.equals("404")) this.responseCode =  MyServerResponseEnum.DEVICE_NOT_FOUND;
        else if (code.equals("429")) this.responseCode =  MyServerResponseEnum.NOT_SO_FAST;
        else this.responseCode = MyServerResponseEnum.BAD_SIGNATURE;
    }
}
