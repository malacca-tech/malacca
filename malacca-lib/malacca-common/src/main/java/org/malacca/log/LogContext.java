package org.malacca.log;

import lombok.Data;
import org.malacca.messaging.Message;

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
@Data
public class LogContext {
    private String traceId;
    private String tenantId;
    private String namespaceId;
    private String serviceId;
    private String appId;
    private String componentId;
    // TODO: 2020/3/20  node name
    private String nodeId;
    private String type;
    private Message message;
    private boolean status;
    private String tips;
    private String level;
    private LogFactoryType logFactoryType;
    private Class<?> clazz;
    private int consumingTime;
    private String clusterId;
    private String componentType;
    private String errorCode;
    private String description;
}
