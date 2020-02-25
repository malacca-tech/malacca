package org.malacca.support;

import cn.hutool.core.util.StrUtil;
import org.malacca.component.Component;
import org.malacca.component.output.HttpOutComponent;
import org.malacca.entry.Entry;
import org.malacca.entry.HttpEntry;
import org.malacca.support.parser.HttpInputParser;
import org.malacca.support.parser.Parser;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/25
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class ClassNameParserFactory implements ParserFactory {

    /**
     * 这里存的是 type -> parser的className的一个别名关系
     */
    Map<String, String> entryTypeAlias;

    Map<String,String> componentTypeAlias;

    @Override
    public Parser getParser(String type,Class target) {

        if(target.isAssignableFrom(Entry.class)){
            return getEntryParser(type);
        }
        if(target.isAssignableFrom(Component.class)){
            return getComponentParser(type);
        }
        //todo 根据反射创建实例，如果对于特殊的场景需要单例的，就把这个factory注册为spring的bean，不过讲道理肯定是要注册成bean的，方便设置到每个service里面
        //todo 这里如果实在没找到，是应该抛一个ParserNotFoundException,告诉用户你配的这个yaml有问题，你要的组件没有加载
        return null;
    }

    private Parser getEntryParser(String type){
        String className = entryTypeAlias.get(type);
        return createParserInstance(className);
    }

    private Parser getComponentParser(String type){
        String className = componentTypeAlias.get(type);
        return createParserInstance(className);
    }

    private Parser createParserInstance(String className){
        return null;
    }

    protected void initTypeAlias() {
        entryTypeAlias = new HashMap<>();
        componentTypeAlias = new HashMap<>();
        //tood 这里只是一个demo，后期需要从classpath的META-INF的配置文件里面获取这个映射关系
        entryTypeAlias.put("http", HttpInputParser.class.getName());
        componentTypeAlias.put("http", HttpOutComponent.class.getName());
    }

    /**
     * 当有新的插件加载的时候可能需要重新刷一下这个映射关系，现在是一个全局刷的
     * todo 可能需要一个指定添加alias的方法
     */
    public void refreshTypeAlias(){
        initTypeAlias();
    }

}
