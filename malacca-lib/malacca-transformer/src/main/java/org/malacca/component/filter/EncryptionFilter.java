package org.malacca.component.filter;

import org.malacca.exception.EncryptionMessageHandlerException;
import org.malacca.support.helper.AesHelper;
import org.malacca.support.util.Base64Kit;

import java.security.MessageDigest;

public class EncryptionFilter extends AbstractCryptographyFilter {

    public EncryptionFilter(String id, String name) {
        super(id, name);
    }

    @Override
    protected String handleAes(String data) {
        try {
            AesHelper aesHelper = new AesHelper(getMode(), getBadPadding(), getIv(), getPassword(), getOutputType());
            return aesHelper.encrypt(data);
        } catch (Exception e) {
            throw new EncryptionMessageHandlerException("AES加密失败", e);
        }
    }

    @Override
    protected String handleBase64(String data) {
        try {
            return Base64Kit.encode(data);
        } catch (Exception e) {
            throw new EncryptionMessageHandlerException("BASE64加密失败", e);
        }
    }

    public String handleMd5(String data) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(data.getBytes("utf-8"));
        } catch (Exception e) {
            throw new EncryptionMessageHandlerException("MD5加密失败", e);
        }
        return new String(digest);

    }

    @Override
    public String getType() {
        return "encryptionFilter";
    }

}
