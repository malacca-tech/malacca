package org.malacca.utils;

import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
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
 * Author :chensheng 2020/2/26
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class FreeMarkerUtils {

    protected Map<String, Object> messageMap = new HashMap<>();

    public String parseExpression(String config) {
        StringReader stringReader = null;
        Writer writer = null;
        String isMatch = "0";
        try {
            stringReader = new StringReader(config);
            writer = new StringWriter();
            Template template = new Template(null, stringReader, null);
            template.process(messageMap, writer);
            isMatch = "1";
            return writer.toString();
        } catch (IOException e) {
            // TODO: 2020/2/26
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
            // TODO: 2020/2/26
        } finally {
            CloseableUtils.close(writer);
            CloseableUtils.close(stringReader);
            return "1".equals(isMatch) ? writer.toString() : "false";
        }
    }

    public void setVariable(String key, Object value) {
        messageMap.put(key, value);
    }

    public void setMap(Map map) {
        messageMap.putAll(map);
    }
}
