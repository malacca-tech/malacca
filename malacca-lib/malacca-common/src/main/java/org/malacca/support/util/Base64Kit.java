package org.malacca.support.util;

import org.malacca.exception.Base64UtilMessageHandlerException;

import java.util.Base64;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/17
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class Base64Kit {

    /**
     * base64加密
     *
     * @param str
     * @return
     */
    public static String encode(String str) {
        String base64encodedString = "";
        if (str != null) {
            try {
                base64encodedString = Base64.getEncoder().encodeToString(str.getBytes("utf-8"));
            } catch (Exception e) {
                throw new Base64UtilMessageHandlerException("Base64加密异常", e);
            }
        }
        return base64encodedString;
    }

    /**
     * base64解密
     *
     * @param str
     * @return
     */
    public static String decode(String str) {
        String base64decodedString = "";
        if (str != null) {
            byte[] base64decodedBytes = Base64.getDecoder().decode(str);
            try {
                base64decodedString = new String(base64decodedBytes, "utf-8");
            } catch (Exception e) {
                throw new Base64UtilMessageHandlerException("Base64解密异常", e);
            }
        }
        return base64decodedString;
    }
}
