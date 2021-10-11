package org.malacca.dingtalk;

import lombok.Data;

@Data
public class DingtalkLinkMessage extends DingtalkMessage {

    private DingtalkLink link = new DingtalkLink();

    public DingtalkLinkMessage() {
        setMsgtype("link");
    }

}
