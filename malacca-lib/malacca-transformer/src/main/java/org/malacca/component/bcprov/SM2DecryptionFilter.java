package org.malacca.component.bcprov;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.asymmetric.SM2Engine;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.exception.MessagingException;
import org.malacca.exception.SMFilterMessageHandlerException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.malacca.support.helper.MessageFreeMarker;
import org.springframework.util.StringUtils;

import static cn.hutool.core.util.CharsetUtil.CHARSET_UTF_8;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :yangxing 2021/8/18
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class SM2DecryptionFilter extends AbstractAdvancedComponent {

    // 解密内容
    private String data;

    // 公钥
    private String publicKey;

    // 私钥
    private String privateKey;

    private SM2Engine.SM2Mode sm2Mode = SM2Engine.SM2Mode.C1C3C2;

    // 加密结果展现形式(支持base64,bcd,hex)
    private EncryptOutputType encryptOutputType = EncryptOutputType.Base64;

    public SM2DecryptionFilter(String id, String name) {
        super(id, name);
    }

    @Override
    protected Message doHandleMessage(Message<?> message) throws MessagingException {
        String result = "";
        MessageFreeMarker freeMarker = new MessageFreeMarker(message);
        String privateKeyStr = freeMarker.parseExpression(privateKey);
        String publicKeyStr = StringUtils.hasText(publicKey) ? freeMarker.parseExpression(publicKey) : null;
        String content = freeMarker.parseExpression(data);
        try {
            SM2 sm2 = new SM2(privateKeyStr, publicKeyStr).setMode(sm2Mode);
            switch (encryptOutputType) {
                case Bcd:
                    result = sm2.decryptStrFromBcd(content, KeyType.PrivateKey, CHARSET_UTF_8);
                    break;
                default:
                    result = sm2.decryptStr(content, KeyType.PrivateKey, CHARSET_UTF_8);
                    break;
            }
        } catch (Exception e) {
            throw new SMFilterMessageHandlerException("国密SM2解密失败", e);
        }
        Message resultMessage = MessageBuilder.fromMessage(message).withPayload(result).build();
        return resultMessage;
    }

    @Override
    public String getType() {
        return "sm2Decryption";
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public SM2Engine.SM2Mode getSm2Mode() {
        return sm2Mode;
    }

    public void setSm2Mode(SM2Engine.SM2Mode sm2Mode) {
        this.sm2Mode = sm2Mode;
    }

    public EncryptOutputType getEncryptOutputType() {
        return encryptOutputType;
    }

    public void setEncryptOutputType(EncryptOutputType encryptOutputType) {
        this.encryptOutputType = encryptOutputType;
    }
}

