package org.malacca.component

import org.malacca.definition.ComponentDefinition
import org.malacca.definition.EntryDefinition
import org.malacca.entry.SqlPollerEntry
import org.malacca.parser.SqlEntryParser
import org.malacca.parser.SqlOutComponentParser
import spock.lang.Specification

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/4
 * </p>
 * <p>
 * Department :
 * </p>
 */
class SqlPollerEntryTests extends Specification {

    def "数据库输出"() {
        given: "开始"

        def definition = new EntryDefinition()
        definition.id = "id"
        definition.type = "db"
        definition.status = true
        definition.name = "name"
        def map = new HashMap<String, Object>()
        map.put("driverClassName", driverClassName)
        map.put("url", url)
        map.put("username", username)
        map.put("password", password)
        map.put("sql", sql)
        map.put("cron", cron)
        definition.params = map

        def parser = new SqlEntryParser()

        def component = (SqlPollerEntry) parser.createInstance(definition)

        expect: "判断"
        assert component.name == nameResult
        assert component.driverClassName == driverClassNameResult
        assert component.url == urlResult
        assert component.username == usernameResult
        assert component.password == passwordResult
        assert component.sql == sqlResult
        assert component.cron == cronResult


        where: "开始"
        driverClassName         | url                               | username | password   | sql                         | cron             | nameResult | driverClassNameResult   | urlResult                         | usernameResult | passwordResult | sqlResult                   | cronResult
        'com.mysql.jdbc.Driver' | 'jdbc:mysql://47.100.31.121:5306' | 'dev'    | '1qaz2wsX' | 'select count(1) from dual' | '0 0/1 * * * ? ' | 'name'     | 'com.mysql.jdbc.Driver' | 'jdbc:mysql://47.100.31.121:5306' | 'dev'          | '1qaz2wsX'     | 'select count(1) from dual' | '0 0/1 * * * ? '
    }
}
