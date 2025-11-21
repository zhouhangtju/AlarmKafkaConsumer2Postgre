package com.mobile.outalarm2.kafka;

import com.alibaba.fastjson.JSON;
import com.mobile.outalarm2.dao.AlarmResultDao;
import com.mobile.outalarm2.db.AlarmResult;
import com.mobile.outalarm2.db.AlarmResultDB;
import com.mobile.outalarm2.util.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
@Transactional
public class KafkaConsumer {

    public static final String TOPIC_TEST = "asap_superset";


    @Autowired
    private AlarmResultDao alarmResultDao;

    @KafkaListener(topics = TOPIC_TEST, concurrency = "6")
    @Transactional
    public void topic_alarm(List<String> messages, Acknowledgment ack) {
        log.info("获取到kakfa消息条数{},线程{}", messages.size(), Thread.currentThread().getName());
        // System.out.printf("获取到kakfa消息条数");
        Optional message = Optional.ofNullable(messages);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 设置为东八区
        Map<String, List<AlarmResultDB>> dateGroupMap = new HashMap<>();
        try {
            if (message.isPresent()) {
                String tableDs = null;
                //  log.info("获取到kakfa消息条数{}", messages.size());
                for (int i = 0; i < messages.size(); i++) {
                    AlarmResult alarm = JSON.parseObject(messages.get(i), AlarmResult.class);
                    String dateHourPart = alarm.getCreateTime().substring(0, 13);
                    // 去除所有非数字字符，得到 "2025082516"
                    String ds = dateHourPart.replaceAll("[^0-9]", "");
                    tableDs = ds.substring(0, 8);
                    AlarmResultDB resultDB = new AlarmResultDB();
                    resultDB.setCreateTime(alarm.getCreateTime());
                    resultDB.setEventName(alarm.getEventName());
                    resultDB.setDeviceType(alarm.getDeviceType());
                    resultDB.setSrcIp(MyStringUtils.removeNullByte(alarm.getSrcIp()));
                    resultDB.setDstIp(MyStringUtils.removeNullByte(alarm.getDstIp()));
                    resultDB.setPayload(MyStringUtils.removeNullByte(alarm.getPayload()));
                    dateGroupMap.computeIfAbsent(tableDs, k -> new ArrayList<>()).add(resultDB);
                    // alarmResultDao.insert(resultDB, "alarm_" + tableDs);
                }
                // 按日期分组批量插入
                for (Map.Entry<String, List<AlarmResultDB>> entry : dateGroupMap.entrySet()) {
                    String tableDs2 = entry.getKey();
                    List<AlarmResultDB> dataList = entry.getValue();
                    if (!dataList.isEmpty() && !StringUtils.isEmpty(tableDs)) { // 避免空表名或空数据
                        alarmResultDao.insertBatch(dataList, "alarm_" + tableDs2);
                    }
                }
            }
        } catch (Exception e) {
            log.info("消费数据故障:{}", e);
        } finally {
            ack.acknowledge();
        }
    }



}