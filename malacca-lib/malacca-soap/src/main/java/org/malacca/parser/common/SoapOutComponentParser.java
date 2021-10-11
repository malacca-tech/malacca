package org.malacca.parser.common;

import cn.hutool.core.lang.Assert;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.common.SoapOutComponent;
import org.malacca.definition.ComponentDefinition;
import org.malacca.parser.AdvancedComponentParser;
import org.malacca.parser.ParserInterface;

import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/13
 * </p>
 * <p>
 * Department :
 * </p>
 */
@ParserInterface(type = "component", typeAlia = "soap")
public class SoapOutComponentParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        SoapOutComponent component = new SoapOutComponent(definition.getId(), definition.getName());
        return component;
    }

    public void setResultNodeName(SoapOutComponent component, Object resultNodeName) {
        Assert.isInstanceOf(String.class, resultNodeName, "resultNodeName url is not a string!");
        String resultNodeNameStr = (String) resultNodeName;
        component.setResultNodeName(resultNodeNameStr);
    }

    public void setMode(SoapOutComponent component, Object mode) {
        if (mode != null) {
            Assert.isInstanceOf(String.class, mode, "mode url is not a string!");
            String modeNameStr = (String) mode;
            Assert.notBlank(modeNameStr, "mode cannot be blank");
            component.setMode(modeNameStr);
        }
    }

    public void setNeedAction(SoapOutComponent component, Object needAction) {
        if (needAction != null) {
            Assert.isInstanceOf(Boolean.class, needAction, "needAction url is not a boolen!");
            Boolean needActionBoolean = (Boolean) needAction;
            component.setNeedAction(needActionBoolean);
        }
    }

    public void setSoapAction(SoapOutComponent component, Object soapAction) {
        if (soapAction != null) {
            Assert.isInstanceOf(String.class, soapAction, "soapAction url is not a string!");
            String soapActionStr = (String) soapAction;
            Assert.notBlank(soapActionStr, "soapAction cannot be blank");
            component.setSoapAction(soapActionStr);
        }
    }

    public void setTimeout(SoapOutComponent component, Object timeout) {
        Assert.isInstanceOf(String.class, timeout, "the timeout is not a string!");
        String timeoutStr = (String) timeout;
        Assert.state(isNumber(timeoutStr), "the timeout is not a number format!");
        Integer timeoutInt = Integer.valueOf((String) timeoutStr);
        component.setTimeout(timeoutInt);
    }

    public void setParameters(SoapOutComponent component, Object params) {
        if (params == null) {
            return;
        }
        if (Map.class.isAssignableFrom(params.getClass())) {
            component.setParams((Map<String, String>) params);
        } else {
            Assert.state(true, "params is not collect format");
        }
    }

    private boolean isNumber(String str) {
        try {
            Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
