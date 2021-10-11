package org.malacca.metric;

import com.codahale.metrics.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/4/10
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class MetricHelper {
    private Map<String, Meter> metricMap = new HashMap<>();
    private MetricRegistry metrics = new MetricRegistry();
    private Map<String, Timer> timerMap = new HashMap<>();

    public static final String JMX_PRE_KEY = "malaccaJmx";
    public static final String SERVICE_KEY = "serviceId";
    public static final String COMPONENT_KEY = "componentId";
    public static final String TYPE_KEY = "type";

    public MetricHelper() {
        ConsoleReporter build = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        build.start(1, TimeUnit.MINUTES);

        JmxReporter reporter = JmxReporter.forRegistry(metrics).build();
        reporter.start();
    }

    public Meter getMeter(String serviceId, String componentId) {
        String name = getName(serviceId, componentId, "metric");
        if (!metricMap.containsKey(name)) {
            metricMap.put(name, metrics.meter(name));
        }
        return metricMap.get(name);
    }

    public Timer getTimer(String serviceId, String componentId) {
        String name = getName(serviceId, componentId, "timer");
        if (!timerMap.containsKey(name)) {
            timerMap.put(name, metrics.timer(name));
        }
        return timerMap.get(name);
    }

    public String getName(String serviceId, String componentId, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append(serviceId).append("-")
                .append(componentId).append("-").append(type);
        return sb.toString();
    }
}
