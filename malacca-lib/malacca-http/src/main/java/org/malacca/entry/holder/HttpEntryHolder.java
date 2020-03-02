package org.malacca.entry.holder;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import org.malacca.entry.CommonHttpEntry;
import org.malacca.entry.Entry;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
// TODO: 2020/3/2 多线程
public class HttpEntryHolder extends AbstractEntryHolder<Entry> implements HttpHandler {
    /**
     * http Entry缓存
     */
    private Map<String, CommonHttpEntry> httpEntryMap;

    // TODO: 2020/3/2 初始化有待考证
    private Map<String, HttpServer> httpServerMap = new HashMap<>();

    public static final String GET_METHOD = "GET";

    public static final String POST_METHOD = "POST";

    public static final String ENCODING = System.getProperty("file.encoding");

    public static final int REQUEST_NUM = 100;

    @Override
    public void loadEntry(String id, Entry entry) {
        if (CommonHttpEntry.class.isAssignableFrom(entry.getClass())) {
            try {
                exposeHttpEntry((CommonHttpEntry) entry);
            } catch (IOException e) {
                e.printStackTrace();
                // TODO: 2020/3/2
            }
            getHttpEntryMap().put(id, (CommonHttpEntry) entry);
        } else {
            // TODO: 2020/3/2 异常  UnSupportEntryTypeException
        }

    }

    /**
     * 暴露http服务
     *
     * @param entry
     */
    private void exposeHttpEntry(CommonHttpEntry entry) throws IOException {
        // TODO: 2020/3/2 日志
        HttpServerProvider provider = HttpServerProvider.provider();
        HttpServer httpserver = provider.createHttpServer(new InetSocketAddress(entry.getPort()), REQUEST_NUM);
        httpserver.createContext(entry.getUri(), this::handle);
        httpserver.setExecutor(null);
        httpserver.start();
        httpServerMap.put(entry.getId(), httpserver);
    }

    @Override
    public void unloadEntry(String id, Entry entry) {
        HttpServer httpServer = httpServerMap.get(entry.getId());
        if (httpServer != null) {
            httpServer.stop(REQUEST_NUM);
        }
        getHttpEntryMap().remove(id, entry);
    }

    public Map<String, CommonHttpEntry> getHttpEntryMap() {
        if (httpEntryMap == null) {
            this.httpEntryMap = new HashMap<>(16);
        }
        return this.httpEntryMap;
    }

    public Map<String, HttpServer> getHttpServerMap() {
        return httpServerMap;
    }

    public void setHttpServerMap(Map<String, HttpServer> httpServerMap) {
        this.httpServerMap = httpServerMap;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        // parse request
        OutputStream outputStream = httpExchange.getResponseBody();
        URI requestedUri = httpExchange.getRequestURI();
        Map<String, Object> params = getParams(requestedUri.getRawQuery());
        String rawPath = requestedUri.getRawPath();
        CommonHttpEntry entry = httpEntryMap.get(rawPath);

        String body = "";
        if (requestMethod.equalsIgnoreCase(GET_METHOD)) {
            Headers responseHeaders = httpExchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json");

            httpExchange.sendResponseHeaders(200, 0);
            //he.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getBytes().length);

        } else if (requestMethod.equalsIgnoreCase(POST_METHOD)) {
            // TODO: 2020/3/2 表单格式
            InputStream inputStream = httpExchange.getRequestBody();
            body = IoUtil.read(inputStream, ENCODING);
        }

        Message<?> message = buildMessage(params, body, entry);
        Message resultMessage = entry.handleMessage(message);
        writeResponse(resultMessage, outputStream);
        outputStream.close();
    }

    private Message<?> buildMessage(Map<String, Object> params, Object body, CommonHttpEntry entry) {
        //todo 根据语法判断消息体 默认是 http传过来的消息体
        Message message = MessageBuilder.withPayload(body).copyContext(params).build();
        return message;
    }

    private void writeResponse(Message<?> message, OutputStream outputStream) throws IOException {
        Object payload = message.getPayload();
        if (payload == null) {
            payload = "";
        }
        if (payload instanceof String) {
            outputStream.write(((String) payload).getBytes());
        } else if (payload instanceof JSONObject) {
            outputStream.write(JSONObject.toJSONString(payload).getBytes());
        } else {
            outputStream.write(payload.toString().getBytes());
        }
    }

    private Map<String, Object> getParams(String query) {
        if (query != null) {
            return Stream.of(query.split("&"))
                    .filter(s -> s.split("=").length == 2 && s.split("=")[0].length() > 0 && s.split("=")[1].length() > 0)
                    .collect(Collectors.toMap(x -> x.toString().split("=")[0], y -> y.toString().split("=")[1]));
        }
        return null;
    }

}
