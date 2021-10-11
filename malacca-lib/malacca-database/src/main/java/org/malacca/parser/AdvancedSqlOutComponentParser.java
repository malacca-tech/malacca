package org.malacca.parser;

import cn.hutool.core.lang.Assert;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.AdvancedSqlOutComponent;
import org.malacca.definition.ComponentDefinition;
import org.malacca.utils.AESUtils;

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
@ParserInterface(type = "component", typeAlia = "poller")
public class AdvancedSqlOutComponentParser extends AdvancedComponentParser {

    public static final String AES_PWD = "1234567890ABCDEF1234567890ABCDEf";

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        AdvancedSqlOutComponent component = new AdvancedSqlOutComponent(definition.getId(), definition.getName());
        return component;
    }

    public void setUsername(AdvancedSqlOutComponent component, Object username) {
        Assert.isInstanceOf(String.class, username, "the username is not a string!");
        String usernameStr = (String) username;
        Assert.notBlank(usernameStr, "username cannot be blank");
        String decrypt = null;
        try {
            decrypt = AESUtils.decrypt(usernameStr, AES_PWD);
        } catch (Exception e) {
            Assert.state(true, "解析数据库用户失败");
        }
        component.setUsername(decrypt);
    }

    public void setPassword(AdvancedSqlOutComponent component, Object password) {
        Assert.isInstanceOf(String.class, password, "the password is not a string!");
        String passwordStr = (String) password;
        Assert.notBlank(passwordStr, "password cannot be blank");
        String decrypt = null;
        try {
            decrypt = AESUtils.decrypt(passwordStr, AES_PWD);
        } catch (Exception e) {
            Assert.state(true, "解析数据库密码失败");
        }
        component.setPassword(decrypt);
    }

}
