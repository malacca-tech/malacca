package org.malacca.entry.common;

import org.malacca.utils.SoapUtils;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.WsdlDefinition;

import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;
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
public class SoapMessageDispatcherServlet extends MessageDispatcherServlet {

    private Map<String, WsdlDefinition> wsdlDefinitions = new Hashtable<>();

    @Override
    protected WsdlDefinition getWsdlDefinition(HttpServletRequest request) {
        if ("GET".equals(request.getMethod()) && request.getRequestURI().endsWith(".wsdl")) {
            String fileName = SoapUtils.extractFilenameFromUrlPath(request.getRequestURI());
            return this.wsdlDefinitions.get(fileName);
        } else {
            return null;
        }
    }

    public void registerDefinition(String path, WsdlDefinition definition) {
        wsdlDefinitions.put(path, definition);
    }

    public void unregisterDefinition(String path) {
        wsdlDefinitions.remove(path);
    }
}