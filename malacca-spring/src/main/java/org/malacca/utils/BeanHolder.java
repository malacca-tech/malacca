package org.malacca.utils;

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
public class BeanHolder<T> {

    private String name;

    private T bean;

    public BeanHolder(String name, T bean) {
        this.name = name;
        this.bean = bean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getBean() {
        return bean;
    }

    public void setBean(T bean) {
        this.bean = bean;
    }
}
