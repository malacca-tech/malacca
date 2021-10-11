package org.malacca.exception;

/**
 * Description: malacca-ee
 * <p>
 * Created by chensheng on 2021/4/25 15:20
 * <p>
 * © 2021. DimensionX B.V. 保留所有权利
 */
public class RetryMessageHandlerException extends MessagingException {

    public static final String CODE = "E_RETRY_001";

    public RetryMessageHandlerException(String tips, Exception e) {
        super(CODE, tips, e);
    }
}
