package com.example.myapplication;

import android.util.Log;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Srv {

    private static final String urlRoot = "http://157.230.26.24:3030";
    private static final String urlMsg = "/messages";

    public static void postMessage(DeviceMessage message){
        postString(urlMsg, message.toString());
    }

    public static void postString(String url, String payLoad){
        class PostMsgToSrv implements Runnable {
            String url = "";
            String payLoad = "";
            PostMsgToSrv(String url, String payLoad) { this.url = url; this.payLoad = payLoad;}
            public void run() {
                try {
                    URL url = new URL(urlRoot+this.url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    MyUtil.log("payLoad:" + payLoad);
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(payLoad);
                    os.flush();
                    os.close();
                    String responseCode = String.valueOf(conn.getResponseCode());
                    Log.e("STATUS", responseCode);
                    if (responseCode.equals("200")) ;//listener.response(MyServerResponseEnum.OK);
                    else if (responseCode.equals("500")) ;//listener.response(MyServerResponseEnum.RESPONSE500);
                    else ;//listener.response(MyServerResponseEnum.ERROR);
                    Log.e("MSG" , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        new Thread(new PostMsgToSrv(url, payLoad)).start();
    }




//    public static void sendPost() {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    URL url = new URL("http://157.230.26.24:3030/messages");
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("POST");
//                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//                    conn.setRequestProperty("Accept","application/json");
//                    conn.setDoOutput(true);
//                    conn.setDoInput(true);
//
//                    JSONObject jsonParam = new JSONObject();
//                    jsonParam.put("device_id", "aa:bb:cc:ee");
//                    jsonParam.put("message", "Lorem ipsum");
//                    jsonParam.put("signature", "aabbccdd");
//                    Log.e("JSON", jsonParam.toString());

//                    String testMessage = "{\n" +
//                            "  \"device_id\": {\n" +
//                            "    \"value\": \"aa:bb:cc:ee\"\n" +
//                            "  },\n" +
//                            "  \"message\": {\n" +
//                            "    \"value\": \"Lorem ipsum\"\n" +
//                            "  },\n" +
//                            "  \"signature\": {\n" +
//                            "    \"value\": \"aabbccdd\"\n" +
//                            "  }\n" +
//                            "}";
//                    DeviceMessage testMessage = new DeviceMessage("aa:bb:cc:ee", "Hello World!", "here should be signature");
//                    MyUtil.log("Test Message");
//                    MyUtil.log(testMessage.toString());
//                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
//                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
//                    //os.writeBytes(testMessage);
//                    os.writeBytes(testMessage.toString());
//
//                    os.flush();
//                    os.close();
//
//                    Log.e("STATUS", String.valueOf(conn.getResponseCode()));
//                    Log.e("MSG" , conn.getResponseMessage());
//
//                    conn.disconnect();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        thread.start();
//    }


//    private static String convertStreamToString(InputStream is) {
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        StringBuilder sb = new StringBuilder();
//
//        String line = null;
//        try {
//            while ((line = reader.readLine()) != null) {
//                sb.append((line + "\n"));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return sb.toString();
//    }
}
