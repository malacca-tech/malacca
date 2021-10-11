package org.malacca.kit;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpKit {

    public static String service(String url
            , Map<String, String> headers
            , String method
            , Map<String, String> parameters
            , String mediaType
            , String payload
            , RequestConfig requestConfig) throws Exception {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            httpclient = HttpClients.createDefault();
            if (requestConfig == null) {
                requestConfig = RequestConfig.custom()
                        .setConnectTimeout(60 * 1000)
                        .setConnectionRequestTimeout(60 * 1000)
                        .setSocketTimeout(60 * 1000).build();
            }
            if ("GET".equals(method)) {
                URIBuilder uriBuilder = new URIBuilder(url);
                if (parameters != null) {
                    for (Map.Entry<String, String> entry : parameters.entrySet()) {
                        uriBuilder.addParameter(entry.getKey(), entry.getValue());
                    }
                }
                HttpGet httpGet = new HttpGet(uriBuilder.build());
                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        httpGet.setHeader(entry.getKey(), entry.getValue());
                    }
                }
                httpGet.setConfig(requestConfig);
                httpResponse = httpclient.execute(httpGet);
            } else {
                HttpPost httpPost;
                StringEntity entity;
                if (mediaType != null && mediaType.startsWith("application/x-www-form-urlencoded")) {
                    httpPost = new HttpPost(url);
                    List<NameValuePair> paramList = new ArrayList<>();
                    for (Map.Entry<String, String> entry : parameters.entrySet()) {
                        paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                    }
                    entity = new UrlEncodedFormEntity(paramList, "UTF-8");
                } else {
                    URIBuilder uriBuilder = new URIBuilder(url);
                    if (parameters != null) {
                        for (Map.Entry<String, String> entry : parameters.entrySet()) {
                            uriBuilder.addParameter(entry.getKey(), entry.getValue());
                        }
                    }
                    httpPost = new HttpPost(uriBuilder.build());
                    entity = new StringEntity(payload, ContentType.create(mediaType == null ? "application/json" : mediaType, "UTF-8"));
                }
                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        httpPost.setHeader(entry.getKey(), entry.getValue());
                    }
                }
                httpPost.setEntity(entity);
                httpPost.setConfig(requestConfig);
                httpResponse = httpclient.execute(httpPost);
            }
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                return EntityUtils.toString(httpEntity);//取出应答字符串
            } else {
                throw new RuntimeException("http处理异常,错误代码:" + statusCode);
            }
        } finally {
            CloseKit.close(httpclient);
            CloseKit.close(httpResponse);
        }
    }

    public static String service(String url
            , Map<String, String> headers
            , String method
            , Map<String, String> parameters
            , String mediaType
            , String payload) throws Exception {
        return service(url, headers, method, parameters, mediaType, payload, null);
    }

    public static String postForm(String url
            , Map<String, String> headers
            , Map<String, String> parameters) throws Exception {
        return service(url, headers, "POST", parameters, null, null);
    }

    public static String postForm(String url
            , Map<String, String> parameters) throws Exception {
        return service(url, null, "POST", parameters, null, null);
    }

    public static String postBody(String url
            , Map<String, String> headers
            , String mediaType
            , String payload) throws Exception {
        return service(url, headers, "POST", null, mediaType, payload);
    }

    public static String get(String url, Map<String, String> headers) throws Exception {
        return service(url, headers, "GET", null, null, null);
    }

    public static String get(String url) throws Exception {
        return service(url, null, "GET", null, null, null);
    }

    public static String fastGet(String url) throws Exception {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000).build();
        return service(url, null, "GET", null, null, null, requestConfig);
    }
}
