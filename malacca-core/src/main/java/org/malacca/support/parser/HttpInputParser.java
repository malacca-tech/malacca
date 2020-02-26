package org.malacca.support.parser;

import cn.hutool.core.lang.Assert;
import org.malacca.definition.EntryDefinition;
import org.malacca.entry.Entry;
import org.malacca.entry.HttpEntry;

import java.util.Map;

public class HttpInputParser implements EntryParser {
    public static final String URI_KEY = "uri";
    public static final String METHOD_KEY = "method";


    @Override
    public Entry createInstance(EntryDefinition definition) {
        HttpEntry entry = new HttpEntry(definition.getId(), definition.getName());
        entry.setStatus(definition.isStatus());
        entry.setEnv(definition.getEnv());
        Map<String, Object> params = definition.getParams();
        setPath(entry, params.get(URI_KEY));
        setMethod(entry, params.get(METHOD_KEY));
        entry.setEntryKey();
        return entry;
    }

    private void setPath(HttpEntry entry, Object uri) {
        Assert.isInstanceOf(String.class, uri, "the uri is not a string!");
        String uriStr = (String) uri;
        Assert.notBlank(uriStr, "uri cannot be blank");
        entry.setUri(uriStr);
    }

    private void setMethod(HttpEntry entry, Object method) {
        Assert.isInstanceOf(String.class, method, "the method is not a string!");
        String methodStr = (String) method;
        Assert.notBlank(methodStr, "method cannot be blank");
        entry.setMethod(methodStr);
    }
}

