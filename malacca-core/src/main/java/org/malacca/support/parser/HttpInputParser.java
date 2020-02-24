package org.malacca.support.parser;

import org.malacca.entry.HttpEntry;

import java.util.Map;

public class HttpInputParser extends AbstractParser<HttpEntry> {
    public HttpInputParser() {
        super("org.malacca.entry.HttpEntry");
    }

    @Override
    public HttpEntry createInstance(Map<String, Object> params) {
        HttpEntry entry = super.createInstance(params);
        entry.setEntryKey(entry.getPath());
        return entry;
    }
}

