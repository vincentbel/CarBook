package com.Doric.CarBook.search;

import android.os.Environment;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/5/27.
 */

/**
 * 数据缓存。提供读写功能
 */
public  class DataCache {

    /**
     * push the carseable,carseries,carinfor data to cache
     */
    private static  String dir;
    private static String path;
    static {
        dir = getSDPath()
                + "/CarBook/Cache/";
        path = dir+"Cache.txt";
    }
    public static void OutputToCacheFile(List<NameValuePair> params,JSONObject jsonObject){
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        File dataFile =new File(path);
        FileWriter writer=null;
        try {
            if (!dataFile.exists()) {

                dataFile.createNewFile();
                writer = new FileWriter(path, true);

                String str = params.toString() + "<json>" + jsonObject.toString() + "</json>\n";

                writer.write(str);

                //write


            } else {
                //read
                BufferedReader br = new BufferedReader(new FileReader(dataFile));
                String temp = null;
                StringBuffer sb = new StringBuffer();
                temp = br.readLine();
                while (temp != null){
                    sb.append(temp + "");
                    temp = br.readLine();
                }
                if(temp!=null){
                    if(temp.indexOf(params.toString())>0){
                        return;
                    }
                }

                writer = new FileWriter(path, true);
                String str = params.toString() + "\n<json>" + jsonObject.toString() + "</json>\n";
                System.out.println(str);
                writer.write(str);
            }
        }
        catch(java.io.IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                if (writer != null)
                    writer.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }


    }

    public static JSONObject InputToMemory(List<NameValuePair> params){

        File dataFile = new File(path);

        if(dataFile.exists()&&!dataFile.isDirectory()){



            BufferedReader br = null;
            StringBuilder content = new StringBuilder();;
            String line = null;
            try {
                br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(new File(path)), "UTF-8"));
                while((line = br.readLine()) != null) {
                    content.append(line);
                }
                br.close();
            } catch (IOException e) {
               e.printStackTrace();

            }

            String temp = content.toString();
            String par= params.toString();
                int index = temp.indexOf(par);

                if(index>=0){
                    String tmp = temp.substring(index);
                    tmp = tmp.substring(tmp.indexOf("<json>")+6,tmp.indexOf("</json>"));

                    try {
                        return new JSONObject(tmp);
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                        return null;
                    }
                }
                else{
                    return null;
                }



            }


        return null;
    }

    private static String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if   (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();

    }


}
