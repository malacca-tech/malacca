package org.malacca.parser.simple;

import org.malacca.definition.EntryDefinition;
import org.malacca.entry.AbstractAdvancedEntry;
import org.malacca.entry.simple.MalaccaSoapEntry;
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
 * Author :chensheng 2021/1/20
 * </p>
 * <p>
 * Department :
 * </p>
 */
@ParserInterface(type = "entry", typeAlia = "simpleSoap")
public class MalaccaSoapEntryParser extends AdvancedEntryParser {

    @Override
    public AbstractAdvancedEntry doCreateInstance(EntryDefinition definition) {
        MalaccaSoapEntry malaccaSoapEntry = new MalaccaSoapEntry(definition.getId(), definition.getName());
        malaccaSoapEntry.setEnv(definition.getEnv());
        return malaccaSoapEntry;
    }
}
