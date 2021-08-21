package org.malacca.flow

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


class FlowBuilderTests extends Specification {

    def "测试拆分单行"() {
        given: "创建builder实例,并拆分单行,并拼装成element"
        def builder = new DefaultFlowBuilder()
        def flowElement = builder.parseLine(line)

        expect: "判断拆分结果"
        assert flowElement.preComponentId == pre
        assert flowElement.sufComponentId == suf
        assert flowElement.type.isSynchronized() == isSync
        assert flowElement.type.isExceptionType() == isException

        where: "准备数据"
        line            | pre | suf | isSync | isException
        'a --> b'       | 'a' | 'b' | true   | false
        'c -..-> d'     | 'c' | 'd' | false  | false
        'e -E-> f'      | 'e' | 'f' | true   | true
        'x -.E.-> y'    | 'x' | 'y' | false  | true
        '   a --> b'    | 'a' | 'b' | true   | false
        'a --> b   '    | 'a' | 'b' | true   | false
        'a    --> b'    | 'a' | 'b' | true   | false
        '   a -->    b' | 'a' | 'b' | true   | false
    }

    def "测试生成Flow"() {
        given: "创建builder实例,创建flow"
        def builder = new DefaultFlowBuilder()
        def flowStr = "component1 -.[ router:router1] .-> soapOut1\n" +
                "  component1 --> soapOut2\n" +
                "  component2 - [router:router2] -> soapOut1\n" +
                "  soapOut1 --> soapOut2\n" +
                "  component2 -E-> soapOut2"
        def flow = builder.buildFlow(flowStr, null, null)
        def elements = flow.getNextFlowElements(flowComponentId, MessageBuilder.withPayload("0").build())

        expect: "判断生成结果"
        assert elements.size() == elementsSize
        assert elements.get(size).sufComponentId == sufComponentId
        assert elements.get(size).preComponentId == preComponentId
        assert elements.get(size).type.exceptionType == exceptionType
        assert elements.get(size).type.synchronized == isSynchronized

        where: "准备数据"
        flowComponentId | elementsSize | size | preComponentId | sufComponentId | exceptionType | isSynchronized
        'component1'    | 2            | 0    | 'component1'   | 'soapOut1'     | false         | false
        'component2'    | 2            | 0    | 'component2'   | 'soapOut1'     | false         | true
        'soapOut1'      | 1            | 0    | 'soapOut1'     | 'soapOut2'     | false         | true
    }
}
