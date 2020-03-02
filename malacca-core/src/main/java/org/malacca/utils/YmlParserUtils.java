package org.malacca.utils;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import org.malacca.definition.ServiceDefinition;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
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
            if (yamlReader != null) {
                yamlReader.close();
            }
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
            if (yamlReader != null) {
                yamlReader.close();
            }
        }
        return result;
    }
}
