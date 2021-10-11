package org.malacca.parser;

import cn.hutool.core.lang.Assert;
import org.malacca.definition.EntryDefinition;
import org.malacca.entry.AbstractAdvancedEntry;
import org.malacca.entry.AdvancedSqlPollerEntry;
import org.malacca.utils.AESUtils;

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
public class AdvancedSqlEntryParser extends AdvancedEntryParser {

    public static final String AES_PWD = "1234567890ABCDEF1234567890ABCDEf";

    @Override
    public AbstractAdvancedEntry doCreateInstance(EntryDefinition definition) {
        AdvancedSqlPollerEntry entry = new AdvancedSqlPollerEntry(definition.getId(), definition.getName());
        return entry;
    }

    public void setUsername(AdvancedSqlPollerEntry entry, Object username) {
        Assert.isInstanceOf(String.class, username, "the username is not a string!");
        String usernameStr = (String) username;
        Assert.notBlank(usernameStr, "username cannot be blank");
        String decrypt = null;
        try {
            decrypt = AESUtils.decrypt(usernameStr, AES_PWD);
        } catch (Exception e) {
            Assert.state(true,"解析数据库用户失败");
        }
        entry.setUsername(decrypt);
    }

    public void setPassword(AdvancedSqlPollerEntry entry, Object password) {
        Assert.isInstanceOf(String.class, password, "the password is not a string!");
        String passwordStr = (String) password;
        Assert.notBlank(passwordStr, "password cannot be blank");
        String decrypt = null;
        try {
            decrypt = AESUtils.decrypt(passwordStr, AES_PWD);
        } catch (Exception e) {
            Assert.state(true,"解析数据库密码失败");
        }
        entry.setPassword(decrypt);
    }

}
