package org.malacca.entry

import org.malacca.entry.HttpEntry
import org.malacca.entry.holder.HttpEntryHolder
import org.malacca.entry.register.DefaultEntryRegister
import spock.lang.Specification

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/28
 * </p>
 * <p>
 * Department :
 * </p>
 */
class EntryRegisterTests extends Specification {

    def "入口注册测试"() {
        def entryRegister = new DefaultEntryRegister()
        for (int i = 0; i < size; i++) {
            def entry = new HttpEntry()
            entry.setUri(path + i)
            entry.setMethod("get")
            entry.setId("s")
            entry.setEntryKey()
            entryRegister.registerEntry(entry)
        }

        def holder = (HttpEntryHolder) (entryRegister.getHolderMap().get(entryType))

        expect: "校验"
        assert holder.httpEntryMap.containsKey(key) == contains
        assert holder.httpEntryMap.size() == entrySize

        where: "准备数据"
        size | path    | entryType   | key       | contains | entrySize
        10   | '/test' | 'httpEntry' | '/test1'  | true     | 10
        10   | '/test' | 'httpEntry' | '/test10' | false    | 10
    }
}
