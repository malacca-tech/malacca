package org.malacca.component.parser;

import cn.hutool.core.lang.Assert;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.component.limiter.RateLimiterFilter;
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
 * Author :yangxing 2021/5/18
 * </p>
 * <p>
 * Department :
 * </p>
 */
@ParserInterface(type = "component", typeAlia = "rateLimiterFilter")
public class RateLimiterFilterParser extends AdvancedComponentParser {

    private static final String PERMITS_KEY = "permits";
    private static final String SECONDS_KEY = "seconds";

    @Override
    public AbstractAdvancedComponent doCreateInstance(ComponentDefinition definition) {
        RateLimiterFilter rateLimiterFilter = new RateLimiterFilter(definition.getId(), definition.getName());
        Map<String, Object> params = definition.getParams();
        setRateLimiter(rateLimiterFilter, params);
        return rateLimiterFilter;
    }

    public void setRateLimiter(RateLimiterFilter component, Map<String, Object> params) {
        int permits = component.getPermits();
        Object permitsObj = params.get(PERMITS_KEY);
        if (null != permitsObj) {
            Assert.isInstanceOf(String.class, permitsObj, "the permits is not a string!");
            String permitsStr = (String) permitsObj;
            try {
                permits = Integer.parseInt(permitsStr);
                Assert.state(permits > 0, "permits must be positive!");
            } catch (Exception e) {
                Assert.state(false, "the permits is not a int format!");
            }
        }

        int seconds = component.getSeconds();
        Object secondsObj = params.get(SECONDS_KEY);
        if (null != secondsObj) {
            Assert.isInstanceOf(String.class, secondsObj, "the seconds is not a string!");
            String secondsStr = (String) secondsObj;
            try {
                seconds = Integer.parseInt(secondsStr);
                Assert.state(seconds > 0, "seconds must be positive!");
            } catch (Exception e) {
                Assert.state(false, "the seconds is not a int format!");
            }
        }
        component.setRateLimiter(permits, seconds);
    }
}

