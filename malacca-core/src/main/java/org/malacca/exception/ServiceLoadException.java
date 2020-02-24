package org.malacca.exception;


/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/20
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class ServiceLoadException extends RuntimeException {
    // TODO: 2020/2/20 异常设计
    public ServiceLoadException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
