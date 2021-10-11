package org.malacca.dingtalk;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public abstract class DingtalkMessage {

    private String agentid;
    private String touser;
    private String toparty;
    private String totag;
    private String msgtype;

    public String toJSONString() {
        return JSONObject.toJSONString(this);
    }
}
