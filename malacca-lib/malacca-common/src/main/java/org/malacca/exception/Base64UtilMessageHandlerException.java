package org.malacca.exception;

/**
 * Description: malacca-ee
 * <p>
 * Created by chensheng on 2021/4/25 15:20
 * <p>
 * © 2021. DimensionX B.V. 保留所有权利
 */
public class Base64UtilMessageHandlerException extends MessagingException {

    public static final String CODE = "E_BASE64_001";

    public Base64UtilMessageHandlerException(String tips, Exception e) {
        super(CODE, tips, e);
    }
}
