package org.malacca.flow;

import cn.hutool.core.util.StrUtil;
import org.malacca.exception.FlowBuildException;
import org.malacca.service.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/26
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class DefaultFlowBuilder implements FlowBuilder {

    /**
     * 同步正则表达式
     */
    public static final String SYNC_REGEX = "-\\s*([\\s\\S]*?)\\s*->";
    /**
     * 异步正则表达式
     */
    public static final String ASY_REGEX = "-\\s*\\.?\\s*([\\s\\S]*?)\\s*\\.\\s*->";
    /**
     * 异常标识正则表达式
     */
    public static final String ERROR_KEY = "E";

    public static final String ROUTER_KEY = "\\[\\s*router\\s*:\\s*(\\S*?)\\s*]";

    @Override
    public Flow buildFlow(String flowStr, Service service) throws FlowBuildException {
        //按行解析
        try {
            String[] flowElementStrs = flowStr.split("\n");
            DefaultFlow flow = new DefaultFlow();
            for (String flowElementStr : flowElementStrs) {
                flowElementStr = flowElementStr.trim();
                FlowElement flowElement = new FlowElement();
                //获取组件id 组件id 后面必须有空格
                String componentId = flowElementStr.substring(0, flowElementStr.indexOf(" "));
                //检验 组件id是否存在
                if (service.getEntryMap().get(componentId) == null && service.getComponentMap().get(componentId) == null) {
                    // TODO: 2020/2/27 异常
                    throw new FlowBuildException();
                }
                FlowElement nextComponent = buildElement(flowElementStr);
                flowElement.setComponentId(componentId);
                flowElement.setNextElement(nextComponent);
                flow.addFlowElement(flowElement);
            }
            return flow;
        } catch (FlowBuildException e) {
            throw e;
        } catch (Exception e) {
            // TODO: 2020/2/27
            throw new FlowBuildException();
        }
    }

    // TODO: 2020/2/26 死循环
    private FlowElement buildElement(String flowElementStr) {
        //去除空格
        FlowElement flowElement = new FlowElement();
        String componentId = "";

        //判断是否是异步
        Pattern p = Pattern.compile(ASY_REGEX);
        Matcher m = p.matcher(flowElementStr);
        boolean isAsy = false;
        while (m.find()) {
            isAsy = true;
            setFlowElementAttributes(flowElement, m.group(1).trim(), false);
            componentId = flowElementStr.substring(flowElementStr.lastIndexOf(m.group(0)) + m.group(0).length()).trim();
            flowElement.setComponentId(componentId);
        }
        //判断是否是 同步
        if (!isAsy) {
            Pattern syncPattern = Pattern.compile(SYNC_REGEX);
            Matcher syncMatcher = syncPattern.matcher(flowElementStr);
            while (syncMatcher.find()) {
                setFlowElementAttributes(flowElement, syncMatcher.group(1).trim(), true);
                componentId = flowElementStr.substring(flowElementStr.lastIndexOf(syncMatcher.group(0)) + syncMatcher.group(0).length()).trim();
                flowElement.setComponentId(componentId);
            }
        }
        return flowElement;
    }

    private void setFlowElementAttributes(FlowElement element, String key, boolean isSync) {
        element.setSynchronized(isSync);
        if (StrUtil.isNotBlank(key)) {
            if (ERROR_KEY.equals(key)) {
                element.setErrorChannel(true);
            } else {
                Pattern p = Pattern.compile(ROUTER_KEY);
                Matcher m = p.matcher(key);
                while (m.find()) {
                    element.setRouterId(m.group(1));
                }
            }
        }
    }
}
