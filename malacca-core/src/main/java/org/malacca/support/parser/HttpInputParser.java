package org.malacca.support.parser;

import cn.hutool.core.lang.Assert;
import org.malacca.definition.EntryDefinition;
import org.malacca.entry.Entry;
import org.malacca.entry.HttpEntry;
import org.malacca.entry.holder.HttpEntryHolder;

import java.net.URI;
import java.util.Map;

public class HttpInputParser implements EntryParser {
    public static final String URI_KEY = "uri";

    @Override
    public Entry createInstance(EntryDefinition definition) {
        HttpEntry entry = new HttpEntry(definition.getId(), definition.getName());
        Map<String, Object> params = definition.getParams();
        // TODO: 2020/2/25 注入其它属性，并对属性进行校验 并且属性的key值要写成常量，作为parser的成员变量
        // todo 按照下面的规则和方式把其它的补全
        setPath(entry, params.get(URI_KEY));
        return entry;
    }

    private void setPath(HttpEntry entry, Object uri) {
        Assert.isInstanceOf(String.class, uri, "the uri is not a string!");
        String uriStr = (String) uri;
        Assert.notBlank(uriStr, "uri cannot be blank");
        entry.setPath(uriStr);
    }
}

