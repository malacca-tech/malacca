package org.malacca.entry.common;

import org.springframework.ws.transport.http.HttpServletConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletConnection extends HttpServletConnection {

    protected ServletConnection(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        super(httpServletRequest,httpServletResponse);
    }
}
