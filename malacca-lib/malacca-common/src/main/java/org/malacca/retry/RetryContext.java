package org.malacca.retry;

import lombok.Data;

/**
 * Description: malacca-ee
 * <p>
 * Created by chensheng on 2021/5/24
 * <p>
 * © 2021. DimensionX B.V. 保留所有权利
 */
@Data
public class RetryContext {
    private int times = 3;
}
