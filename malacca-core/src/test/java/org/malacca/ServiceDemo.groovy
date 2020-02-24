package org.malacca

import org.malacca.entry.Entry
import org.malacca.entry.holder.HttpEntryHolder
import org.malacca.entry.register.DefaultEntryRegister
import org.malacca.service.DefaultServiceManager
import spock.lang.Specification

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/24
 * </p>
 * <p>
 * Department :
 * </p>
 */
class ServiceDemo extends Specification {

    def test() {
        given: "准备数据"
        def serviceYml = "# 此yml 为基础描述esb服务文档，包含 serviceId,namespace,version,dispalyName,description,entries,components,flow 属性\n" +
                "# 此配置 模板 应满足不依赖第三方 而独立解析成业务\n" +
                "# 服务唯一标识\n" +
                "serviceId: A001\n" +
                "# 分组\n" +
                "namespace: default\n" +
                "# 版本覆盖关系，不同版本， 版本冲突解决\n" +
                "version: v1.0.0\n" +
                "# 服务名\n" +
                "displayName: \n" +
                "# 描述\n" +
                "description:\n" +
                "# 入口 \n" +
                "entries:\n" +
                "    # 内部组件  默认 type  企业版组件 另外种格式区分 eg: org.malacca.http\n" +
                "  - type: httpInput\n" +
                "    # 组件id  用于流程 控制\n" +
                "    id: component1\n" +
                "    # 入口 展示名称 \n" +
                "    name: HIS # 应用方\n" +
                "    # 服务 状态\n" +
                "    status: true\n" +
                "    #组件特有参数\n" +
                "    params:\n" +
                "      # http 映射路径 创建实例 handlerMapping 处理映射\n" +
                "      path: /path/test1\n" +
                "      method: GET\n" +
                "    # 环境变量 key-value\n" +
                "    env: \n" +
                "        # key value 形式 塞到commponent \n" +
                "      key1: value1\n" +
                "      key2: value3 \n" +
                "  - type: httpInput\n" +
                "    # 组件id  用于流程 控制\n" +
                "    id: component2\n" +
                "    # 入口 展示名称 \n" +
                "    name: \n" +
                "    # 服务 状态\n" +
                "    status: false\n" +
                "    #组件特有参数\n" +
                "    params:\n" +
                "      path: /path/test2\n" +
                "      method: GET\n" +
                "# 组件 包括 中间的数据业务组件 以及 输出组件\n" +
                "components:\n" +
                "  # webservice\n" +
                "  - type: httpOutput\n" +
                "    # 组件id\n" +
                "    id: soapOut1\n" +
                "    #组件name\n" +
                "    name: 数据上报\n" +
                "    #组件是否启用\n" +
                "    status: true\n" +
                "    #组件内部属性\n" +
                "    params:\n" +
                "      url: http://www.baidu.com\n" +
                "      timeout: 10000\n" +
                "    #组件环境变量 key-value 键值对\n" +
                "    env:    \n" +
                "      key1: value1\n" +
                "      key2: value3\n" +
                " # 流程 start 开始 stop结束 用于描述业务流程关系\n" +
                "flow: >\n" +
                "  start --> http1 \n" +
                "  http1 --> http2\n" +
                "  http2 --> stop"
        def serviceManager = new DefaultServiceManager()
        def entryRegister = new DefaultEntryRegister()
        entryRegister.putHolder("httpInput", new HttpEntryHolder() {

            @Override
            void unloadEntry(String id, Entry entry) {

            }
        })
        serviceManager.setEntryRegister(entryRegister)
        serviceManager.loadService(serviceYml)
    }
}
