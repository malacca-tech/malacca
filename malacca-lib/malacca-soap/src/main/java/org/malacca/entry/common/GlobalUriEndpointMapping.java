package org.malacca.entry.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContextException;
import org.springframework.ws.server.endpoint.mapping.UriEndpointMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/4/3
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class GlobalUriEndpointMapping extends UriEndpointMapping {

    private boolean lazyInitEndpoints = false;
    private final Map<String, Object> endpointMap = new HashMap();

    public void setLazyInitEndpoints(boolean lazyInitEndpoints) {
        this.lazyInitEndpoints = lazyInitEndpoints;
    }

    protected Object lookupEndpoint(String key) {
        return this.endpointMap.get(key);
    }

    @Override
    protected void registerEndpoint(String key, Object endpoint) throws BeansException {
        Object mappedEndpoint = this.endpointMap.get(key);
        if (mappedEndpoint != null) {
            throw new ApplicationContextException("Cannot map endpoint [" + endpoint + "] on registration key [" + key + "]: there's already endpoint [" + mappedEndpoint + "] mapped");
        } else {
            if (!this.lazyInitEndpoints && endpoint instanceof String) {
                String endpointName = (String) endpoint;
                endpoint = this.resolveStringEndpoint(endpointName);
            }

            if (endpoint == null) {
                throw new ApplicationContextException("Could not find endpoint for key [" + key + "]");
            } else {
                this.endpointMap.put(key, endpoint);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Mapped key [" + key + "] onto endpoint [" + endpoint + "]");
                }
            }
        }
    }

    protected void unregisterEndpoint(String key) throws BeansException {
        if (!endpointMap.containsKey(key)) {
            throw new ApplicationContextException("Cannot unregister registration key [" + key + "]: there's no endpoint mapped");
        } else {
            this.endpointMap.remove(key);
        }
    }

    public void registerEndpointViaFacade(String key, Object endpoint) throws BeansException {
        this.registerEndpoint(key, endpoint);
    }

    public void unregisterEndpointViaFacade(String key) throws BeansException {
        this.unregisterEndpoint(key);
    }
}
