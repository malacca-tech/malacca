   ####流程编排设计
   ```text
    流程支持以下功能
        1. directChannel
        2. pubsubChannel
        3. executorChannel
        4. queueChannel
        5. priorityChannel
        6. errorChannel
    queueChannel priorityChannel 结合组件可以实现功能
    设计应满足
    1.同异步标识
    2.是否有返回值（可以有个返回通道标识，或者如果为异步操作，默人返回）
    3.是否是异常通道
    --> 同步通道
    -.-> 异步通道
    -condition-> 条件同步通道
    -.condition.-> 条件异步通道
    -E-> 异常同步通道
    -.E.->异常同步通道

描述 
    Flow 
        List<FlowElement> 列表
        FlowElement
            id component
            isSynchronized 同异步
            isErrorChannel 异常
            String condition 条件 freemarker表达式
            List<FlowElement> elements 下一个 elements
        getNextFlowElements(componentId)
```
