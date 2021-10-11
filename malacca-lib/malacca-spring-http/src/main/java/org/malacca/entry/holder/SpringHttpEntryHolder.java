package org.malacca.entry.holder;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSONObject;
import org.malacca.entry.Entry;
import org.malacca.entry.HttpMapping;
import org.malacca.entry.SpringHttpEntry;
import org.malacca.exception.MessagingException;
import org.malacca.exception.ServiceLoadException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.malacca.utils.IPUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
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
 * Author :chensheng 2020/3/19
 * </p>
 * <p>
 * Department :
 * </p>
 */
// TODO: 2020/3/2 多线程
public class SpringHttpEntryHolder extends AbstractEntryHolder<Entry> implements HttpRequestHandler {
    // TODO: 2020/3/2 初始化有待考证
    //port -(id,HttpServer)
    private Map<String, SpringHttpEntry> httpServerMap = new HashMap<>();

    public static final String GET_METHOD = "GET";

    public static final String POST_METHOD = "POST";

    public static final int REQUEST_NUM = 100;

    private static final String CONTENT_TYPE = "application/json;charset=utf-8";

    public static final String ENCODING = System.getProperty("file.encoding");

    private HttpMapping httpMapping;


    @Override
    public void loadEntry(String id, Entry entry) {
        if (SpringHttpEntry.class.isAssignableFrom(entry.getClass())) {
            try {
                exposeHttpEntry(id, (SpringHttpEntry) entry);
            } catch (Exception e) {
                throw new ServiceLoadException("Http服务入口注册失败", e);
            }
        } else {
            throw new ServiceLoadException("Http入口不是SpringHttpEntry.class 类型");
        }
    }

    /**
     * 暴露http服务
     *
     * @param entry
     */
    private void exposeHttpEntry(String id, SpringHttpEntry entry) throws Exception {
        httpMapping.postProcessBeforeInitialization(this, entry, "");
        putHttpServerMap(id, entry);
    }

    @Override
    public void unloadEntry(String id, Entry entry) {
        httpMapping.postProcessBeforeDestruction(this, (SpringHttpEntry) entry, "");
        removeHttpServer(id, entry);
    }


    public void putHttpServerMap(String id, SpringHttpEntry entry) {
        httpServerMap.put(id, entry);
    }

    private void removeHttpServer(String id, Entry entry) {
        SpringHttpEntry springHttpEntry = httpServerMap.get(id);
        if (springHttpEntry != null) {
            httpServerMap.remove(id);
        }
    }

    @Override
    public void handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String uri = httpServletRequest.getRequestURI();
        SpringHttpEntry entry = httpServerMap.get(uri);
        String result;
        if (entry == null) {
            httpServletResponse.setStatus(404);
            result = "No handler found for uri [" + httpServletRequest.getRequestURI() + "] and method [" + httpServletRequest.getMethod() + "]";
        } else {
            String body = IoUtil.read(httpServletRequest.getInputStream(), ENCODING);

            Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
            HashMap<String, Object> params = new HashMap<>();
            params.putAll(parameterMap);
            Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                params.put(name, httpServletRequest.getHeader(name));
            }
            Message<?> message = buildMessage(params, body, entry);
            Message returnMessage = null;
            try {
                returnMessage = entry.handleMessage(message);
                result = getResultStr(returnMessage);
            } catch (MessagingException e) {
                result = getExceptionStr(e);
            }
            httpServletResponse.setStatus(200);
        }
        sendResponse(httpServletResponse, result);
        httpServletResponse.flushBuffer();
    }

    private String getExceptionStr(MessagingException e) {
        JSONObject result = new JSONObject();
        result.put("code", 0);
        result.put("data", null);
        result.put("tips", e.getMessage());
        result.put("cause", "");
        return result.toJSONString();
    }

    private void sendResponse(HttpServletResponse httpServletResponse, String result) throws IOException {
        httpServletResponse.setHeader("Via", IPUtils.getLocalAddress().getHostAddress());
        httpServletResponse.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
        httpServletResponse.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(result.getBytes().length));
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        outputStream.write(result.getBytes());
    }

    private Message<?> buildMessage(Map<String, Object> params, Object body, SpringHttpEntry entry) {
        //todo 根据语法判断消息体 默认是 http传过来的消息体
        Message message = MessageBuilder.withPayload(body).copyContext(params).build();
        return message;
    }

    public HttpMapping getHttpMapping() {
        return httpMapping;
    }

    public void setHttpMapping(RequestMappingHandlerMapping httpMapping) {
        this.httpMapping = (HttpMapping) httpMapping;
    }

    private String getResultStr(Message message) {
        Object payload = message.getPayload();
        if (payload == null) {
            return "";
        }

        if (payload instanceof JSONObject) {
            return JSONObject.toJSONString(payload);
        } else {
            return payload.toString();
        }
    }
}
