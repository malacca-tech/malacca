package org.malacca.event;

import lombok.Data;

import java.util.Map;

/**
 * Description: malacca-ee
 * <p>
 * Created by chensheng on 2021/4/26 15:36
 * <p>
 * © 2021. DimensionX B.V. 保留所有权利
 */
@Data
public class MalaccaEvent {
    private String code;
    private String tips;
    private Exception exception;

    public MalaccaEvent() {
    }

    public MalaccaEvent(String code, String tips) {
        this.code = code;
        this.tips = tips;
    }

    public MalaccaEvent(String code, String tips, Exception exception) {
        this.code = code;
        this.tips = tips;
        this.exception = exception;
    }
}
