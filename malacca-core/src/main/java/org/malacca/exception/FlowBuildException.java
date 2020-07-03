package org.malacca.exception;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/27
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class FlowBuildException extends RuntimeException {
    public FlowBuildException() {
    }

    public FlowBuildException(String message) {
        super(message);
    }

    public FlowBuildException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlowBuildException(Throwable cause) {
        super(cause);
    }

    public FlowBuildException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
