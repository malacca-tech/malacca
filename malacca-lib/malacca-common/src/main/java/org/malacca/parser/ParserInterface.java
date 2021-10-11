package org.malacca.parser;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ParserInterface {

    /**
     * component | entry
     */
    String type() default "component";

    String typeAlia() default "";
}
