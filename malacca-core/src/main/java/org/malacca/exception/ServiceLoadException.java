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

    private String code;

    private String data;

    public ServiceLoadException(String message) {
        super(message);
        this.code = "E";
        this.data = "";
    }

    public ServiceLoadException(String message, String data) {
        super(message);
        this.code = "E";
        this.data = data;
    }

    public ServiceLoadException(String code, String message, String data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public ServiceLoadException(String message, Throwable cause) {
        super(message, cause);
        this.code = "";
        this.data = "";
    }

    public ServiceLoadException(String message, String data, Throwable cause) {
        super(message, cause);
        this.code = "E";
        this.data = data;
    }

    public ServiceLoadException(String code, String message, String data, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
