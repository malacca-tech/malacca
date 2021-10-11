package org.malacca.exception;

/**
 * Description: malacca-ee
 * <p>
 * Created by yangxing on 2021/8/17 10:15
 * <p>
 * © 2021. DimensionX B.V. 保留所有权利
 */
public class EmailMessageHandlerException extends MessagingException {

    public static final String CODE = "E_MAIL_002";

    public EmailMessageHandlerException(String tips) {
        super(CODE, tips);
    }

    public EmailMessageHandlerException(Exception e) {
        super(e);
    }

    public EmailMessageHandlerException(String tips, Exception e) {
        super(CODE, tips, e);
    }
}
