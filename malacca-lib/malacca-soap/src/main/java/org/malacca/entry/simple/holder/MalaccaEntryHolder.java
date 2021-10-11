package org.malacca.entry.simple.holder;

import com.alibaba.fastjson.JSONObject;
import org.malacca.entry.Entry;
import org.malacca.entry.holder.AbstractEntryHolder;
import org.malacca.entry.simple.MalaccaService;
import org.malacca.entry.simple.MalaccaSoapEntry;
import org.malacca.exception.ServiceLoadException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;

import javax.jws.WebService;
import java.util.HashMap;
import java.util.Map;

@WebService(endpointInterface = "org.malacca.entry.simple.MalaccaService",
        targetNamespace = "http://soap.malacca.com",
        serviceName = "MalaccaSoapService",
        portName = "MalaccaSoapServicePort")
public class MalaccaEntryHolder extends AbstractEntryHolder<Entry> implements MalaccaService {

    private Map<String, MalaccaSoapEntry> malaccaSoapEntryMap;

    @Override
    public String soapAction(String serviceId, String data, String account, String appId, String time, String token) {
        Message buildMessage = MessageBuilder.withPayload(data)
                .setContext("account", account)
                .setContext("appid", appId)
                .setContext("time", time)
                .setContext("token", token)
                .build();
        MalaccaSoapEntry malaccaSoapEntry = getMalaccaSoapEntryMap().get(serviceId);

        if (malaccaSoapEntry != null) {
            Message resultMessage = malaccaSoapEntry.handleMessage(buildMessage);
            return JSONObject.toJSONString(resultMessage.getPayload());
        } else {
            return String.format("未找到服务%s", serviceId);
        }
    }

    @Override
    public void loadEntry(String key, Entry entry) throws ServiceLoadException {
        getMalaccaSoapEntryMap().put(key, (MalaccaSoapEntry) entry);
    }

    @Override
    public void unloadEntry(String key, Entry entry) {
        getMalaccaSoapEntryMap().remove(key);
    }

    public Map<String, MalaccaSoapEntry> getMalaccaSoapEntryMap() {
        if (malaccaSoapEntryMap == null) {
            malaccaSoapEntryMap = new HashMap<>(16);
        }
        return malaccaSoapEntryMap;
    }

    public void setMalaccaSoapEntryMap(Map<String, MalaccaSoapEntry> malaccaSoapEntryMap) {
        this.malaccaSoapEntryMap = malaccaSoapEntryMap;
    }
}
