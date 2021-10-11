package org.malacca.component.parser;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.bcprov.SM4DecryptionFilter;
import org.malacca.definition.ComponentDefinition;
import org.malacca.exception.SMFilterMessageHandlerException;
import org.malacca.parser.AdvancedComponentParser;
import org.malacca.parser.ParserInterface;

@ParserInterface(type = "component", typeAlia = "sm4Decryption")
public class SM4DecryptionFilterParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        SM4DecryptionFilter sm2Filter = new SM4DecryptionFilter(definition.getId(), definition.getName());
        return sm2Filter;
    }

    public void setMode(SM4DecryptionFilter component, Object mode) {
        Assert.isInstanceOf(String.class, mode, "the mode is not a string!");
        try {
            Mode modeObj = Mode.valueOf((String) mode);
            component.setMode(modeObj);
        } catch (Exception e) {
            throw new SMFilterMessageHandlerException("不支持的模式: " + mode);
        }
    }

    public void setPadding(SM4DecryptionFilter component, Object padding) {
        Assert.isInstanceOf(String.class, padding, "the padding is not a string!");
        try {
            Padding paddingObj = Padding.valueOf((String) padding);
            component.setPadding(paddingObj);
        } catch (Exception e) {
            throw new SMFilterMessageHandlerException("不支持的补码方式: " + padding);
        }
    }

}
