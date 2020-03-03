package org.malacca.service;

import org.malacca.entry.register.DefaultEntryRegister;
import org.malacca.entry.register.EntryRegister;
import org.malacca.flow.DefaultFlowBuilder;
import org.malacca.flow.FlowBuilder;
import org.malacca.messaging.Message;
import org.malacca.support.ClassNameParserFactory;
import org.malacca.support.ParserFactory;

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
        super(new DefaultEntryRegister(), new ClassNameParserFactory(), new DefaultFlowBuilder());
    }

    public DefaultServiceManager(EntryRegister entryRegister, ParserFactory parserFactory, FlowBuilder defaultFlowBuilder) {
        super(entryRegister, parserFactory, defaultFlowBuilder);
    }

    @Override
    void retryFrom(String serviceId, String componentId, Message message) {

    }
}
