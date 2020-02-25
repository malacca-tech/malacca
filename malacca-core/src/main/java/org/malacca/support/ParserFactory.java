package org.malacca.support;

import org.malacca.support.parser.Parser;

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
public interface ParserFactory<T> {
    /**
     * 根据类型字符串获取对应的parser
     *
     * @param type
     * @return
     */
    Parser<T, ?> getParser(String type, Class T);
}
