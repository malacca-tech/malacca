package org.malacca.parser;

import cn.hutool.core.lang.Assert;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.DingTalkComponent;
import org.malacca.definition.ComponentDefinition;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2021/4/6
 * </p>
 * <p>
 * Department :
 * </p>
 */
@ParserInterface(type = "component", typeAlia = "dingTalk")
public class DingTalkComponentParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        DingTalkComponent dingTalkComponent = new DingTalkComponent(definition.getId(), definition.getName());
        return dingTalkComponent;
    }

    public void setToparty(DingTalkComponent component, Object topart) {
        Assert.isInstanceOf(String.class, topart, "the topart is not a string!");
        String toPartStr = (String) topart;
        component.setToparty(toPartStr);
    }

    public void setTotag(DingTalkComponent component, Object totag) {
        Assert.isInstanceOf(String.class, totag, "the totag is not a string!");
        String toTagUrl = (String) totag;
        component.setTotag(toTagUrl);
    }

    public void setUrl(DingTalkComponent component, Object url) {
        Assert.isInstanceOf(String.class, url, "the url is not a string!");
        String urlStr = (String) url;
        component.setUrl(urlStr);
    }

    public void setDingtalkConfig(DingTalkComponent component, Object dingtalkConfig) {

    }
}