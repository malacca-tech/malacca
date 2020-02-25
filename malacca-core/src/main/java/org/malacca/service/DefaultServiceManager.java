package org.malacca.service;

import org.malacca.entry.register.DefaultEntryRegister;
import org.malacca.entry.register.EntryRegister;
import org.malacca.messaging.Message;
import org.malacca.support.ClassNameParserFactory;
import org.malacca.support.ParserFactory;

import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/24
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class DefaultServiceManager extends AbstractServiceManager {

    public DefaultServiceManager() {
        //todo 这个entryRegister也是同理，弄一个default的entryRegister
        super(new DefaultEntryRegister(), new ClassNameParserFactory());
    }

    protected DefaultServiceManager(EntryRegister entryRegister, ParserFactory parserFactory) {
        super(entryRegister, parserFactory);
    }

    @Override
    void retryFrom(String serviceId, String componentId, Message message) {

    }
}
