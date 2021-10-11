package org.malacca.exception;

/**
 * Description: malacca-ee
 * <p>
 * Created by chensheng on 2021/4/25 16:50
 * <p>
 * © 2021. DimensionX B.V. 保留所有权利
 */
public class JSMessageHandlerException extends MessagingException {

    public static final String CODE = "E_TRANSFORMER_006";

    public JSMessageHandlerException(String tips, Exception e) {
        super(CODE, tips, e);
    }
}
