package org.malacca.flow

import org.junit.jupiter.api.Test
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

//ef "test1"() {
//    given: "准备mock数据"
//
//    expect: "测试方法"
//    z == calculateService.plus(x, y)
//
//    where: "校验结果"
//    x | y || z
//    1 | 0 || 1
//    2 | 1 || 3

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
}
