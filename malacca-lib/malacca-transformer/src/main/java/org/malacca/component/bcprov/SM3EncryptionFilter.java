package org.malacca.component.bcprov;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.SM3;
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
public class SM3EncryptionFilter extends AbstractAdvancedComponent {

    // 加密内容
    private String data;

    // 盐值
    private String salt;

    // 加盐位置
    private int saltPosition = 0;

    // 散列次数
    private int digestCount = 1;

    // 加密结果展现形式(SM2还支持base64和bcd)
    private EncryptOutputType encryptOutputType = EncryptOutputType.Hex;

    public SM3EncryptionFilter(String id, String name) {
        super(id, name);
    }

    @Override
    protected Message doHandleMessage(Message<?> message) throws MessagingException {
        String result = "";
        MessageFreeMarker freeMarker = new MessageFreeMarker(message);
        String saltStr = StringUtils.hasText(salt) ? freeMarker.parseExpression(salt) : null;
        String content = freeMarker.parseExpression(data);
        try {
            SM3 sm3 = new SM3(SecureUtil.decode(saltStr), saltPosition, digestCount);
            switch (encryptOutputType) {
                case Hex:
                    result = sm3.digestHex(content);
                    break;
                default:
                    result = StrUtil.str(sm3.digest(content), CHARSET_UTF_8);
                    break;
            }
        } catch (Exception e) {
            throw new SMFilterMessageHandlerException("国密SM3加密失败", e);
        }
        Message resultMessage = MessageBuilder.fromMessage(message).withPayload(result).build();
        return resultMessage;
    }

    @Override
    public String getType() {
        return "sm3Encryption";
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getSaltPosition() {
        return saltPosition;
    }

    public void setSaltPosition(int saltPosition) {
        this.saltPosition = saltPosition;
    }

    public int getDigestCount() {
        return digestCount;
    }

    public void setDigestCount(int digestCount) {
        this.digestCount = digestCount;
    }

    public EncryptOutputType getEncryptOutputType() {
        return encryptOutputType;
    }

    public void setEncryptOutputType(EncryptOutputType encryptOutputType) {
        this.encryptOutputType = encryptOutputType;
    }
}

