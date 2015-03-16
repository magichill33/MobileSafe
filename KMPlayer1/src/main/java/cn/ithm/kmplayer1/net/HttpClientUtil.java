package cn.ithm.kmplayer1.net;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import cn.ithm.kmplayer1.ConstantValue;
import cn.ithm.kmplayer1.GloableParameters;

/**
 * 建立网络链接传输数据
 * 
 * @author Administrator
 * 
 */
public class HttpClientUtil {
	private HttpClient client;

	// 网络访问的方式？get和post

	private HttpPost post;
	private HttpGet get;
	private HttpResponse response;

	private static Header[] headers;

	static {
		headers = new BasicHeader[10];
		headers[0] = new BasicHeader("Appkey", "12343");
		headers[1] = new BasicHeader("Udid", "");// 手机串号
		headers[2] = new BasicHeader("Os", "android");//
		headers[3] = new BasicHeader("Osversion", "");//
		headers[4] = new BasicHeader("Appversion", "");// 1.0
		headers[5] = new BasicHeader("Sourceid", "");//
		headers[6] = new BasicHeader("Ver", "");

		headers[7] = new BasicHeader("Userid", "");
		headers[8] = new BasicHeader("Usersession", "");

		headers[9] = new BasicHeader("Unique", "");
	}

	public HttpClientUtil() {
		client = new DefaultHttpClient();

		// wap方式的ip和端口设置
		if (StringUtils.isNotBlank(GloableParameters.PROXY_IP)) {
			HttpHost host = new HttpHost(GloableParameters.PROXY_IP, GloableParameters.PROXY_PORT);
			// 设置代理的信息
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);// map.put(key,value)
		}

	}

	/**
	 * 发送xml文件到服务器段
	 * 
	 * @param url
	 * @param xml
	 */
	public InputStream sendXml(String url, String xml) {
		post = new HttpPost(url);
		try {
			StringEntity entity = new StringEntity(xml, ConstantValue.ENCODING);
			post.setEntity(entity);

			response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				// 返回的结果是输入流--xml文件
				return response.getEntity().getContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;// 如果获取到null
	}

	/**
	 * 发送一个Post请求
	 * 
	 * @param uri
	 * @param params
	 *            ：参数
	 * @return
	 */
	public String sendPost(String uri, Map<String, String> params) {
		post = new HttpPost(uri);
		// 设置头信息
		// post.setHeaders(headers);

		// 处理超时
		HttpParams httpParams = new BasicHttpParams();//
		httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		post.setParams(httpParams);

		try {
			// 设置参数
			if (params != null && params.size() > 0) {
				List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();

				for (Map.Entry<String, String> item : params.entrySet()) {
					BasicNameValuePair pair = new BasicNameValuePair(item.getKey(), item.getValue());
					parameters.add(pair);
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, ConstantValue.ENCODING);
				post.setEntity(entity);
			}

			response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				// 返回的结果是输入流--Json文件
				// return response.getEntity().getContent();
				return EntityUtils.toString(response.getEntity(), ConstantValue.ENCODING);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String sendGet(String uri) {
		get = new HttpGet(uri);

		HttpParams httpParams = new BasicHttpParams();//
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		get.setParams(httpParams);

		try {
			response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(), ConstantValue.ENCODING);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public InputStream loadImg(String uri) {
		get = new HttpGet(uri);

		HttpParams httpParams = new BasicHttpParams();//
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		get.setParams(httpParams);

		try {
			response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 200) {
				return response.getEntity().getContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
