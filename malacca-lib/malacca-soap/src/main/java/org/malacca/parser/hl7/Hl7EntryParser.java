package org.malacca.parser.hl7;

import org.malacca.definition.EntryDefinition;
import org.malacca.entry.AbstractAdvancedEntry;
import org.malacca.entry.hl7.Hl7Entry;
import org.malacca.parser.AdvancedEntryParser;
import org.malacca.parser.ParserInterface;

import java.lang.reflect.Method;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/4/6
 * </p>
 * <p>
 * Department :
 * </p>
 */
@ParserInterface(type = "entry", typeAlia = "hl7")
public class Hl7EntryParser extends AdvancedEntryParser {

    @Override
    public AbstractAdvancedEntry doCreateInstance(EntryDefinition definition) {
        Hl7Entry hl7Entry = new Hl7Entry(definition.getId(), definition.getName());
        hl7Entry.setStatus(definition.isStatus());
        hl7Entry.setEnv(definition.getEnv());
        return hl7Entry;
    }

}
