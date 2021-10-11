package org.malacca.parser;

import org.malacca.definition.EntryDefinition;
import org.malacca.entry.AbstractAdvancedEntry;
import org.malacca.entry.SpringHttpEntry;

@ParserInterface(type = "entry", typeAlia = "springhttp")
public class SpringHttpEntryParser extends AdvancedEntryParser {

    @Override
    public AbstractAdvancedEntry doCreateInstance(EntryDefinition definition) {
        SpringHttpEntry entry = new SpringHttpEntry(definition.getId(), definition.getName());
        return entry;
    }

    public void setPort(SpringHttpEntry entry, Object port) {
    }
}

