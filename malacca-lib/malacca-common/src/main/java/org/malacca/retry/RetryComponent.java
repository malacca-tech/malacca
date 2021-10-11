package org.malacca.retry;

/**
 * Description: malacca-ee
 * <p>
 * Created by chensheng on 2021/5/24
 * <p>
 * © 2021. DimensionX B.V. 保留所有权利
 */
public interface RetryComponent {
    void setRetryContext(RetryContext retryContext);
}
