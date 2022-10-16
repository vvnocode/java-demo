package com.vv.java.demo.kafka.producer.factory;


import cn.hutool.json.JSONObject;
import com.vv.java.demo.kafka.producer.constant.ProducerConstant;
import com.vv.java.demo.kafka.producer.store.KafkaProducerStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

/**
 * kafka生产者工厂
 */
@Slf4j
public class KafkaProducerFactory {

    private static void init(JSONObject kafkaConf) {
        //初始化
        JSONObject init = kafkaConf.getByPath(ProducerConstant.CONFIG_INIT, JSONObject.class);
        Properties props = new Properties();
        init.entrySet().stream().forEach(set -> props.put(set.getKey(), set.getValue()));
        KafkaProducerStore.producer = new KafkaProducer<>(props);
    }

    public static KafkaProducer<String, String> getProducer(JSONObject kafkaConf) {
        if (KafkaProducerStore.producer == null) {
            init(kafkaConf);
        }
        return KafkaProducerStore.producer;
    }

}
