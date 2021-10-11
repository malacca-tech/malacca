package org.malacca.dingtalk;

import lombok.Data;

@Data
public class DingtalkTextMessage extends DingtalkMessage {

    private DingtalkContent text =new DingtalkContent();

    public DingtalkTextMessage() {
        setMsgtype("text");
    }
}
