package org.malacca.utils;

import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class FreeMarker {

    protected Map<String, Object> messageMap = new HashMap<>();

    public String parseExpression(String config) throws Exception {
        StringReader stringReader = null;
        Writer writer = null;
        try {
            stringReader = new StringReader(config);
            writer = new StringWriter();
            Template template = new Template(null, stringReader, null);
            template.process(messageMap, writer);
            return writer.toString();
        } catch (IOException e) {
            throw new Exception("FreeMarker输入输出异常:" + e.getMessage()
                    + config
                    , e);
        } catch (TemplateException e) {
            throw new Exception("FreeMarker模板异常:" + e.getMessage()
                    + config
                    , e);
        } finally {
            CloseableUtils.close(writer);
            CloseableUtils.close(stringReader);
        }
    }

    public void setVariable(String key, Object value) {
        messageMap.put(key, value);
    }

    public Object parseExpressionExtend(String config) throws Exception {
        if (config != null && config.startsWith("Timestamp(")) {
            try {
                String timestampValues[] = config.split(",");
                String timestamp = timestampValues[0].replace("Timestamp(", "");
                String dateFormat = timestampValues[1].replace(")", "");
                SimpleDateFormat format = new SimpleDateFormat(dateFormat);
                return new Timestamp(format.parse(timestamp).getTime());
            } catch (Exception e) {
                throw new Exception("日期转换失败，请使用Timestamp(日期时间值,日期时间格式)标识" + config, e);
            }
        }
        return config;
    }

    public void setMap(Map map) {
        messageMap.putAll(map);
    }
}
