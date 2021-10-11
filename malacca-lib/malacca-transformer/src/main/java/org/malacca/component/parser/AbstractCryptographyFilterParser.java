package org.malacca.component.parser;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.filter.AbstractCryptographyFilter;
import org.malacca.definition.ComponentDefinition;
import org.malacca.parser.AdvancedComponentParser;
import org.malacca.support.helper.AesHelper;

import java.util.List;

public abstract class AbstractCryptographyFilterParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        AbstractCryptographyFilter encryptionFilter = getAbstractCryptographyFilter(definition);
        return encryptionFilter;
    }

    abstract protected AbstractCryptographyFilter getAbstractCryptographyFilter(ComponentDefinition definition);

    protected void setHeadersExprList(AbstractCryptographyFilter component, Object headersExprList) {
        if (null != headersExprList && !StrUtil.isBlankIfStr(headersExprList)) {
            Assert.isInstanceOf(List.class, headersExprList, "this headersExprList is not a list!");
            List<String> headers = (List<String>) headersExprList;
            component.setHeadersExprList(headers);
        }
    }

    protected void setParamsExprList(AbstractCryptographyFilter component, Object paramsExprList) {
        if (null != paramsExprList && !StrUtil.isBlankIfStr(paramsExprList)) {
            Assert.isInstanceOf(List.class, paramsExprList, "this paramsExprList is not a list!");
            List<String> params = (List<String>) paramsExprList;
            component.setParamsExprList(params);
        }
    }

    protected void setBodyExprList(AbstractCryptographyFilter component, Object bodyExprList) {
        if (null != bodyExprList && !StrUtil.isBlankIfStr(bodyExprList)) {
            Assert.isInstanceOf(List.class, bodyExprList, "this bodyExprList is not a list!");
            List<String> bodyExprs = (List<String>) bodyExprList;
            component.setBodyExprList(bodyExprs);
        }
    }

    protected void setBodyType(AbstractCryptographyFilter component, Object bodyType) {
        if (null != bodyType) {
            Assert.isInstanceOf(String.class, bodyType, "the bodyType is not a string!");
            String bodyTypeStr = (String) bodyType;
            component.setBodyType(bodyTypeStr);
        }
    }

    protected void setEncryptType(AbstractCryptographyFilter component, Object encryptType) {
        Assert.isInstanceOf(String.class, encryptType, "the encryptType is not a string!");
        String entryTypeStr = (String) encryptType;
        Assert.notBlank(entryTypeStr, "entryType cannot be blank");
        component.setEncryptType(entryTypeStr);
    }

    protected void setMode(AbstractCryptographyFilter component, Object mode) {
        Assert.isInstanceOf(String.class, mode, "the mode is not a string!");
        String modeStr = (String) mode;
        Assert.notBlank(modeStr, "mode cannot be blank");
        component.setMode(AesHelper.AesMode.valueOf(modeStr));
    }

    protected void setPassword(AbstractCryptographyFilter component, Object password) {
        Assert.isInstanceOf(String.class, password, "the password is not a string!");
        String passwordStr = (String) password;
        Assert.notBlank(passwordStr, "password cannot be blank");
        component.setPassword(passwordStr);
    }

    protected void setBadPadding(AbstractCryptographyFilter component, Object badPadding) {
        Assert.isInstanceOf(String.class, badPadding, "the badPadding is not a string!");
        String badPaddingStr = (String) badPadding;
        Assert.notBlank(badPaddingStr, "badPadding cannot be blank");
        component.setBadPadding(AesHelper.BadPadding.valueOf(badPaddingStr));
    }

    protected void setIv(AbstractCryptographyFilter component, Object iv) {
        if (null != iv) {
            Assert.isInstanceOf(String.class, iv, "the iv is not a string!");
            String ivStr = (String) iv;
            component.setIv(ivStr);
        }
    }

    protected void setOutputType(AbstractCryptographyFilter component, Object outputType) {
        Assert.isInstanceOf(String.class, outputType, "the outputType is not a string!");
        String outputTypeStr = (String) outputType;
        Assert.notBlank(outputTypeStr, "outputType cannot be blank");
        component.setOutputType(AesHelper.OutputType.valueOf(outputTypeStr));
    }

}
