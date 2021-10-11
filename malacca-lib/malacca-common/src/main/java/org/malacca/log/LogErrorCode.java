package org.malacca.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/7/1
 * </p>
 * <p>
 * Department :
 * </p>
 */
@Getter
@AllArgsConstructor
public enum LogErrorCode {
    LOG_RECORD_ERROR("10001", "日志记录失败"),
    ;
    private String code;
    private String context;

}
