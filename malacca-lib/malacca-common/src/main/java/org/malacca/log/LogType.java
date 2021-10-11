package org.malacca.log;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/20
 * </p>
 * <p>
 * Department :
 * </p>
 */
public enum LogType {
    REQUEST("request"), SERVICE("service"), RESPONSE("response");

    private String name;

    LogType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
