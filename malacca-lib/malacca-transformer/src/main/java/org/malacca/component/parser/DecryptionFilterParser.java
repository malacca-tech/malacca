package org.malacca.component.parser;

import org.malacca.component.filter.AbstractCryptographyFilter;
import org.malacca.component.filter.DecryptionFilter;
import org.malacca.definition.ComponentDefinition;
import org.malacca.parser.ParserInterface;

@ParserInterface(type = "component", typeAlia = "decryptionFilter")
public class DecryptionFilterParser extends AbstractCryptographyFilterParser {

    @Override
    protected AbstractCryptographyFilter getAbstractCryptographyFilter(ComponentDefinition definition) {
        return new DecryptionFilter(definition.getId(), definition.getName());
    }

    public void setHeadersExprList(DecryptionFilter component, Object headersExprList) {
        super.setHeadersExprList(component, headersExprList);
    }

    public void setParamsExprList(DecryptionFilter component, Object paramsExprList) {
        super.setParamsExprList(component, paramsExprList);
    }

    public void setBodyExprList(DecryptionFilter component, Object bodyExprList) {
        super.setBodyExprList(component, bodyExprList);
    }

    public void setBodyType(DecryptionFilter component, Object bodyType) {
        super.setBodyType(component, bodyType);
    }

    public void setEncryptType(DecryptionFilter component, Object encryptType) {
        super.setEncryptType(component, encryptType);
    }

    public void setMode(DecryptionFilter component, Object mode) {
        super.setMode(component, mode);
    }

    public void setPassword(DecryptionFilter component, Object password) {
        super.setPassword(component, password);
    }

    public void setBadPadding(DecryptionFilter component, Object badPadding) {
        super.setBadPadding(component, badPadding);
    }

    public void setIv(DecryptionFilter component, Object iv) {
        super.setIv(component, iv);
    }

    public void setOutputType(DecryptionFilter component, Object outputType) {
        super.setOutputType(component, outputType);
    }
}
