package com.Doric.CarBook.utility;


import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

//工具类--连接数据库
public class ServiceHandler {

    public static final int GET = 1;
    public static final int POST = 2;
    public static final String TAG = "service handler";
    static String response = null;
    // Set the timeout in milliseconds until a connection is established.
    // The default value is zero, that means the timeout is not used.
    int timeoutConnection = 6000;
    // Set the default socket timeout (SO_TIMEOUT)
    // in milliseconds which is the timeout for waiting for data.
    int timeoutSocket = 10000;

    public ServiceHandler() {
    }

    public String makeServiceCall(String url, int method) {
        return makeServiceCall(url, method, null);
    }

    public String makeServiceCall(String url, int method, List<NameValuePair> params) {
        HttpParams httpParameters = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

        HttpEntity httpEntity;
        HttpResponse httpResponse = null;

        try {
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = httpClient.execute(httpPost);
            } else if (method == GET) {
                if (params != null) {
                    String paramStr = URLEncodedUtils.format(params, "UTF-8");
                    url += "?" + paramStr;
                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
            }
            Log.i(TAG, httpResponse.getStatusLine().toString());
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpClient.getConnectionManager().shutdown();
        return response;
    }
}
