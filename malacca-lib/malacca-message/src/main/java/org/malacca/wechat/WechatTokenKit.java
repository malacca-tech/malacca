package org.malacca.wechat;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.malacca.kit.HttpKit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class WechatTokenKit {

    private static Map<String, WechatToken> tokenMap = new Hashtable<>();

    /**
     * 获取access_token
     *
     * @param agentid
     * @return
     */
    public static String getToken(String corpid, String agentid, String secret) throws Exception {
        WechatToken token = getToken(agentid);
        String tokenValue = null;
        if (token == null) {
            tokenValue = generateNewToken(corpid, secret);
            token = new WechatToken();
            token.setToken(tokenValue);
            token.setDate(new Date());
            saveToken(agentid, token);
            return tokenValue;
        }
        long now = System.currentTimeMillis();
        if (now - token.getDate().getTime() > 6000000) {
            tokenValue = generateNewToken(corpid, secret);
            token.setToken(tokenValue);
            token.setDate(new Date());
            saveToken(agentid, token);
        } else {
            tokenValue = token.getToken();
        }
        return tokenValue;
    }

    public static String getToken(String corpid, String secret) throws Exception {
        return getToken(corpid, null, secret);
    }

    private static WechatToken getToken(String agentid) throws IOException {
        if (agentid == null) {
            return null;
        }
        WechatToken token = tokenMap.get(agentid);
        if (token == null) {
            String fileName = System.getProperty("user.home") + File.separator + "sie-message" + File.separator + "wechat-token.json";
            File file = new File(fileName);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            String tokenTxt = FileUtils.readFileToString(file, Charset.forName("UTF-8"));
            JSONObject tokenJson = JSONObject.parseObject(tokenTxt);
            if (tokenJson != null) {
                JSONObject o = tokenJson.getJSONObject(agentid);
                if (o != null) {
                    token = JSONObject.toJavaObject(o, WechatToken.class);
                }
            }
        }
        return token;
    }

    private static void saveToken(String agentid, WechatToken wechatToken) throws IOException {
        if (agentid == null) {
            return;
        }
        tokenMap.put(agentid, wechatToken);
        String fileName = System.getProperty("user.home") + File.separator + "sie-message" + File.separator + "wechat-token.json";
        File file = new File(fileName);
        String tokenTxt = FileUtils.readFileToString(file, Charset.forName("UTF-8"));
        JSONObject tokenJson = JSONObject.parseObject(tokenTxt);
        if (tokenJson == null) {
            tokenJson = new JSONObject();
        }
        tokenJson.put(agentid, JSONObject.toJSONString(wechatToken));
        FileUtils.writeStringToFile(file, tokenJson.toJSONString(), Charset.forName("UTF-8"));
    }

    /**
     * @param corpId
     * @param secret
     * @return
     */
    private static String generateNewToken(String corpId, String secret) throws Exception {
        String tokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
        String url = tokenUrl + "?corpid=" + corpId + "&corpsecret=" + secret;
        String response = HttpKit.get(url);
        JSONObject jo = JSONObject.parseObject(response);

        String tokenValue = (String) jo.get("access_token");
        //TODO ERROR
        return tokenValue;
    }

}
