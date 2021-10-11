package org.malacca.entry.hl7;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.SOAPBinding;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/4/6
 * </p>
 * <p>
 * Department :
 * </p>
 */
@WebService(targetNamespace = "urn:hl7-org:v3")
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
public interface Hl7Service {

    @WebResult(name = "HIPMessageServerResult", targetNamespace = "")
    @RequestWrapper(localName = "HIPMessageServer", targetNamespace = "urn:hl7-org:v3", className = "gbup.client.HIPMessageServer")
    @ResponseWrapper(localName = "HIPMessageServerResponse", targetNamespace = "urn:hl7-org:v3", className = "gbup.client.HIPMessageServerResponse")
    String HIPMessageServer(
            @WebParam(name = "action", targetNamespace = "")
                    String action,
            @WebParam(name = "message", targetNamespace = "")
                    String message
    );
}
