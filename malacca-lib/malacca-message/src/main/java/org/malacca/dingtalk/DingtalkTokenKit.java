package org.malacca.dingtalk;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DingtalkTokenKit {

    private static Map<String, DingtalkToken> tokenMap = new HashMap<>();

    public static String getDingtalkToken(String appKey, String appSecret, String host) throws Exception {
        DingtalkToken dingtalkToken = tokenMap.get(appKey);
        String tokenValue = null;
        if (dingtalkToken == null) {
            tokenValue = generateNewToken(appKey, appSecret, host);
            dingtalkToken = new DingtalkToken();
            dingtalkToken.setToken(tokenValue);
            dingtalkToken.setDate(new Date());
            tokenMap.put(appKey, dingtalkToken);
            return tokenValue;
        }
        long now = System.currentTimeMillis();
        if (now - dingtalkToken.getDate().getTime() > 7000000) {
            tokenValue = generateNewToken(appKey, appSecret, host);
            dingtalkToken.setToken(tokenValue);
            dingtalkToken.setDate(new Date());
            tokenMap.put(appKey, dingtalkToken);
        } else {
            tokenValue = dingtalkToken.getToken();
        }
        return tokenValue;
    }

    private static String generateNewToken(String appKey, String appSecret, String host) throws Exception {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest req = new OapiGettokenRequest();
        req.setAppkey(appKey);
        req.setAppsecret(appSecret);
        req.setHttpMethod("GET");
        OapiGettokenResponse rsp = client.execute(req);
        String accessToken = rsp.getAccessToken();
        return accessToken;
    }
}
