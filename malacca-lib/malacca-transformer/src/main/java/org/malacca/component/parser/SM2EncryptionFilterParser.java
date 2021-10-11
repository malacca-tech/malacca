package org.malacca.component.parser;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.asymmetric.SM2Engine;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.bcprov.EncryptOutputType;
import org.malacca.component.bcprov.SM2EncryptionFilter;
import org.malacca.definition.ComponentDefinition;
import org.malacca.exception.SMFilterMessageHandlerException;
import org.malacca.parser.AdvancedComponentParser;
import org.malacca.parser.ParserInterface;
import org.springframework.util.StringUtils;

@ParserInterface(type = "component", typeAlia = "sm2Encryption")
public class SM2EncryptionFilterParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        SM2EncryptionFilter sm2Filter = new SM2EncryptionFilter(definition.getId(), definition.getName());
        return sm2Filter;
    }

    public void setPrivateKey(SM2EncryptionFilter component, Object privateKey) {
        if (!StringUtils.isEmpty(privateKey)) {
            Assert.isInstanceOf(String.class, privateKey, "the privateKey is not a string!");
            String privateStr = (String) privateKey;
            component.setPrivateKey(privateStr);
        }
    }

    public void setSm2Mode(SM2EncryptionFilter component, Object mode) {
        Assert.isInstanceOf(String.class, mode, "the mode is not a string!");
        try {
            SM2Engine.SM2Mode sm2Mode = SM2Engine.SM2Mode.valueOf((String) mode);
            component.setSm2Mode(sm2Mode);
        } catch (Exception e) {
            throw new SMFilterMessageHandlerException("不支持的加密类型: " + mode);
        }
    }

    public void setEncryptOutputType(SM2EncryptionFilter component, Object type) {
        Assert.isInstanceOf(String.class, type, "the resultType is not a string!");
        try {
            EncryptOutputType encryptOutputType = EncryptOutputType.valueOf((String) type);
            component.setEncryptOutputType(encryptOutputType);
        } catch (Exception e) {
            throw new SMFilterMessageHandlerException("不支持的返回类型: " + type);
        }
    }
}
