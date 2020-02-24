package org.malacca.utils;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import org.malacca.definition.ServiceDefinition;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YmlParserUtils {

    /**
     * yml 转map
     *
     * @param yml
     * @return
     * @throws IOException
     */
    public static ServiceDefinition ymlToDefinition(String yml) throws IOException {
        YamlReader yamlReader = null;
        ServiceDefinition result;
        try {
            yamlReader = new YamlReader(yml);
            result = yamlReader.read(ServiceDefinition.class);
        } finally {
            yamlReader.close();
        }
        return result;
    }

    public static Map ymlToMap(Reader reader) throws IOException {
        YamlReader yamlReader = null;
        Map result;
        try {
            yamlReader = new YamlReader(reader);
            result = yamlReader.read(Map.class);
        } finally {
            yamlReader.close();
        }
        return result;
    }

    public static void mapToYml(Map map, String path) throws IOException {
        FileWriter fileWriter = null;
        YamlWriter writer = null;
        try {
            fileWriter = new FileWriter(path);
            writer = new YamlWriter(fileWriter);
            writer.write(toHashMap(map));
        } finally {
            writer.close();
            fileWriter.close();
        }
    }

    private static Object toHashMap(Object o) {
        if (o instanceof Map) {
            if (!"HashMap".equals(o.getClass().getSimpleName())) {
                o = new HashMap((Map) o);
            }
            for (Object o1 : ((HashMap) o).entrySet()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) o1;
                entry.setValue(toHashMap(entry.getValue()));
            }
        } else if (o instanceof List) {
            List list = (List) o;
            List newList = new ArrayList();
            for (Object o1 : list) {
                newList.add(toHashMap(o1));
            }
            list.clear();
            list.addAll(newList);
        }
        return o;
    }
}
