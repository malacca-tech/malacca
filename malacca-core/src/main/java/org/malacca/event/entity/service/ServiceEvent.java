package org.malacca.event.entity.service;

import org.malacca.event.Event;

import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: 业务处理事件
 * </p>
 * <p>
 * Author :chensheng 2020/2/25
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class ServiceEvent extends Event {

    public static final String BEGIN_SERVICE = "BEGIN_SERVICE";

    public static final String ERROR_SERVICE = "ERROR_SERVICE";

    public static final String END_SERVICE = "END_SERVICE";

    // TODO: 2020/2/25 事件列表

    public ServiceEvent(String code) {
        super(code);
    }

    public ServiceEvent(String code, String tips) {
        super(code, tips);
    }

    public ServiceEvent(String code, String tips, Throwable throwable) {
        super(code, tips, throwable);
    }

    public ServiceEvent(String code, String tips, Map<String, Object> extendValues) {
        super(code, tips, extendValues);
    }

    public ServiceEvent(String code, Map<String, Object> extendValues) {
        super(code, extendValues);
    }

    public ServiceEvent(String code, Throwable throwable) {
        super(code, throwable);
    }

    public ServiceEvent(String code, Throwable throwable, Map<String, Object> extendValues) {
        super(code, throwable, extendValues);
    }
}
