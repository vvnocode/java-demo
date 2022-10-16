package com.vv.java.demo.kafka.producer.store;

import org.apache.kafka.clients.producer.KafkaProducer;

/**
 * @description:
 * @creator vv
 * @date 2022/6/10 17:46
 */
public class KafkaProducerStore {

    /**
     * 生产者暂时使用单例模式，
     * //todo 后面有需求再考虑多个生产者
     */
    public static KafkaProducer<String, String> producer;
}
