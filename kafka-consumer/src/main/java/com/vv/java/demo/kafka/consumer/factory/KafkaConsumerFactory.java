package com.vv.java.demo.kafka.consumer.factory;

import cn.hutool.json.JSONObject;
import com.vv.java.demo.kafka.consumer.constant.ConfigConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;

/**
 * @description: kafka消费者工厂，按需生成多个。
 * @creator vv
 * @date 2022/5/17 17:21
 */
@Slf4j
public class KafkaConsumerFactory {

    /**
     * 根据指定的配置文件生成一个kafka消费者
     * @param kafkaConf
     * @return
     */
    public static KafkaConsumer<String, String> getOneConsumer(JSONObject kafkaConf) {
        //初始化参数
        JSONObject init = kafkaConf.getByPath(ConfigConstant.CONFIG_INIT, JSONObject.class);
        Properties props = new Properties();
        init.entrySet().stream().forEach(set -> props.put(set.getKey(), set.getValue()));
        return new KafkaConsumer<>(props);
    }

}
