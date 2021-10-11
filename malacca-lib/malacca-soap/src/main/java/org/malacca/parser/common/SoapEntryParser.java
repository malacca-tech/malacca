package org.malacca.parser.common;

import org.malacca.definition.EntryDefinition;
import org.malacca.entry.AbstractAdvancedEntry;
import org.malacca.entry.common.SoapEntry;
import org.malacca.parser.AdvancedEntryParser;
import org.malacca.parser.ParserInterface;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/4/3
 * </p>
 * <p>
 * Department :
 * </p>
 */
@ParserInterface(type = "entry", typeAlia = "soap")
public class SoapEntryParser extends AdvancedEntryParser {

    @Override
    public AbstractAdvancedEntry doCreateInstance(EntryDefinition definition) {
        SoapEntry entry = new SoapEntry(definition.getId(), definition.getName());
        return entry;
    }

}
