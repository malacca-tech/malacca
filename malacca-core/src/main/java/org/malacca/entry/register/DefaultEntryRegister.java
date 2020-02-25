package org.malacca.entry.register;

import org.malacca.entry.Entry;
import org.malacca.entry.holder.HttpEntryHolder;

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
public class DefaultEntryRegister extends AbstractEntryRegister {

    public DefaultEntryRegister() {
        super();
    }

    protected void initHolder(){
        super.putHolder("http", new HttpEntryHolder() {
            @Override
            public void unloadEntry(String id, Entry entry) {

            }
        });
    }
}
