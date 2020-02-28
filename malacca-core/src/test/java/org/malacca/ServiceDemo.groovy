package org.malacca


import org.malacca.entry.register.DefaultEntryRegister
import org.malacca.service.DefaultServiceManager
import org.malacca.support.MessageBuilder
import spock.lang.Specification

import java.util.regex.Pattern

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
        serviceManager.setEntryRegister(entryRegister)
        serviceManager.loadService(serviceYml)
        println("服务加载成功")
        println("开始测试卸载")
        def service = serviceManager.getServices().get("A001")
        def httpEntry = service.getEntryMap().get("component1")
        def message = MessageBuilder.withPayload("ss").build()
       def result= httpEntry.handleMessage(message)
        println(result)
        serviceManager.unloadService("A001")
        println("卸载成功")
    }

    def "正则测试"() {
        given: "准备数据"
        def rxg = "\\[\\s*router\\s*:\\s*(\\S*?)\\s*]"
        def str = "[router:router1]"
        def p = Pattern.compile(rxg);
        def m = p.matcher(str);
        while (m.find()) {
            println("jinlail")
            println(m.group(0))
            println(m.group(1))
        }
    }
}
