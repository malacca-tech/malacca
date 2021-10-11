package org.malacca.exception;

/**
 * Description: malacca-ee
 * <p>
 * Created by chensheng on 2021/4/25 16:32
 * <p>
 * © 2021. DimensionX B.V. 保留所有权利
 */
public class SqlMessageHandlerException extends MessagingException {

    public static final String CODE = "E-SQL-001";

    public SqlMessageHandlerException(String tips, Exception e) {
        super(CODE, tips, e);
    }

    public SqlMessageHandlerException(String tips) {
        super(CODE, tips);
    }
}
