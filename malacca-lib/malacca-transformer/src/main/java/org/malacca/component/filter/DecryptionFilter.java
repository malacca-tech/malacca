package org.malacca.component.filter;


import org.malacca.exception.DecryptionMessageHandlerException;
import org.malacca.support.helper.AesHelper;
import org.malacca.support.util.Base64Kit;

public class DecryptionFilter extends AbstractCryptographyFilter {

    public DecryptionFilter(String id, String name) {
        super(id, name);
    }

    @Override
    protected String handleAes(String data) {
        try {
            AesHelper aesHelper = new AesHelper(getMode(), getBadPadding(), getIv(), getPassword(), getOutputType());
            return aesHelper.decrypt(data);
        } catch (Exception e) {
            throw new DecryptionMessageHandlerException("AES解密失败", e);
        }
    }

    @Override
    protected String handleBase64(String data) {
        try {
            return Base64Kit.decode(data);
        } catch (Exception e) {
            throw new DecryptionMessageHandlerException("Base64解密失败", e);
        }
    }

    @Override
    protected String handleMd5(String data) {
        throw new DecryptionMessageHandlerException("不可解密类的型" + getEncryptType());
    }

    @Override
    public String getType() {
        return "decryptionFilter";
    }

}
