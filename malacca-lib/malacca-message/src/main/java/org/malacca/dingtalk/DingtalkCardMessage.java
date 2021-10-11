package org.malacca.dingtalk;

import lombok.Data;

@Data
public class DingtalkCardMessage extends DingtalkMessage {

    private DingtalkCard action_card = new DingtalkCard();

    public DingtalkCardMessage() {
        setMsgtype("action_card");
    }
}
