package org.malacca.event;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: 事件类
 * </p>
 * <p>
 * Author :chensheng 2020/2/25
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class Event implements Serializable {

    /**
     * 事件编码
     */
    private String code;

    /**
     * 异常
     */
    private Throwable throwable;

    /**
     * 事件消息
     */
    private String tips;

    private Map<String, Object> extendValues;

    public Event(String code) {
        this.code = code;
    }

    public Event(String code, String tips) {
        this.code = code;
        this.tips = tips;
    }

    public Event(String code, String tips, Throwable throwable) {
        this.code = code;
        this.tips = tips;
        this.throwable = throwable;
    }

    public Event(String code, String tips, Map<String, Object> extendValues) {
        this.code = code;
        this.tips = tips;
        this.extendValues = extendValues;
    }

    public Event(String code, Map<String, Object> extendValues) {
        this.code = code;
        this.extendValues = extendValues;
    }

    public Event(String code, Throwable throwable) {
        this.code = code;
        this.throwable = throwable;
    }

    public Event(String code, Throwable throwable, Map<String, Object> extendValues) {
        this.code = code;
        this.throwable = throwable;
        this.extendValues = extendValues;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, Object> getExtendValues() {
        return extendValues;
    }

    public void setExtendValues(Map<String, Object> extendValues) {
        this.extendValues = extendValues;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

}
