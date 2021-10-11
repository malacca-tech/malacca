package org.malacca.exception;

/**
 * Description: malacca-ee
 * <p>
 * Created by yangxing on 2021/5/18 16:15
 * <p>
 * © 2021. DimensionX B.V. 保留所有权利
 */
public class RateLimiterMessageHandlerException extends MessagingException {

    public static final String CODE = "E_TRANSFORMER_008";

    public RateLimiterMessageHandlerException(String tips, Exception e) {
        super(CODE, tips, e);
    }

    public RateLimiterMessageHandlerException(String tips) {
        super(CODE, tips);
    }
}
