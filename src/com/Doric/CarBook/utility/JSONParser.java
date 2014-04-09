package com.Doric.CarBook.utility;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

//工具类--JSON获取与发送
public class JSONParser {

    static JSONObject jsonObject = null;
    static String json = "";
    private ServiceHandler serviceHandler = null;

    public JSONParser() {
        serviceHandler = new ServiceHandler();
    }

    //通过URL获取JSON对象
    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {
        json = serviceHandler.makeServiceCall(url, ServiceHandler.POST, params);
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
