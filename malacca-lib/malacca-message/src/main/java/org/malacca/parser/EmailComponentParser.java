package org.malacca.parser;

import cn.hutool.core.lang.Assert;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.email.EmailComponent;
import org.malacca.definition.ComponentDefinition;
import org.springframework.util.StringUtils;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :yangxing 2021/8/17
 * </p>
 * <p>
 * Department :
 * </p>
 */
@ParserInterface(type = "component", typeAlia = "email")
public class EmailComponentParser extends AdvancedComponentParser {

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        EmailComponent emailComponent = new EmailComponent(definition.getId(), definition.getName());
        return emailComponent;
    }

    public void setCcUser(EmailComponent component, Object ccUser) {
        if (!StringUtils.isEmpty(ccUser)) {
            Assert.isInstanceOf(String.class, ccUser, "the ccUser is not a string!");
            String ccUserStr = (String) ccUser;
            component.setCcUser(ccUserStr);
        }
    }

    public void setBccUser(EmailComponent component, Object bccUser) {
        if (!StringUtils.isEmpty(bccUser)) {
            Assert.isInstanceOf(String.class, bccUser, "the bccUser is not a string!");
            String bccUserStr = (String) bccUser;
            component.setBccUser(bccUserStr);
        }
    }

}

