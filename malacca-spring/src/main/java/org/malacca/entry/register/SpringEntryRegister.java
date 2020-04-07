package org.malacca.entry.register;

import org.malacca.entry.Entry;
import org.malacca.entry.holder.CommonHttpEntryHolder;
import org.malacca.entry.holder.EntryHolder;
import org.malacca.entry.holder.HttpEntryHolder;
import org.malacca.entry.holder.PollerEntryHolder;
import org.malacca.exception.ServiceLoadException;
import org.malacca.exception.constant.SystemExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/4
 * </p>
 * <p>
 * Department :
 * </p>
 */
//@Component
public class SpringEntryRegister extends AbstractEntryRegister implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(SpringEntryRegister.class);

    @Autowired
    private PollerEntryHolder pollerEntryHolder;

    @Override
    public void registerEntry(Entry entry) {
        LOG.info("开始注册Entry{},Entry标识为{}", entry.getType(), entry.getEntryKey());
        EntryHolder entryHolder = getHolderMap().get(entry.getType());
        if (entryHolder != null) {
            try {
                entryHolder.loadEntry(entry.getEntryKey(), entry);
            } catch (ServiceLoadException e) {
                throw new ServiceLoadException(SystemExceptionCode.MALACCA_100002_ENTRY_REGISTER_ERROR, e.getMessage(), e);
            } catch (Exception e) {
                throw new ServiceLoadException(SystemExceptionCode.MALACCA_100002_ENTRY_REGISTER_ERROR, e.getMessage(), e);
            }
        } else {
            LOG.warn("没有找到注册{}类型的EntryHolder", entry.getType(), entry.getEntryKey());
        }
        LOG.info("注册Entry{},Entry标识为{} 成功", entry.getType(), entry.getEntryKey());
    }

    @Override
    public void deregisterEntry(Entry entry) {
        LOG.info("开始卸载Entry{},Entry标识为{}", entry.getType(), entry.getEntryKey());
        EntryHolder entryHolder = getHolderMap().get(entry.getType());
        if (entryHolder != null) {
            try {
                entryHolder.unloadEntry(entry.getEntryKey(), entry);
            } catch (Exception e) {
                throw new ServiceLoadException(SystemExceptionCode.MALACCA_100004_ENTRY_DEREGISTER_ERROR, e.getMessage(), e);
            }
        } else {
            LOG.warn("没有找到注册{}类型的EntryHolder", entry.getType(), entry.getEntryKey());
        }
        LOG.info("卸载Entry{},Entry标识为{} 成功", entry.getType(), entry.getEntryKey());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO: 2020/3/4 初始化holder
        putHolder("pollerEntry", pollerEntryHolder);
        putHolder("httpEntry", new CommonHttpEntryHolder());
    }
}
