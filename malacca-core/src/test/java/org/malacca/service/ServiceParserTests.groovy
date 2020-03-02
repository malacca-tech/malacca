package org.malacca.flow


import org.malacca.entry.HttpEntry
import org.malacca.entry.register.DefaultEntryRegister
import org.malacca.service.DefaultServiceManager
import org.malacca.support.MessageBuilder
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
class ServiceParserTests extends Specification {

    def "服务配置解析"() {
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
        def component = componentMap.get(componentId)
        def entryMap = service.getEntryMap()
        def entry = entryMap.get(entryId)
        def flow = (DefaultFlow) service.getFlow()
        def elements = flow.getNextFlowElements(flowComponentId, MessageBuilder.withPayload("0").build())

        expect: "判断服务解析结果"
        assert componentMap.size() == componentMapSize
        assert entryMap.size() == entryMapSize
        assert component.id == componentId
        assert component.type == componentType
        assert entry.type == entryType
        assert entry.id == entryId
        assert entry instanceof HttpEntry == classType
        assert elements.size() == elementsSize
        assert elements.get(size).sufComponentId == sufComponentId
        assert elements.get(size).preComponentId == preComponentId
        assert elements.get(size).type.exceptionType == exceptionType
        assert elements.get(size).type.synchronized == isSynchronized

        where: "开始校验"
        fileName   | serviceId | componentId | entryId      | flowComponentId | componentMapSize | entryMapSize | componentType | entryType   | classType | elementsSize | size | sufComponentId | preComponentId | exceptionType | isSynchronized
        'demo.yml' | 'A001'    | 'soapOut1'  | 'component1' | 'component1'    | 2                | 2            | 'httpOut'     | 'httpEntry' | true      | 2            | 0    | 'soapOut1'     | 'component1'   | false         | false
        'demo.yml' | 'A001'    | 'soapOut1'  | 'component1' | 'component1'    | 2                | 2            | 'httpOut'     | 'httpEntry' | true      | 2            | 1    | 'soapOut2'     | 'component1'   | false         | true
        'demo.yml' | 'A001'    | 'soapOut1'  | 'component1' | 'soapOut1'      | 2                | 2            | 'httpOut'     | 'httpEntry' | true      | 1            | 0    | 'soapOut2'     | 'soapOut1'     | false         | true
        'demo.yml' | 'A001'    | 'soapOut2'  | 'component2' | 'component2'    | 2                | 2            | 'httpOut'     | 'httpEntry' | true      | 2            | 0    | 'soapOut1'     | 'component2'   | false         | true
        'demo.yml' | 'A001'    | 'soapOut2'  | 'component2' | 'component2'    | 2                | 2            | 'httpOut'     | 'httpEntry' | true      | 2            | 1    | 'soapOut2'     | 'component2'   | true          | true
    }
}
