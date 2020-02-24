package org.malacca.flow;

import org.malacca.messaging.Message;

import java.util.List;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description: 业务流程
 * </p>
 * <p>
 * Author :chensheng 2020/2/20
 * </p>
 * <p>
 * Department :
 * </p>
 */
public interface Flow {
    /**
     * 获取下一个流程列表id
     * componentId 组件id
     * message 提供元数据 上下文 用于流程条件判断
     */
    List<String> getNextComponents(String componentId, Message message);
}
