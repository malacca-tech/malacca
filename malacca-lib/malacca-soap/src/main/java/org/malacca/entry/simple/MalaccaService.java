package org.malacca.entry.simple;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

@WebService(targetNamespace = "http://soap.malacca.com")
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
public interface MalaccaService {

    String soapAction(@WebParam(name = "serviceId") String serviceId
            , @WebParam(name = "data") String data
            , @WebParam(name = "account", header = true) String account
            , @WebParam(name = "appid", header = true) String appId
            , @WebParam(name = "time", header = true) String time
            , @WebParam(name = "token", header = true) String token
    );
}
