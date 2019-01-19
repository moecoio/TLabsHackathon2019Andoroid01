package com.example.myapplication;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class DeviceReg {
    public String device_id = "";
    public String public_key = "";

    public DeviceReg(String devId, String public_key){
        this.device_id = devId;
        this.public_key = public_key;
    }

    @Override
    public String toString() {
        JSONObject jroot = new JSONObject();
        try {
            jroot.put("device_id", device_id);
            jroot.put("public_key", public_key);
        }catch (JSONException exc){
            Log.e("JSON Exception", "");
            exc.printStackTrace();
        }
        return jroot.toString();
    }
}
