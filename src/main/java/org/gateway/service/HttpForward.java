package org.gateway.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.gateway.pojo.Api;
import org.springframework.stereotype.Service;

/**
 * Http代理转发
 * @author mawentao
 *
 */
@Service
public class HttpForward implements IForward {

	@Override
	public Object forward(HttpServletRequest request, Api api) throws Exception {
		Object res = null;
		String url = api.getUri();
		HttpClient client = HttpClients.createDefault();
        HttpUriRequest req;
        HttpResponse response;
        
        //1. 设置请求和传输超时时间
        RequestConfig.Builder builder = RequestConfig.custom();
		if (api.getTimeout()>0) {
			int timeout = api.getTimeout();
			builder.setConnectionRequestTimeout(timeout)
					.setConnectTimeout(timeout)
					.setSocketTimeout(timeout);
		}
		RequestConfig requestConfig = builder.build();
		
		//2. 构建Http请求
		String contentType = request.getContentType();
		if (contentType==null) {
			req = toHttpGet(url, request, requestConfig);
		} else {
			req = toHttpPost(url, request, requestConfig);
		}
		if (req==null) {
			throw new Exception("cannot forward "+contentType);
		}
		
		//3. 附加在Header中的字段
		Enumeration<String> attrbutes = request.getAttributeNames();
		while (attrbutes.hasMoreElements()) {
			String attr = attrbutes.nextElement();
			if (attr.startsWith("X-GW")) {
				String attrValue = (String)request.getAttribute(attr);
				req.setHeader(attr,attrValue);
				System.err.println(attr+":"+attrValue);
			}
		}
		
		//4. 发送http请求，处理返回 
		response = client.execute(req);
		int resStatusCode = response.getStatusLine().getStatusCode();
		if (resStatusCode!=200) {
			throw new Exception("Http State: "+resStatusCode);
		}
		res = EntityUtils.toString(response.getEntity());
		return res;
	}
	

	/**
	 * 将request转为HttpGet
	 * @param uri
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private HttpGet toHttpGet(String uri, HttpServletRequest request, RequestConfig requestConfig) throws Exception {
        URIBuilder builder = new URIBuilder(uri);
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String nex = enumeration.nextElement();
            builder.setParameter(nex, request.getParameter(nex));
        }
        HttpGet httpGet = new HttpGet(builder.build());
        httpGet.setConfig(requestConfig);
        return httpGet;
	}
	
	/**
	 * 将request转为HttpPost
	 * @param uri
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private HttpPost toHttpPost(String uri, HttpServletRequest request, RequestConfig requestConfig) throws Exception {
		StringEntity entity = null;
//		System.err.println(request.getContentType());
	    if (request.getContentType().contains("json")) {
	        entity = jsonData(request);  //填充json数据
	    } else {
	        entity = formData(request);  //填充form数据
	    }
	    HttpPost httpPost = new HttpPost(uri);
	    httpPost.setHeader("Content-Type", request.getHeader("Content-Type"));
	    httpPost.setEntity(entity);
	    httpPost.setConfig(requestConfig);
	    return httpPost;
	}
	
	/**
	 * 处理post请求 form数据 填充form数据
	 *
	 * @param request 前台请求
	 */
	public UrlEncodedFormEntity formData(HttpServletRequest request) {
	    UrlEncodedFormEntity urlEncodedFormEntity = null;
	    try {
	        List<NameValuePair> pairs = new ArrayList<NameValuePair>();  //存储参数
	        Enumeration<String> params = request.getParameterNames();  //获取前台传来的参数
	        while (params.hasMoreElements()) {
	            String name = params.nextElement();
	            pairs.add(new BasicNameValuePair(name, request.getParameter(name)));
	        }
	        //根据参数创建参数体，以便放到post方法中
	        urlEncodedFormEntity = new UrlEncodedFormEntity(pairs, request.getCharacterEncoding());
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	    return urlEncodedFormEntity;
	}
	
	/**
	 * 处理post请求 json数据
	 *
	 * @param request 前台请求
	 */
	public StringEntity jsonData(HttpServletRequest request) {
	    InputStreamReader is = null;
	    try {
	        is = new InputStreamReader(request.getInputStream(), request.getCharacterEncoding());
	        BufferedReader reader = new BufferedReader(is);
	        //将json数据放到String中
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	        //根据json数据创建请求体
	        return new StringEntity(sb.toString(), request.getCharacterEncoding()); 
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return null;
	}
	
}
