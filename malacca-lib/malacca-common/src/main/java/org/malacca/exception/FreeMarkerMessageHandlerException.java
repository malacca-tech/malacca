package org.malacca.exception;

/**
 * Description: malacca-ee
 * <p>
 * Created by chensheng on 2021/4/25 16:03
 * <p>
 * © 2021. DimensionX B.V. 保留所有权利
 */
public class FreeMarkerMessageHandlerException extends MessagingException {

    public static final String CODE = "E_FREEMARKER_001";

    public FreeMarkerMessageHandlerException(String tips, Exception e) {
        super(CODE, tips, e);
    }
}
