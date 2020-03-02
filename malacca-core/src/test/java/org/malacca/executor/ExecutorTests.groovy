package org.malacca.flow

import com.alibaba.fastjson.JSONObject
import org.malacca.entry.register.DefaultEntryRegister
import org.malacca.exector.DefaultFlowExecutor
import org.malacca.service.DefaultServiceManager
import org.malacca.support.MessageBuilder
import spock.lang.Specification

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

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
class ExecutorTests extends Specification {

    def "执行器测试"() {
        given: "准备数据"
        def inputStream = ServiceParserTests.class.getClassLoader().getResourceAsStream(fileName)
        def scanner = new Scanner(inputStream).useDelimiter("\\A");
        def serviceYml = scanner.hasNext() ? scanner.next() : ""
        def serviceManager = new DefaultServiceManager()
        def entryRegister = new DefaultEntryRegister()
        serviceManager.setEntryRegister(entryRegister)
        serviceManager.loadService(serviceYml)

        def service = serviceManager.getServices().get(serviceId)
        def componentMap = service.getComponentMap()
        def flow = (DefaultFlow) service.getFlow()
        def threadExecutor = new ThreadPoolExecutor(5, 200,
                10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000))

        def executor = new DefaultFlowExecutor(flow, threadExecutor, componentMap)
        def payload = executor.execute(componentId, MessageBuilder.withPayload("1").build()).payload
        if (payload instanceof JSONObject) {
            payload = JSONObject.toJSONString(payload)
        }
        println(payload)

        expect: "判断规则"
        assert payload == result

        where: "准备数据"
        fileName   | serviceId | componentId  | result
        'demo.yml' | 'A001'    | 'soapOut1'   | '1'
        'demo.yml' | 'A001'    | 'component1' | '{"code":"1"}'
        'demo.yml' | 'A001'    | 'component2' | '{"code":"1"}'
    }
}
