package org.malacca.component;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpRequest;
import org.malacca.exception.MessagingException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/2
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class CommonHttpOutComponent extends AbstractComponent {

    public static final String TYPE = "httpOut";

    public static final String POST_METHOD = "POST";

    /**
     * http路径
     */
    private String url;

    /**
     *
     */
    private String method = "POST";

    /**
     * 请求头
     */
    private Map<String, String> headers = new HashMap<>();

    /**
     * 请求参数 get方式 和表单使用此参数
     */
    private Map<String, Object> parameters = new HashMap<>();

    /**
     *
     */
    private String mediaType = null;

    private int timeout = 3000;

    public CommonHttpOutComponent() {
        super(TYPE, TYPE);
    }

    public CommonHttpOutComponent(String id, String name) {
        super(id, name);
    }


    @Override
    public Message handleMessage(Message<?> message) throws MessagingException {
        HttpRequest request;
        if (POST_METHOD.equals(method)) {
            request = buildPostRequest(message);
        } else {
            request = buildGetRequest(message);
        }
        setHeaders(request);
        String result = request.execute().body();
        Message resultMessage = MessageBuilder.withPayload(result).copyContext(message.getContext()).build();
        return resultMessage;
    }

    private HttpRequest buildGetRequest(Message<?> message) {
        // TODO: 2020/3/2 freemarker
        StringBuilder path = new StringBuilder(url + "?");
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            path.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return HttpRequest.get(path.toString());
    }

    private HttpRequest setHeaders(HttpRequest request) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request = request.header(entry.getKey(), entry.getValue());
        }
        return request;
    }

    private HttpRequest buildPostRequest(Message<?> message) {
        HttpRequest request = HttpRequest.post(url).timeout(timeout);
        if ("表单".equals(mediaType)) {
            // TODO: 2020/3/2 freemarker 表达式
            request = request.form(parameters);
        } else {
            request.body(String.valueOf(message.getPayload()));
        }
        return request;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
