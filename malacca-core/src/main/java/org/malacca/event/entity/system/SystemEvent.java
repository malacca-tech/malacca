package org.malacca.event.entity.system;

import org.malacca.event.Event;

import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: 系统事件
 * </p>
 * <p>
 * Author :chensheng 2020/2/25
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class SystemEvent extends Event {

    public static final String INITIALIZE = "INITIALIZE";

    public static final String FLOW_LOAD_ERROR = "FLOW_LOAD_ERROR";

    public static final String FLOW_LOAD_SUCCESS = "FLOW_LOAD_SUCCESS";

    public static final String JOB_SCHEDULE_BEGIN = "JOB_SCHEDULE_BEGIN";

    public static final String JOB_SCHEDULE_ERROR = "JOB_SCHEDULE_ERROR";

    public static final String JOB_SCHEDULE_IGNORE = "JOB_SCHEDULE_IGNORE";

    public static final String JOB_SCHEDULE_CANCEL = "JOB_SCHEDULE_CANCEL";

    public static final String JOB_SCHEDULE_SUCCESS = "JOB_SCHEDULE_SUCCESS";

    public static final String JOB_SCHEDULE_TIMEOUT_ERROR = "JOB_SCHEDULE_TIMEOUT_ERROR";

    public SystemEvent(String code) {
        super(code);
    }

    public SystemEvent(String code, String tips) {
        super(code, tips);
    }

    public SystemEvent(String code, String tips, Throwable throwable) {
        super(code, tips, throwable);
    }

    public SystemEvent(String code, String tips, Map<String, Object> extendValues) {
        super(code, tips, extendValues);
    }

    public SystemEvent(String code, Map<String, Object> extendValues) {
        super(code, extendValues);
    }

    public SystemEvent(String code, Throwable throwable) {
        super(code, throwable);
    }

    public SystemEvent(String code, Throwable throwable, Map<String, Object> extendValues) {
        super(code, throwable, extendValues);
    }
}
