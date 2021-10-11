package org.malacca.log;

import org.slf4j.Logger;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/19
 * </p>
 * <p>
 * Department :
 * </p>
 */
public interface LogComponent {

    void setLogContext(LogContext context);

    void setLogger(Logger logger);
}
