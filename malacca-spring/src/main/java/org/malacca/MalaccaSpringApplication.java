package org.malacca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

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
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MalaccaSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(MalaccaSpringApplication.class, args);
    }
}
