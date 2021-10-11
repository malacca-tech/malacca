package org.malacca.utils;

import cn.hutool.core.codec.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.*;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/11/24
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class AESUtils {

    private static String iv = "0123456789ABCDEF";//偏移量字符串必须是16位 当模式是CBC的时候必须设置偏移量
    private static String Algorithm = "AES";
    private static String AlgorithmProvider = "AES/ECB/PKCS5Padding"; // 算法/模式/补码方式

    public static String decrypt(String src, String pwd) throws Exception {
        byte key[] = pwd.getBytes("utf-8");
        SecretKey secretKey = new SecretKeySpec(key, Algorithm);

        //IvParameterSpec ivParameterSpec = getIv();
        Cipher cipher = Cipher.getInstance(AlgorithmProvider);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] hexBytes = hexStringToBytes(src);
        byte[] plainBytes = cipher.doFinal(hexBytes);
        return new String(plainBytes,"utf-8");
    }

    /**
     * 将16进制字符串装换为byte数组
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            b[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return b;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
