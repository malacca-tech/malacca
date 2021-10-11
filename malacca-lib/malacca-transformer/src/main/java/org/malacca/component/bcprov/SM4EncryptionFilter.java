package org.malacca.component.bcprov;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SM4;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.exception.MessagingException;
import org.malacca.exception.SMFilterMessageHandlerException;
import org.malacca.messaging.Message;
import org.malacca.support.MessageBuilder;
import org.malacca.support.helper.MessageFreeMarker;
import org.springframework.util.StringUtils;

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
public class SM4EncryptionFilter extends AbstractAdvancedComponent {

    // 加密内容
    private String data;

    // 模式
    private Mode mode = Mode.CBC;

    // Padding补码方式
    private Padding padding = Padding.PKCS5Padding;

    // 密钥，支持三种密钥长度：128、192、256位
    private String secretKey;

    // 偏移向量，加盐
    private String iv;

    // 加密结果展现形式(SM2还支持base64和bcd)
    private EncryptOutputType encryptOutputType = EncryptOutputType.Base64;

    public SM4EncryptionFilter(String id, String name) {
        super(id, name);
    }

    @Override
    protected Message doHandleMessage(Message<?> message) throws MessagingException {
        String result = "";
        MessageFreeMarker freeMarker = new MessageFreeMarker(message);
        String secretKeyStr = freeMarker.parseExpression(secretKey);
        String ivStr = StringUtils.hasText(iv) ? freeMarker.parseExpression(iv) : null;
        String content = freeMarker.parseExpression(data);
        try {
            SM4 sm4 = new SM4(mode, padding, SecureUtil.decode(secretKeyStr), SecureUtil.decode(ivStr));
            switch (encryptOutputType) {
                case Hex:
                    result = sm4.encryptHex(content);
                    break;
                case Base64:
                    result = sm4.encryptBase64(content);
                    break;
            }
        } catch (Exception e) {
            throw new SMFilterMessageHandlerException("国密SM4加密失败", e);
        }
        Message resultMessage = MessageBuilder.fromMessage(message).withPayload(result).build();
        return resultMessage;
    }

    @Override
    public String getType() {
        return "sm4Encryption";
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Padding getPadding() {
        return padding;
    }

    public void setPadding(Padding padding) {
        this.padding = padding;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public EncryptOutputType getEncryptOutputType() {
        return encryptOutputType;
    }

    public void setEncryptOutputType(EncryptOutputType encryptOutputType) {
        this.encryptOutputType = encryptOutputType;
    }
}

