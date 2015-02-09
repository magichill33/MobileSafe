package com.ly.lottery.net;


import com.ly.lottery.ConstantValue;
import com.ly.lottery.GlobalParams;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2015/2/9.
 */
public class HttpClientUtil {
    private HttpClient client;
    private HttpPost post;
    private HttpGet get;

    public HttpClientUtil(){
        client = new DefaultHttpClient();
        //判断是否需要设置代理信息
        if (StringUtils.isNotBlank(GlobalParams.PROXY)){
            //设置代理信息
            HttpHost host = new HttpHost(GlobalParams.PROXY,GlobalParams.PORT);
            client.getParams().setParameter(
                    ConnRouteParams.DEFAULT_PROXY,host
            );
        }
    }

    public InputStream sendXml(String uri,String xml){
        post = new HttpPost(uri);
        try {
            StringEntity entity = new StringEntity(xml, ConstantValue.ENCONDING);
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200){
                return response.getEntity().getContent();
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

}
