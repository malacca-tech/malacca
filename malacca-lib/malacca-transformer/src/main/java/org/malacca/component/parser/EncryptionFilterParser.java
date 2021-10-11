package org.malacca.component.parser;

import org.malacca.component.filter.AbstractCryptographyFilter;
import org.malacca.component.filter.EncryptionFilter;
import org.malacca.definition.ComponentDefinition;
import org.malacca.parser.ParserInterface;

@ParserInterface(type = "component", typeAlia = "encryptionFilter")
public class EncryptionFilterParser extends AbstractCryptographyFilterParser {

    @Override
    protected AbstractCryptographyFilter getAbstractCryptographyFilter(ComponentDefinition definition) {
        return new EncryptionFilter(definition.getId(), definition.getName());
    }

    public void setHeadersExprList(EncryptionFilter component, Object headersExprList) {
        super.setHeadersExprList(component, headersExprList);
    }

    public void setParamsExprList(EncryptionFilter component, Object paramsExprList) {
        super.setParamsExprList(component, paramsExprList);
    }

    public void setBodyExprList(EncryptionFilter component, Object bodyExprList) {
        super.setBodyExprList(component, bodyExprList);
    }

    public void setBodyType(EncryptionFilter component, Object bodyType) {
        super.setBodyType(component, bodyType);
    }

    public void setEncryptType(EncryptionFilter component, Object encryptType) {
        super.setEncryptType(component, encryptType);
    }

    public void setMode(EncryptionFilter component, Object mode) {
        super.setMode(component, mode);
    }

    public void setPassword(EncryptionFilter component, Object password) {
        super.setPassword(component, password);
    }

    public void setBadPadding(EncryptionFilter component, Object badPadding) {
        super.setBadPadding(component, badPadding);
    }

    public void setIv(EncryptionFilter component, Object iv) {
        super.setIv(component, iv);
    }

    public void setOutputType(EncryptionFilter component, Object outputType) {
        super.setOutputType(component, outputType);
    }
}
