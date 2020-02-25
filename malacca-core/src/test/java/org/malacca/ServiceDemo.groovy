package org.malacca

import org.malacca.entry.Entry
import org.malacca.entry.holder.HttpEntryHolder
import org.malacca.entry.register.DefaultEntryRegister
import org.malacca.service.DefaultServiceManager
import spock.lang.Specification

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
class ServiceDemo extends Specification {

    def "服务配置测试"() {
        given: "准备数据"
        def inputStream = ServiceDemo.class.getClassLoader().getResourceAsStream("demo.yml")
        def scanner = new Scanner(inputStream).useDelimiter("\\A");
        def serviceYml = scanner.hasNext() ? scanner.next() : ""
        def serviceManager = new DefaultServiceManager()
        def entryRegister = new DefaultEntryRegister()
        entryRegister.putHolder("httpInput", new HttpEntryHolder() {

            @Override
            void unloadEntry(String id, Entry entry) {

            }
        })
        serviceManager.setEntryRegister(entryRegister)
        serviceManager.loadService(serviceYml)
        println("服务加载成功")
        println("开始测试卸载")
        serviceManager.unloadService("A001")
        println("卸载成功")
    }
}
