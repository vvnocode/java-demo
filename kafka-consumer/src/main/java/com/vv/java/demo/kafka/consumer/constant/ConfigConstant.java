package com.vv.java.demo.kafka.consumer.constant;

/**
 * @description:
 * @creator vv
 * @date 2022/6/10 17:20
 */
public class ConfigConstant {
    /**
     * kafka配置文件
     */
    public static final String CONFIG_FILE_NAME = "kafka.json";

    /**
     * 初始化参数
     */
    public static final String CONFIG_INIT = "consumer.init";

    /**
     * kafka topic订阅配置(Map<String,String>)。{别名:TOPIC}
     */
    public static final String CONSUMER_TOPIC = "consumer.topic";

    /**
     * kafka 消费者数量(int)，最好和partition一致
     */
    public static final String CONSUMER_THREAD = "consumer.thread";
}
