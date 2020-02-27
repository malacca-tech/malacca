package org.malacca.flow;

import org.malacca.exception.FlowBuildException;
import org.malacca.service.Service;

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
public interface FlowBuilder {

    /**
     * 根据流程字符串 解析出流程
     *
     * @param flowStr
     * @return
     */
    Flow buildFlow(String flowStr, Service service) throws FlowBuildException;

}
