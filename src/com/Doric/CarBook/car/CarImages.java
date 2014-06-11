package com.Doric.CarBook.car;

import com.Doric.CarBook.Constant;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sunyao_Will on 2014/4/27.
 */
public class CarImages {


    // 用于储存图片URL的字符串数组
    public static String[] imageUrls;
    /*
    * 初始化字符串数组
    */
    public static void initImages(int n, JSONObject carPic) {
        imageUrls = new String[n];
        for (Integer i = 1; i <= n; i++) {
            try {
                imageUrls[i - 1] = Constant.BASE_URL + "/" + carPic.getString("pictures_url_" + i.toString());
                System.out.println(imageUrls[i - 1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
