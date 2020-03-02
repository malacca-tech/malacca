package org.malacca.entry

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
 * Author :chensheng 2020/3/2
 * </p>
 * <p>
 * Department :
 * </p>
 */
class CommonHttpEntryTests extends Specification {

    def "测试通用的http入口"() {
        given: "开始了"

        def entryRegister = new DefaultEntryRegister()
        def entry = new CommonHttpEntry("ss", "name")
        entry.setMethod("GET")
        entry.setPort(8889)
        entry.setUri("/path/test")
        entry.setEntryKey()
        entryRegister.registerEntry(entry)
    }
}
