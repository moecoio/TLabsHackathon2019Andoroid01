package com.example.myapplication;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceMessage {
    public String device_id = "";
    public String message = "";
    public String signature = "";

    public DeviceMessage(String devId, String msg, String sig){
        this.device_id = devId;
        this.message = msg;
        this.signature = sig;
    }

    @Override
    public String toString() {
        JSONObject jroot = new JSONObject();
        try {
            jroot.put("device_id", device_id);
            jroot.put("message", message);
            jroot.put("signature",signature);
        }catch (JSONException exc){
            Log.e("JSON Exception", "");
            exc.printStackTrace();
        }

        return jroot.toString();
    }
}
