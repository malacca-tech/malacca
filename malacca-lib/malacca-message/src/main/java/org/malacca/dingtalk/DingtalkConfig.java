package org.malacca.dingtalk;

import lombok.Data;

@Data
public class DingtalkConfig {

    private String appKey;
    private String appSecret;
    private String dingTalkHost;
}
