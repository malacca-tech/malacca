package org.malacca.utils;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/5
 * </p>
 * <p>
 * Department :
 * </p>
 *
 */

public class BeanRegistry {

    private ApplicationContext applicationContext;

    public BeanRegistry(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * @param tClass
     * @return
     */
    public Object registerBean(Class<?> tClass) {
        return registerBean(Name.getNextId(tClass), tClass);
    }

    /**
     * @param tClass
     * @param props
     * @return
     */
    public Object registerBean(Class<?> tClass, Map<String, Object> props) {
        return registerBean(Name.getNextId(tClass), tClass, props);
    }

    /**
     * @param beanName
     * @param tClass
     * @return
     */
    public Object registerBean(String beanName, Class<?> tClass) {
        return registerBean(beanName, tClass, null);
    }

    /**
     * @param beanName
     * @param tClass
     * @param props
     * @return
     */
    public Object registerBean(String beanName, Class<?> tClass, Map<String, Object> props) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(tClass);
        if (props != null) {
            for (Map.Entry<String, Object> entry : props.entrySet()) {
                builder.addPropertyValue(entry.getKey(), entry.getValue());
            }
        }
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) this.applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        beanFactory.registerBeanDefinition(beanName, builder.getRawBeanDefinition());
        return context.getBean(beanName);
    }

    public BeanHolder registerBean2(Class<?> tClass) {
        String beanName = Name.getNextId(tClass);
        Object bean = registerBean(beanName, tClass);
        BeanHolder beanHolder = new BeanHolder(beanName, bean);
        return beanHolder;
    }

    public BeanHolder registerBean2(Class<?> tClass, Map<String, Object> props) {
        String beanName = Name.getNextId(tClass);
        Object bean = registerBean(beanName, tClass, props);
        BeanHolder beanHolder = new BeanHolder(beanName, bean);
        return beanHolder;
    }

    /**
     * @param beanName
     */
    public void removeBeanDefinition(String beanName) {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        beanFactory.removeBeanDefinition(beanName);
    }
}
