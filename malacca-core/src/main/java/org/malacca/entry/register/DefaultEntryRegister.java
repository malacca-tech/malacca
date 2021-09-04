package org.malacca.entry.register;

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
        initHolder();
    }

    // TODO: 2020/2/25 在初始化的时候就一应该初始化holder
    protected void initHolder() {
//        super.putHolder("httpEntry", new HttpEntryHolder());
    }

}
