package org.malacca.component.parser;

import cn.hutool.core.lang.Assert;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.bcprov.EncryptOutputType;
import org.malacca.component.bcprov.SM3EncryptionFilter;
import org.malacca.definition.ComponentDefinition;
import org.malacca.exception.SMFilterMessageHandlerException;
import org.malacca.parser.AdvancedComponentParser;
import org.malacca.parser.ParserInterface;

@ParserInterface(type = "component", typeAlia = "sm3Encryption")
public class SM3EncryptionFilterParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        SM3EncryptionFilter sm2Filter = new SM3EncryptionFilter(definition.getId(), definition.getName());
        return sm2Filter;
    }

    public void setEncryptOutputType(SM3EncryptionFilter component, Object type) {
        Assert.isInstanceOf(String.class, type, "the encryptOutputType is not a string!");
        try {
            EncryptOutputType encryptOutputType = EncryptOutputType.valueOf((String) type);
            component.setEncryptOutputType(encryptOutputType);
        } catch (Exception e) {
            throw new SMFilterMessageHandlerException("不支持的返回类型: " + type);
        }
    }
}
