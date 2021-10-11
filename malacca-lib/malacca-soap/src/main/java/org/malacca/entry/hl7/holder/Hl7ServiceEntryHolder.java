package org.malacca.entry.hl7.holder;

import com.alibaba.fastjson.JSONObject;
import org.malacca.entry.Entry;
import org.malacca.entry.hl7.Hl7Entry;
import org.malacca.entry.hl7.Hl7Service;
import org.malacca.entry.holder.AbstractEntryHolder;
import org.malacca.exception.ServiceLoadException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;
import java.util.HashMap;
import java.util.Map;

@WebService(targetNamespace = "urn:hl7-org:v3",
        endpointInterface = "org.malacca.entry.hl7.Hl7Service",
        serviceName = "HIPService",
        portName = "HIPServiceSoap12")
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
public class Hl7ServiceEntryHolder extends AbstractEntryHolder<Entry> implements Hl7Service {

    private Map<String, Hl7Entry> hl7EntryMap;

    @Override
    public String HIPMessageServer(String action, String data) {
        Message buildMessage = MessageBuilder.withPayload(data).setContext("action", action).build();
        Hl7Entry hl7Entry = getHl7EntryMap().get(action);
        if (hl7Entry != null) {
            Message resultMessage = hl7Entry.handleMessage(buildMessage);
            return JSONObject.toJSONString(resultMessage.getPayload());
        } else {
            // TODO: 2020/4/6 没找到
        }
        return null;
    }

    @Override
    public void loadEntry(String key, Entry hl7Entry) throws ServiceLoadException {
        getHl7EntryMap().put(key, (Hl7Entry) hl7Entry);
    }

    @Override
    public void unloadEntry(String key, Entry hl7Entry) {
        getHl7EntryMap().remove(key);
    }

    public Map<String, Hl7Entry> getHl7EntryMap() {
        if (hl7EntryMap == null) {
            hl7EntryMap = new HashMap<>(16);
        }
        return hl7EntryMap;
    }

    public void setHl7EntryMap(Map<String, Hl7Entry> hl7EntryMap) {
        this.hl7EntryMap = hl7EntryMap;
    }
}
