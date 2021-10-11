package org.malacca.component.limiter;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import org.malacca.component.AbstractAdvancedComponent;
import org.malacca.exception.MessagingException;
import org.malacca.exception.RateLimiterMessageHandlerException;
import org.malacca.messaging.Message;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimiterFilter extends AbstractAdvancedComponent {

    // 是否采用分布式存储许可
    private boolean isDistributed = false;

    // 如果选择分布式，输入请求接口（1.数据库api-server，2.待定）
    private String url = "http://localhost:8880/api/v1/acquire?engine_token=ZW5naW5lMQ==";

    // 获取令牌等待超时时间,timeout为0，则代表非阻塞，获取不到立即返回
    private int acquireTimeout = 1000;

    private int maxBlock = 2;

    private int permits = 1;

    private int seconds = 1;

    // 个/s
    private RateLimiter rateLimiter = RateLimiter.create(permits / seconds);

    private AtomicInteger blockCount = new AtomicInteger(0);

    public RateLimiterFilter(String id, String name) {
        super(id, name);
    }

    @Override
    protected Message doHandleMessage(Message<?> message) throws MessagingException {
        try {
            if (blockCount.get() >= maxBlock) {
                throw new RateLimiterMessageHandlerException(getId() + "过多阻塞请求，获取令牌失败");
            }
            blockCount.getAndIncrement();
            boolean acquire;
            if (isDistributed) {
                try {
                    JSONObject limitInfo = new JSONObject() {{
                        put("id", getId());
                        put("permits", permits);
                        put("seconds", seconds);
                    }};

                    String result = HttpUtil.get(url, limitInfo, acquireTimeout);
                    acquire = Boolean.parseBoolean(result);
                } catch (Exception e) {
                    acquire = false;
                }
            } else {
                acquire = rateLimiter.tryAcquire(acquireTimeout, TimeUnit.MILLISECONDS);
            }
            if (!acquire) {
                blockCount.decrementAndGet();
                throw new RateLimiterMessageHandlerException(getId() + "获取令牌失败");
            }
            blockCount.decrementAndGet();
            return message;
        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            throw new RateLimiterMessageHandlerException("限流组件执行失败", e);
        }
    }

    @Override
    public String getType() {
        return "rateLimiterFilter";
    }

    public boolean getIsDistributed() {
        return isDistributed;
    }

    public void setIsDistributed(boolean distributed) {
        isDistributed = distributed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPermits() {
        return permits;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setPermits(int permits) {
        this.permits = permits;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setRateLimiter(double permits, int seconds) {
        // 速率unit秒生成许可数
        rateLimiter = RateLimiter.create(permits / seconds);
    }

    public int getAcquireTimeout() {
        return acquireTimeout;
    }

    public void setAcquireTimeout(int acquireTimeout) {
        this.acquireTimeout = acquireTimeout;
    }

    public int getMaxBlock() {
        return maxBlock;
    }

    public void setMaxBlock(int maxBlock) {
        this.maxBlock = maxBlock;
        blockCount = new AtomicInteger(0);
    }
}
