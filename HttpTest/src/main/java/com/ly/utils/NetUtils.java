package com.ly.utils;

import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpConnection;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/2/2.
 */
public class NetUtils {
    private static final String TAG = "NetUtils";
    private static final String urlStr = "http://192.168.0.113:8080/HttpServer/servlet/LoginServlet";
    /**
     * 使用get的方式登录
     * @param userName
     * @param password
     * @return
     */
    public static String loginOfGet(String userName,String password){
        /**
         * 默认使用utf-8编码
         */
        HttpURLConnection conn = null;
        String data = "username="+ URLEncoder.encode(userName)+"&password="+URLEncoder.encode(password);
        try {
            URL url = new URL(urlStr+"?"+data);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET"); //设置请求方式
            conn.setConnectTimeout(10000); //连接超时时间
            conn.setReadTimeout(5000); //读数据的超时时间

            int responseCode = conn.getResponseCode(); //获取状态响应吗
            if(responseCode==200){
                InputStream ips = conn.getInputStream();
                String text = getStringFromInputStream(ips);
                return text;
            }else {
                Log.i(TAG, "访问失败: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (conn!=null){
                conn.disconnect();
            }
        }
        return null;
    }

    public static String loginOfPost(String userName,String password){
        HttpURLConnection conn = null;

        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true); //必须设置此方法，允许输出
            //post请求参数
            String data = "username="+userName+"&password="+password;
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();
            if (responseCode==200){
                InputStream ips = conn.getInputStream();
                String text = getStringFromInputStream(ips);
                return text;
            }else {
                Log.i(TAG, "访问失败: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (conn!=null){
                conn.disconnect();
            }
        }

        return null;

    }


    /**
     * 使用httpClient get方式登录
     * @param userName
     * @param password
     * @return
     */
    public static String loginHttpGet(String userName,String password)
    {
        HttpClient client = null;
        //定义一个客户端
        client = new DefaultHttpClient();
        // 定义一个get请求方法
        String data = "username=" + userName + "&password=" + password;
        HttpGet get = new HttpGet(urlStr+"?"+data);
        try {
            // response 服务器相应对象, 其中包含了状态信息和服务器返回的数据
            HttpResponse response = client.execute(get); // 开始执行get方法, 请求网络
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode==200){
                InputStream ips = response.getEntity().getContent();
                String text = getStringFromInputStream(ips);
                return text;
            }else {
                Log.i(TAG, "请求失败: " + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (client!=null){
                client.getConnectionManager().shutdown();
            }
        }

        return null;
    }

    /**
     * 使用httpClient post的方式登录
     * @param userName
     * @param password
     * @return
     */
    public static String loginHttpPost(String userName,String password){
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(urlStr);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("username",userName));
        parameters.add(new BasicNameValuePair("password",password));
        //把post请求的参数包装一层
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,"utf-8");
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200){
                InputStream ips = response.getEntity().getContent();
                String text = getStringFromInputStream(ips);
                return text;
            }else {
                Log.i(TAG, "请求失败: " + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (client!=null){
                client.getConnectionManager().shutdown();
            }
        }
        return null;
    }

    private static String getStringFromInputStream(InputStream ips) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = ips.read(buffer))!=-1){
            baos.write(buffer,0,len);
        }
        ips.close();
        String text = baos.toString(); //把流中数据转换成字符串，采用的是utf-8
        baos.close();
        return text;
    }
}
