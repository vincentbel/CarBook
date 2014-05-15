package com.Doric.CarBook.utility;

import android.util.Log;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

//������--JSON��ȡ�뷢��
public class JSONParser {

    private JSONObject jsonObject = null;   //��ȡ����json����
    private String json = "";
    private ServiceHandler serviceHandler = null;

    private static final String TAG = "JSONParser";

    public JSONParser() {
        serviceHandler = new ServiceHandler();
    }

    /**
     * ͨ��URL��ȡJSON����
     *
     * @param url    ��ȡjson�����url
     * @param params ���͵�url�Ĳ���
     * @return ͨ��url��ȡ����json����
     */
    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {
        json = serviceHandler.makeServiceCall(url, ServiceHandler.POST, params);

        if (json == null) {
            return null;
        }
        try {
            Log.i(TAG, json);
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
