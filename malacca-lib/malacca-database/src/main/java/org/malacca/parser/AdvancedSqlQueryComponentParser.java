package org.malacca.parser;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.AdvancedSqlQueryComponent;
import org.malacca.component.procedure.ProcedureParam;
import org.malacca.definition.ComponentDefinition;
import org.malacca.utils.AESUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
@ParserInterface(type = "component", typeAlia = "dbQuery")
public class AdvancedSqlQueryComponentParser extends AdvancedComponentParser {

    public static final String AES_PWD = "1234567890ABCDEF1234567890ABCDEf";

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        AdvancedSqlQueryComponent component = new AdvancedSqlQueryComponent(definition.getId(), definition.getName());
        component.setStatus(definition.isStatus());
        component.setEnv(definition.getEnv());
        return component;
    }

    public void setUsername(AdvancedSqlQueryComponent component, Object username) {
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

    public void setPassword(AdvancedSqlQueryComponent component, Object password) {
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

    public void setParamList(AdvancedSqlQueryComponent component, Object paramList) {
        if (null != paramList) {
            Assert.isInstanceOf(List.class, paramList, "the paramList is not a list!");
            List<ProcedureParam> params = new ArrayList<>();
            for (Object param : (List<Object>) paramList) {
                Assert.isInstanceOf(Map.class, param, "the param is not a map!");
                ProcedureParam procedureParam = BeanUtil.mapToBean((Map<String, String>) param, ProcedureParam.class, false);
                params.add(procedureParam);
            }
            component.setParamList(params);
        }
    }

    public void setProcedureName(AdvancedSqlQueryComponent component, Object procedureName) {
        if (!StringUtils.isEmpty(procedureName)) {
            Assert.isInstanceOf(String.class, procedureName, "the procedureName is not a list");
            String procedureNameStr = (String) procedureName;
            component.setProcedureName(procedureNameStr);
        }
    }

}
