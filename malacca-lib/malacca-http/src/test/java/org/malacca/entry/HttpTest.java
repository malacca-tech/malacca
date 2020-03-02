package org.malacca.entry;

import cn.hutool.core.io.IoUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.malacca.entry.register.DefaultEntryRegister;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.Map;
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
public class HttpTest {


    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
//            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
//            server.createContext("/form", new FormHttpHandler());
//            server.createContext("/info", new InfoHttpHandler());
//            server.start();
//            System.out.println("ssss");
            DefaultEntryRegister entryRegister = new DefaultEntryRegister();
            CommonHttpEntry entry = new CommonHttpEntry("ss", "name");
            entry.setMethod("GET");
            entry.setPort(8889);
            entry.setUri("/path/test");
            entry.setEntryKey();
            entryRegister.registerEntry(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> getParams(String paramStr) {
        if (paramStr != null) {
            return Stream.of(paramStr.split("&"))
                    .filter(s -> s.split("=").length == 2 && s.split("=")[0].length() > 0 && s.split("=")[1].length() > 0)
                    .collect(Collectors.toMap(x -> x.toString().split("=")[0], y -> y.toString().split("=")[1]));
        }
        return null;
    }

    static class FormHttpHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("from方法：" + exchange.getRequestMethod());//get
            String paramStr = exchange.getRequestURI().getQuery();
            exchange.sendResponseHeaders(200, 0);
            String body = "<!DOCTYPE html>"
                    + "<head>"
                    + "<meta charset='utf-8'>"
                    + "<title>FromPage</title>"
                    + "</head>"
                    + "</body>"
                    + "<form action='info' method='post'>"
                    + "abc:<input type='text' name='abc'/><br/>"
                    + "def:<input type='text' name='def'/><br/>"
                    + "<input type='submit' value='测试'/>"
                    + "</form>"
                    + "</body>";
            exchange.getResponseBody().write(body.getBytes());
            exchange.close();
        }

    }

    static class InfoHttpHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("info方法：" + exchange.getRequestMethod());//post
            String bodyStr = new String(IoUtil.readBytes(exchange.getRequestBody()));
            bodyStr = URLDecoder.decode(bodyStr, "UTF-8");
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().write("success".getBytes());
            exchange.close();
        }

    }
}
