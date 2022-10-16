package com.vv.java.demo.kafka.consumer.store;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @creator vv
 * @date 2022/5/19 10:43
 */
public class KafkaConsumerStore {

    /**
     * 缓存下消费者，在关闭钩子函数中使用。
     * //todo 现在生产环境就只需要消费一个topic，后面如果要消费多个消费者可考虑下是否把每个topic单独消费。
     */
    public static List<KafkaConsumer<String, String>> usingConsumer = new ArrayList<>(10);
}
