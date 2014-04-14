package com.Doric.CarBook.utility;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

//工具类--JSON获取与发送
public class JSONParser {

    private JSONObject jsonObject = null;   //获取到的json对象
    private String json = "";
    private ServiceHandler serviceHandler = null;

    public JSONParser() {
        serviceHandler = new ServiceHandler();
    }

    /**
     * 通过URL获取JSON对象
     *
     * @param url    获取json对象的url
     * @param params 发送到url的参数
     * @return 通过url获取到的json对象
     */
    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {
        json = serviceHandler.makeServiceCall(url, ServiceHandler.POST, params);
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getJSONFromUrl(String url) {
        return getJSONFromUrl(url, null);
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
