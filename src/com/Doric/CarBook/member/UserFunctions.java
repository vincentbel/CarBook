package com.Doric.CarBook.member;

import android.content.Context;
import android.util.Log;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.utility.DatabaseHelper;
import com.Doric.CarBook.utility.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserFunctions {

    //服务器请求URL   登录请求的url,务必加上http://或https://
    private static String loginURL = Constant.BASE_URL + "/login.php";
    private static String registerURL = Constant.BASE_URL + "/register.php";
    private static String collectionURL = Constant.BASE_URL + "/user_collect.php";
    private static String myCommentsURL = Constant.BASE_URL + "/user_comments.php";

    //标签
    private static String loginTag = "login";
    private static String registerTag = "register";
    private static String collectionTag = "collect";
    private static String collectionDeleteTag = "delete_collect";
    private static String collectionSyncTag = "collect_sync";
    private static String myCollection = "my_collection";
    private static String defaultCollection = "default_collection";
    private static String myCommentsTag = "my_comments";


    DatabaseHelper db;   //本地SQLite数据库辅助类
    private JSONParser jsonParser;     //json传输数据工具类

    // constructor
    public UserFunctions(Context context) {
        jsonParser = new JSONParser();
        db = new DatabaseHelper(context);
    }

    /**
     * 发送登录请求
     *
     * @param username  登录的用户名
     * @param password  登录密码
     * @return 返回登录是否成功的json对象，具体见@link json文件夹下register_login.json
     */
    public JSONObject loginUser(String username, String password) {
        // 构造发送给服务器端的参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", loginTag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));

        // 获取 json 对象
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

        //将用户信息存到本地SQLite数据库并同步收藏
        SyncData(json);
        return json;
    }

    /**
     * 发送注册请求
     *
     * @param name 注册用户名
     * @param email  注册email
     * @param password  注册密码
     * @return 返回注册是否成功的json对象，具体见@link json文件夹下register_login.json
     */
    public JSONObject registerUser(String name, String email, String password) {
        // 构造发送给服务器端的参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", registerTag));
        params.add(new BasicNameValuePair("username", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        // 获取 json 对象
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);

        //将用户信息存到本地SQLite数据库
        SyncData(json);
        syncCollection();
        return json;
    }

    /**
     * 将用户信息存到本地SQLite数据库并同步收藏
     * @param json 服务器获取的用户数据json对象
     * @return 是否成功
     */
    public boolean SyncData(JSONObject json) {
        if (json != null) {
            try {
                //如果注册或登录成功
                if (json.getString("success").equals("1")) {
                    //将用户信息存到user表中，返回-1表示不成功
                    long id = db.addUser(json.getString("username"),
                            Integer.parseInt(json.getString("user_id")),
                            json.getString("created_at"));
                    //
                    int updatedRow = db.addUserIdToCollection(Integer.parseInt(json.getString("user_id")));
                    syncCollection();
                    return (id != -1 && updatedRow > 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Function get Login status
     */
    public boolean isUserLoggedIn() {
        int count = db.getUserCount();
        if (count > 0) {
            // user logged in
            return true;
        }
        return false;
    }

    /**
     * Function to logout user
     * Reset Database
     */
    public boolean logoutUser() {
        //db.resetCollection();
        db.resetTables();
        return true;
    }


    public String getUsername() {
        return db.getUserDetails().get(DatabaseHelper.KEY_USER_NAME);
    }

    public int getUserId() {
        return Integer.parseInt(db.getUserDetails().get(DatabaseHelper.KEY_USER_ID));
    }

    public boolean addToCollection(int carId) {
        if (isUserLoggedIn()) {
            int userId = getUserId();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", collectionTag));
            params.add(new BasicNameValuePair("user_id", userId + ""));
            params.add(new BasicNameValuePair("car_id", carId + ""));
            // getting JSON Object
            JSONObject json = jsonParser.getJSONFromUrl(collectionURL, params);
            try {
                if (json.getString("success").equals("1") && db.addCollection(userId, carId) > -1) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return (db.addCollection(carId) > -1);
        }
        return false;
    }

    public boolean cancelCollect(int carId) {
        if (isUserLoggedIn()) {
            int userId = getUserId();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", collectionDeleteTag));
            params.add(new BasicNameValuePair("user_id", userId + ""));
            params.add(new BasicNameValuePair("car_id", carId + ""));
            // getting JSON Object
            JSONObject json = jsonParser.getJSONFromUrl(collectionURL, params);
            try {
                if (json.getString("success").equals("1") && db.deleteCollection(userId, carId) > -1) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return (db.deleteCollection(carId) > 0);
        }
        return false;
    }

    //将本地的收藏同步到服务器
    public void syncCollection() {
        String collectJsonString = db.getAllCollection();
        if (collectJsonString != null && isUserLoggedIn()) {
            int userId = getUserId();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", collectionSyncTag));
            params.add(new BasicNameValuePair("user_id", userId + ""));
            params.add(new BasicNameValuePair("collect_data", collectJsonString));
            JSONObject jsonObject = jsonParser.getJSONFromUrl(collectionURL, params);
            try  {
                if (jsonObject.getString("success").equals(1)) {
                    JSONArray JSONCar = jsonObject.getJSONArray("cars");
                    db.resetCollection();
                    for (int i = 0; i < JSONCar.length(); i++) {
                        int carId = JSONCar.getJSONObject(i).getInt("car_id");
                        String createTime = JSONCar.getJSONObject(i).getString("create_time");
                        db.addCollection(userId, carId, createTime);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public JSONObject getMyCollection() {
        if (isUserLoggedIn()) {List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", myCollection));
            params.add(new BasicNameValuePair("user_id", getUserId() + ""));
            return jsonParser.getJSONFromUrl(collectionURL, params);
        } else {
            String collectJsonString = db.getAllCollection();
            if (collectJsonString == null) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("number", 0);
                    jsonObject.put("success", 1);
                    jsonObject.put("error", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonObject;
            }
            Log.i(defaultCollection, collectJsonString);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", defaultCollection));
            params.add(new BasicNameValuePair("car_id", collectJsonString));
            return jsonParser.getJSONFromUrl(collectionURL, params);
        }
    }

    public boolean isCollected(int carId) {
        return db.isCarCollected(carId);
    }



    public JSONObject getMyComments() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", myCommentsTag));
        params.add(new BasicNameValuePair("user_id", getUserId() + ""));
        return jsonParser.getJSONFromUrl(myCommentsURL, params);
    }
}
