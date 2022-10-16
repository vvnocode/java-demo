package com.vv.java.demo.kafka.consumer.business;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.vv.java.demo.kafka.consumer.constant.ConfigConstant;
import com.vv.java.demo.kafka.consumer.factory.KafkaConsumerFactory;
import com.vv.java.demo.kafka.consumer.store.KafkaConsumerStore;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @description: 多线程消费demo
 * @creator vv
 * @date 2022/5/18 17:54
 */
@Slf4j
@NoArgsConstructor
public class KafkaConsumerDemo implements Runnable {

    private KafkaConsumer<String, String> consumer;

    private JSONObject kafkaConf;

    public KafkaConsumerDemo(JSONObject kafkaConf) {
        this.kafkaConf = kafkaConf;
    }

    @Override
    public void run() {
        //生成kafka消费者
        this.consumer = KafkaConsumerFactory.getOneConsumer(kafkaConf);
        //加入到缓存，可以在关闭的时候要用（非必要）
        KafkaConsumerStore.usingConsumer.add(consumer);
        //在这里订阅
        Map<String, String> topicMap = kafkaConf.getByPath(ConfigConstant.CONSUMER_TOPIC, Map.class);
        Set<String> collect = topicMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
        this.consumer.subscribe(collect);
        consumerTopic(this.consumer);
    }

    public void consumerTopic(KafkaConsumer<String, String> consumer) {
        ConsumerRecords<String, String> msgList;
        try {
            for (; ; ) {
                msgList = consumer.poll(1000);
                if (null == msgList || msgList.count() == 0) {
                    continue;
                }
                log.debug("消费情况--消费到 {} 条数据", msgList.count());
                for (ConsumerRecord<String, String> record : msgList) {
                    //因为配置文件支持每种数据类型接收多个topic，这里可以根据实际情况判断下topic
                    String topic = record.topic();
                    String value = record.value();
                    if (StringUtils.isBlank(value)) {
                        continue;
                    }
                    log.debug("消费情况--收到TOPIC= {} 的一条数据 {}", topic, value);
                }
                //手动异步提交
                consumer.commitAsync();
            }
        } catch (WakeupException e) {
            log.error("关闭消费者");
        } catch (Exception e) {
            log.error("消费人脸抓拍数据报错", e);
        } finally {
            try {
                consumer.commitSync();
                consumer.close();
            } catch (WakeupException e) {
            } catch (Exception e) {
                log.error("关闭消费者出现未知异常", e);
            } finally {
                //这里可能要处理些业务逻辑
            }
        }
    }

    public static void main(String[] args) {
        //线程池。生产环境推荐手动创建线程池，效果会更好。
        ExecutorService executor = Executors.newFixedThreadPool(10);
        //kafka配置
        String kafkaStr = FileUtil.readUtf8String(
                Thread.currentThread().getContextClassLoader().getResource("").getPath()
                        + File.separator + ConfigConstant.CONFIG_FILE_NAME);
        JSONObject kafkaConf = JSONUtil.parseObj(kafkaStr);
        //多线程消费
        int consumerCount = kafkaConf.getByPath(ConfigConstant.CONSUMER_THREAD, Integer.class);
        if (KafkaConsumerStore.usingConsumer.size() < consumerCount) {
            //任务加入线程池
            for (int i = 0; i < consumerCount - KafkaConsumerStore.usingConsumer.size(); i++) {
                executor.execute(new KafkaConsumerDemo(kafkaConf));
            }
        }

    }

}
