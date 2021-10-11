package org.malacca.component;

import org.malacca.exception.MessagingException;
import org.malacca.exception.RetryMessageHandlerException;
import org.malacca.messaging.Message;
import org.malacca.retry.RetryComponent;
import org.malacca.retry.RetryContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * Description: malacca-ee
 * <p>
 * Created by chensheng on 2021/5/24
 * <p>
 * © 2021. DimensionX B.V. 保留所有权利
 */
public abstract class AbstractRetryComponent extends AbstractAdvancedComponent implements RetryComponent {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRetryComponent.class);
    private boolean retry;
    private RetryContext retryContext;
    private RetryTemplate retryTemplate;

    public AbstractRetryComponent(String id, String name) {
        super(id, name);
    }

    @Override
    public Message handleMessage(Message<?> message) throws MessagingException {
        try {
            if (isRetry()) {
                return doWithRetry(message);
            }
        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            throw new RetryMessageHandlerException("自动重推没有成功", e);
        }
        return handleSuperMessage(message);
    }

    private Message handleSuperMessage(Message<?> message) throws MessagingException {
        return super.handleMessage(message);
    }

    @Override
    public void setRetryContext(RetryContext retryContext) {
        this.retryContext = retryContext;
    }

    private RetryTemplate getRetryTemplate() {
        if (retryTemplate == null) {
            RetryTemplate retryTemplate = new RetryTemplate();
            SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(retryContext.getTimes());
            retryTemplate.setRetryPolicy(simpleRetryPolicy);
            this.retryTemplate = retryTemplate;
        }
        return this.retryTemplate;
    }

    private Message<?> doWithRetry(Message<?> message) throws Exception {
        Message<?> result = getRetryTemplate().execute(new RetryCallback<Message<?>, Exception>() {
            @Override
            public Message<?> doWithRetry(org.springframework.retry.RetryContext retryContext) throws Exception {
                return handleSuperMessage(message);
            }
        }, new RecoveryCallback<Message<?>>() {
            @Override
            public Message<?> recover(org.springframework.retry.RetryContext retryContext) throws Exception {
                LOG.info("兜底回调");
                return message;
            } //兜底回调

        });
        return result;
    }
    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }
}
