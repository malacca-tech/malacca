package org.malacca.entry;

import org.malacca.exception.MessagingException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.boot.actuate.endpoint.web.servlet.ControllerEndpointHandlerMapping;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/9/16
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class HttpMapping extends RequestMappingHandlerMapping implements ApplicationListener<ContextRefreshedEvent> {
    private static final Method HANDLE_REQUEST_METHOD = ReflectionUtils.findMethod(HttpRequestHandler.class, "handleRequest", new Class[]{HttpServletRequest.class, HttpServletResponse.class});
    private final AtomicBoolean initialized = new AtomicBoolean(true);
    private RequestMappingHandlerMapping controllerEndpointHandlerMapping;

    public HttpMapping(RequestMappingHandlerMapping controllerEndpointHandlerMapping) {
        this.controllerEndpointHandlerMapping = controllerEndpointHandlerMapping;
    }

    public Object postProcessBeforeInitialization(Object bean, SpringHttpEntry entry, String beanName) throws BeansException {
        if (this.initialized.get() && this.isHandler(bean.getClass())) {
            this.detectHandlerMethods(bean, entry);
        }

        return bean;
    }

    public void postProcessBeforeDestruction(Object bean, SpringHttpEntry entry, String beanName) throws BeansException {
        if (this.isHandler(bean.getClass())) {
            controllerEndpointHandlerMapping.unregisterMapping(this.getMappingForEndpoint(entry));
        }
    }


    public boolean requiresDestruction(Object bean) {
        return this.isHandler(bean.getClass());
    }

    protected boolean isHandler(Class<?> beanType) {
        return HttpRequestHandler.class.isAssignableFrom(beanType);
    }

    protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Object bean = handlerMethod.getBean();
            if (bean instanceof SpringHttpEntry) {
                handler = bean;
            }
        }

        return super.getHandlerExecutionChain(handler, request);
    }

    protected CorsConfiguration getCorsConfiguration(Object handler, HttpServletRequest request) {
        return handler instanceof HandlerMethod ? super.getCorsConfiguration(handler, request) : super.getCorsConfiguration(new HandlerMethod(handler, HANDLE_REQUEST_METHOD), request);
    }

    protected void detectHandlerMethods(Object handler, SpringHttpEntry entry) {
        if (handler instanceof String) {
            handler = this.getApplicationContext().getBean((String) handler);
        }

        RequestMappingInfo mapping = this.getMappingForEndpoint(entry);
        if (mapping != null) {
            List<HandlerMethod> list = controllerEndpointHandlerMapping.getHandlerMethodsForMappingName(entry.getUri());
            if (list == null) {
                controllerEndpointHandlerMapping.registerMapping(mapping, handler, HANDLE_REQUEST_METHOD);
            } else {
                throw new MessagingException(entry.getUri() + " 已注册，请检查配置");
            }
        }

    }

    private RequestMappingInfo getMappingForEndpoint(SpringHttpEntry endpoint) {
        if (ObjectUtils.isEmpty(endpoint.getId())) {
            return null;
        } else {
            Map<String, Object> requestMappingAttributes = new HashMap();
            requestMappingAttributes.put("name", endpoint.getUri());
            requestMappingAttributes.put("value", endpoint.getUri());
            requestMappingAttributes.put("path", endpoint.getUri());
            requestMappingAttributes.put("method", new RequestMethod[]{RequestMethod.valueOf(endpoint.getMethod())});
            org.springframework.web.bind.annotation.RequestMapping requestMappingAnnotation = (org.springframework.web.bind.annotation.RequestMapping) AnnotationUtils.synthesizeAnnotation(requestMappingAttributes, org.springframework.web.bind.annotation.RequestMapping.class, (AnnotatedElement) null);
            return this.createRequestMappingInfo(requestMappingAnnotation, this.getCustomTypeCondition(endpoint.getClass()));
        }
    }

    public void afterPropertiesSet() {
        this.initialized.set(true);
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!this.initialized.getAndSet(true)) {
            super.afterPropertiesSet();
        }

    }
}
