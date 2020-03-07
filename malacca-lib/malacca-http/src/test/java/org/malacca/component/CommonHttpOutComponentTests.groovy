package org.malacca.component

import org.malacca.definition.ComponentDefinition
import org.malacca.parser.CommonHttpOutComponentParser
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
class CommonHttpOutComponentTests extends Specification {

    def "http输出组件"() {
        given: "开始了"
        def definition = new ComponentDefinition()
        definition.id = "component"
        definition.type = "http"
        definition.status = true
        definition.name = "http输出"
        def map = new HashMap<String, Object>()
        map.put("method", method)
        map.put("url", url)
        def headers = new HashMap<String, String>()
        headers.put(key1, value1)
        map.put("headers", headers)
        def parameters = new HashMap<String, String>()
        parameters.put(key2, value2)
        map.put("headers", headers)
        map.put("parameters", parameters)
        map.put("mediaType", mediaType)
        map.put("timeout", timeout)
        definition.params = map

        def parser = new CommonHttpOutComponentParser()
        def component = (CommonHttpOutComponent) parser.createInstance(definition)

        expect: "开始判断"
        assert component.name == nameResult
        assert component.headers.get(key1) == headersResult
        assert component.url == urlResult
        assert component.timeout == timeoutResult
        assert component.parameters.get(key2) == parametersResult

        where: "开始赋值"
        method | url                    | key1       | value1      | key2 | value2 | mediaType          | timeout | nameResult | headersResult | urlResult              | timeoutResult | parametersResult
        'GET'  | 'http://www.baidu.com' | "username" | 'chensheng' | 'id' | '33'   | 'application/json' | '2000'  | 'http输出'   | 'chensheng'   | 'http://www.baidu.com' | 2000          | '33'
    }
}
