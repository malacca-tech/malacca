package org.malacca.flow;

import cn.hutool.core.util.StrUtil;
import org.malacca.component.Component;
import org.malacca.entry.Entry;
import org.malacca.exception.FlowBuildException;
import org.malacca.service.Service;

import java.util.Map;
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
    public Flow buildFlow(String flowExpression, Map<String, Entry> entryMap, Map<String, Component> componentMap) throws FlowBuildException {
        try {
            validFlowExpression(flowExpression, entryMap, componentMap);
            DefaultFlow flow = new DefaultFlow();
            //按行解析
            String[] lines = flowExpression.split("\n");
            for (String singleLine : lines) {
                flow.addFlowElement(parseLine(singleLine));//注册到Flow里面
            }
            return flow;
        } catch (FlowBuildException e) {
            throw e;
        } catch (Exception e) {
            // TODO: 2020/2/27
            throw new FlowBuildException();
        }
    }

    protected FlowElement parseLine(String singleLine) {
        String[] parts = splitLine(singleLine);
        FlowElement flowElement = new FlowElement(parts[0], parseChannelExpression(parts[1]), parts[2]);
        return flowElement;
    }

    protected ChannelType parseChannelExpression(String channelExpression) {
        DefaultChannelType channelType = new DefaultChannelType();
        Pattern p = Pattern.compile(ASY_REGEX);
        Matcher m = p.matcher(channelExpression);
        boolean isAsy = false;
        while (m.find()) {
            isAsy = true;
            setFlowElementAttributes(channelType, m.group(1).trim(), false);
        }

        //判断是否是 同步
        if (!isAsy) {
            Pattern syncPattern = Pattern.compile(SYNC_REGEX);
            Matcher syncMatcher = syncPattern.matcher(channelExpression);
            while (syncMatcher.find()) {
                setFlowElementAttributes(channelType, syncMatcher.group(1).trim(), true);
            }
        }
        return channelType;
    }

    protected String[] splitLine(String line) {
        line = line.trim();
        int firstSplitPointIndex = line.indexOf(" ");
        int lastSplitPointIndex = line.lastIndexOf(" ");
        if (firstSplitPointIndex >= lastSplitPointIndex) {
            // TODO: 2020/2/28 异常
            throw new FlowBuildException();
        }
        String preComponentId = line.substring(0, firstSplitPointIndex);
        String nextComponentId = line.substring(lastSplitPointIndex + 1);
        String channelExpression = line.substring(firstSplitPointIndex + 1, lastSplitPointIndex);
        return new String[]{
                preComponentId, channelExpression, nextComponentId
        };
    }

    protected void validFlowExpression(String expression, Map<String, Entry> entryMap, Map<String, Component> componentMap) throws FlowBuildException {
        //todo 对flow表达式进行校验
        // 1.各个组件或者entry是否存在
    }

    private void setFlowElementAttributes(DefaultChannelType type, String key, boolean isSync) {
        type.setSynchronized(isSync);
        if (StrUtil.isNotBlank(key)) {
            if (ERROR_KEY.equals(key)) {
                type.setExceptionType(true);
            } else {
                // TODO: 2020/2/28 路由
//                Pattern p = Pattern.compile(ROUTER_KEY);
//                Matcher m = p.matcher(key);
//                while (m.find()) {
//                    element.setRouterId(m.group(1));
//                }
            }
        }
    }
}
