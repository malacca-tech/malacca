package org.malacca.entry.common;

import org.malacca.utils.SoapUtils;
import org.springframework.ws.transport.http.WsdlDefinitionHandlerAdapter;

import javax.servlet.http.HttpServletRequest;

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
public class SoapDefinitionHandlerAdapter extends WsdlDefinitionHandlerAdapter {

    protected String transformLocation(String location, HttpServletRequest request) {
        StringBuilder url = new StringBuilder(request.getScheme());
        url.append("://").append(request.getServerName()).append(':').append(request.getServerPort());
        String fileName = SoapUtils.extractFilenameFromUrlPath(request.getRequestURI());
        url.append(fileName);
        return url.toString();
    }
}