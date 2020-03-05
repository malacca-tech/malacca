package org.malacca.utils;

import java.util.Hashtable;
import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/5
 * </p>
 * <p>
 * Department :
 * </p>
 *
 */
public class Name {

    private static Map<String, Integer> classNameCount = new Hashtable<>();

    public static String getNextId(Class<?> clazz) {
        return getNextId(clazz.getName());
    }

    public static String getNextId(String className) {
        if (classNameCount.containsKey(className)) {
            classNameCount.put(className, classNameCount.get(className) + 1);
        } else {
            classNameCount.put(className, 1);
        }
        return className + classNameCount.get(className);
    }

}
