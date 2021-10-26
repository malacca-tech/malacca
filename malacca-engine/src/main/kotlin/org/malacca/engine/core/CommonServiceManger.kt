package org.malacca.engine.core

import org.malacca.entry.register.EntryRegister
import org.malacca.flow.DefaultFlowBuilder
import org.malacca.flow.FlowBuilder
import org.malacca.parser.ParserInterface
import org.malacca.service.AbstractServiceManager
import org.malacca.support.ClassNameParserFactory
import org.malacca.support.ParserFactory
import org.reflections.Reflections
import org.reflections.scanners.FieldAnnotationsScanner
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ConfigurationBuilder
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Component
class CommonServiceManger : AbstractServiceManager, InitializingBean, DisposableBean {

    @Autowired
    var advancedEntryRegister: EntryRegister? = null

    constructor()

    constructor(entryRegister: EntryRegister?, parserFactory: ParserFactory<*>?, flowBuilder: FlowBuilder?)

    override fun afterPropertiesSet() {
        if (entryRegister == null) {
            entryRegister = advancedEntryRegister
        }

        if (parserFactory == null) {
            val classNameParserFactory = ClassNameParserFactory()
            scanParser(classNameParserFactory)
            parserFactory = classNameParserFactory
        }

        if (flowBuilder == null) {
            flowBuilder = DefaultFlowBuilder()
        }

        if (serviceMap == null) {
            serviceMap = HashMap()
        }

        if (threadExecutor == null) {
            threadExecutor = ThreadPoolExecutor(50, 200,
                    10, TimeUnit.SECONDS,
                    LinkedBlockingQueue(10000))
        }

    }

    override fun destroy() {
        TODO("Not yet implemented")
    }

    override fun loadService(yml: String?) {
        super.loadService(yml)
    }

    private fun scanParser(classNameParserFactory: ClassNameParserFactory) {
        // 扫包
        val reflections = Reflections(ConfigurationBuilder()
                .forPackages("org.malacca.component.parser", "org.malacca.support", "org.malacca.parser", "org.malacca.core.component") // 指定路径URL
                .addScanners(SubTypesScanner()) // 添加子类扫描工具
                .addScanners(FieldAnnotationsScanner()) // 添加 属性注解扫描工具
        )
        val entrySet: Set<Class<*>> = reflections.getTypesAnnotatedWith(ParserInterface::class.java)
        for (parserClass in entrySet) {
            val className = parserClass.name
            val parserInterface: ParserInterface = parserClass.getAnnotation(ParserInterface::class.java)
            val type: String = parserInterface.type
            val typeAlia: String = parserInterface.typeAlia
            classNameParserFactory.setTypeAlia(typeAlia, className, type)
        }
    }
}