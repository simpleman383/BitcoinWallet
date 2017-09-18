package com.simpleman.payture.bitcoinwallet.HTTP;

import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;


public class AuthRequest extends JsonObjectRequest {

    private ArrayMap<String, String> params;
    private static final int METHOD = Request.Method.POST;
    private static final String BASE_URL = "http://10.0.2.2:53039";

    public AuthRequest(String route,
                       ArrayMap<String, String> params,
                       Response.Listener<JSONObject> responseListener,
                       Response.ErrorListener errorListener) {
        super(METHOD, getURL(route), getCustomParams(params), responseListener, errorListener);
        this.params = params;
    }

    @Override
    public ArrayMap<String, String> getHeaders() throws AuthFailureError {
        ArrayMap<String, String> headers = new ArrayMap<>();
        if (params != null && params.containsKey("token")) {
            headers.put("x-device", "android");
            headers.put("x-captcha", params.get("token"));
        }
        return headers;
    }

    private static String getURL(String route){
        return String.format("%s/%s", BASE_URL, route);
    }

    private static JSONObject getCustomParams(ArrayMap<String, String> params){

        String rawData = "";
        if (params != null) {
            for (String key : params.keySet()) {
                rawData += String.format("\"%s\":\"%s\",", key, params.get(key));
            }
            rawData = rawData.substring(0, rawData.length() - 1);
            rawData = String.format("{%s}", rawData);
        }

        try {
            return new JSONObject(rawData);
        } catch (JSONException ex) {
            Log.e("GetPhoneCodeRequest", ex.toString());
            return new JSONObject();
        }
    }
}
