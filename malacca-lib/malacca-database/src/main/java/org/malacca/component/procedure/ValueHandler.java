package org.malacca.component.procedure;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :yangxing 2021/7/15
 * </p>
 * <p>
 * Department :
 * </p>
 */
public interface ValueHandler {

    public enum DataType {
        VARCHAR2, INT, NUMBER, DATE, TIMESTAMP;
    }

    Object handle(ProcedureParam param);
}

