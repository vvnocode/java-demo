package com.vv.java.demo.kafka.producer.business;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.vv.java.demo.kafka.producer.constant.ProducerConstant;
import com.vv.java.demo.kafka.producer.factory.KafkaProducerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @description: 生产者demo
 * @creator vv
 * @date 2022/6/10 17:40
 */
@Slf4j
public class KafkaProducerDemo {

    public static final List<String> ALLOW_FILE_TYPE = Arrays.asList("txt", "json");

    public static void main(String[] args) {
        //kafka配置
        String kafkaStr = FileUtil.readUtf8String(
                Thread.currentThread().getContextClassLoader().getResource("").getPath()
                        + File.separator + ProducerConstant.CONFIG_FILE_NAME);
        JSONObject kafkaConf = JSONUtil.parseObj(kafkaStr);
        KafkaProducer<String, String> producer = KafkaProducerFactory.getProducer(kafkaConf);

        while (true) {
            try {
                String topicPath = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                        + File.separator + ProducerConstant.DIR_TOPIC_ROOT;
                boolean exist = FileUtil.exist(topicPath);
                if (!exist && !new File(topicPath).mkdir()) {
                    log.error("无法创建kafka目录，请手动创建");
                    break;
                }
                File[] ls = FileUtil.ls(Thread.currentThread().getContextClassLoader().getResource("").getPath()
                        + File.separator + ProducerConstant.DIR_TOPIC_ROOT);
                Arrays.stream(ls)
                        .filter(File::isDirectory)
                        .forEach(dir -> {
                            String topic = dir.getName();
                            //每个topic下面的文件
                            List<File> files = FileUtil.loopFiles(dir.getAbsolutePath());
                            if (files == null || files.size() == 0) {
                                return;
                            }
                            for (int i = 0; i < files.size(); i++) {
                                File file = files.get(i);
                                String fileName = file.getName();
                                //校验文件类型
                                String[] split = fileName.split("\\.");
                                if (split.length < 2) {
                                    continue;
                                }
                                String fileType = split[split.length - 1];
                                if (!ALLOW_FILE_TYPE.contains(fileType)) {
                                    continue;
                                }

                                String string = FileUtil.readUtf8String(file);
                                //如果调用send.get，异步发送变同步发送
                                producer.send(new ProducerRecord<>(topic, string));
                                log.info("已发送 TOPIC= {} MSG = {}", topic, string);
                                //删除文件
                                FileUtil.del(file);
                            }
                        });

            } catch (Exception e) {
                log.error("执行循环监听任务报错", e);
            }
        }
    }
}
