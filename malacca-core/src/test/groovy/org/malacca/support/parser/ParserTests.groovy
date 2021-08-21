package org.malacca.support.parser

import com.alibaba.fastjson.JSONObject
import org.malacca.component.Component
import org.malacca.component.output.HttpOutComponent
import org.malacca.definition.ComponentDefinition
import org.malacca.definition.EntryDefinition
import org.malacca.entry.Entry
import org.malacca.entry.HttpEntry
import org.malacca.support.ClassNameParserFactory
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
class ParserTests extends Specification {

    def factory = new ClassNameParserFactory()

    def "getParser"() {
        given: "准备获取数据"
        def parser = factory.getParser(type, clazz)

        expect: "查看结果"
        assert parser instanceof HttpOutputParser == result1
        assert parser instanceof HttpInputParser == resul2

        where: "开始"
        type   | clazz           | result1 | resul2
        'http' | Entry.class     | false   | true
        'http' | Component.class | true    | false
    }

    def "获取入口组件实例"() {
        given: "准备获取实例"
        def parser = factory.getParser(type, clazz)
        def entryDefinition = new EntryDefinition()
        entryDefinition.type = type
        entryDefinition.env = null
        entryDefinition.id = entryid
        entryDefinition.status = status
        entryDefinition.name = name
        def json = new JSONObject()
        json.put(key1, value1)
        json.put(key2, value2)
        entryDefinition.params = json

        def instance = (HttpEntry) parser.createInstance(entryDefinition)

        expect: "开始校验结果"

        assert instance.name == nameResullt
        assert instance.status == statusResult
        assert instance.id == idResult
        assert instance.type == typeResult
        assert instance.method == methodResult
        assert instance.uri == uriResult

        where: "开始"
        type   | clazz       | entryid | status | name      | key1     | value1 | key2  | value2        | nameResullt | statusResult | idResult | typeResult  | methodResult | uriResult
        'http' | Entry.class | 'http1' | true   | 'http输入'  | 'method' | 'GET'  | 'uri' | '/path/test'  | 'http输入'    | true         | 'http1'  | 'httpEntry' | 'GET'        | '/path/test'
        'http' | Entry.class | 'http2' | false  | 'http输入2' | 'method' | 'GET1' | 'uri' | '/path/test1' | 'http输入2'   | false        | 'http2'  | 'httpEntry' | 'GET1'       | '/path/test1'
    }

    def "获取业务组件实例"() {
        given: "开始"
        def parser = factory.getParser(type, clazz)

        def componentDefinition = new ComponentDefinition()
        componentDefinition.type = type
        componentDefinition.env = null
        componentDefinition.id = entryid
        componentDefinition.status = status
        componentDefinition.name = name
        def params = new JSONObject()
        params.put(key1, value1)
        params.put(key2, value2)
        componentDefinition.params = params

        def instance = (HttpOutComponent) parser.createInstance(componentDefinition)

        expect: "开始校验结果"

        assert instance.name == result1
        assert instance.status == result2
        assert instance.id == result3
        assert instance.type == result4
        assert instance.timeout == result5
        assert instance.url == result6

        where: "开始"
        type   | clazz           | entryid | status | name      | key1      | value1  | key2  | value2                            | result1   | result2 | result3 | result4     | result5 | result6
        'http' | Component.class | 'http1' | true   | 'http输入'  | 'timeout' | '1000'  | 'url' | 'http://www.baidu.com/path/test'  | 'http输入'  | true    | 'http1' | 'httpOut' | 1000    | 'http://www.baidu.com/path/test'
        'http' | Component.class | 'http2' | false  | 'http输入2' | 'timeout'  | '10002' | 'url' | 'http://www.baidu.com/path/test1' | 'http输入2' | false   | 'http2' | 'httpOut' | 10002   | 'http://www.baidu.com/path/test1'
    }
}
