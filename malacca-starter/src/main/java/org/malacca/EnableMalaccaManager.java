package org.malacca;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/3
 * </p>
 * <p>
 * Department :
 * </p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MalaccaAutoConfiguration.class)
@Documented
@Inherited
public @interface EnableMalaccaManager {
}
