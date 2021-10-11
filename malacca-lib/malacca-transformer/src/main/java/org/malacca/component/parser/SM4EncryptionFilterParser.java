package org.malacca.component.parser;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.bcprov.EncryptOutputType;
import org.malacca.component.bcprov.SM4EncryptionFilter;
import org.malacca.definition.ComponentDefinition;
import org.malacca.exception.SMFilterMessageHandlerException;
import org.malacca.parser.AdvancedComponentParser;
import org.malacca.parser.ParserInterface;

@ParserInterface(type = "component", typeAlia = "sm4Encryption")
public class SM4EncryptionFilterParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        SM4EncryptionFilter sm2Filter = new SM4EncryptionFilter(definition.getId(), definition.getName());
        return sm2Filter;
    }

    public void setMode(SM4EncryptionFilter component, Object mode) {
        Assert.isInstanceOf(String.class, mode, "the mode is not a string!");
        try {
            Mode modeObj = Mode.valueOf((String) mode);
            component.setMode(modeObj);
        } catch (Exception e) {
            throw new SMFilterMessageHandlerException("不支持的模式: " + mode);
        }
    }

    public void setPadding(SM4EncryptionFilter component, Object padding) {
        Assert.isInstanceOf(String.class, padding, "the padding is not a string!");
        try {
            Padding paddingObj = Padding.valueOf((String) padding);
            component.setPadding(paddingObj);
        } catch (Exception e) {
            throw new SMFilterMessageHandlerException("不支持的补码方式: " + padding);
        }
    }

    public void setEncryptOutputType(SM4EncryptionFilter component, Object type) {
        Assert.isInstanceOf(String.class, type, "the encryptOutputType is not a string!");
        try {
            EncryptOutputType encryptOutputType = EncryptOutputType.valueOf((String) type);
            component.setEncryptOutputType(encryptOutputType);
        } catch (Exception e) {
            throw new SMFilterMessageHandlerException("不支持的返回类型: " + type);
        }
    }
}
