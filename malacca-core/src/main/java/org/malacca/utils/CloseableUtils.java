package org.malacca.utils;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/26
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class CloseableUtils {

    public static void close(AutoCloseable s) {
        if (s != null) {
            try {
                s.close();
                s = null;
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: 2020/2/26
            }
        }
    }
}
