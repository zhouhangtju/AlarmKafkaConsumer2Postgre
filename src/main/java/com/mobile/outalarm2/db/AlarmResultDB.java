package com.mobile.outalarm2.db;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class AlarmResultDB {

    @TableField("create_time")
    private String createTime;

    @TableField("event_name")
    private String eventName;
    @TableField("device_type")
    private String deviceType;
    @TableField("payload")
    private String payload;

    @TableField("src_ip")
    private String srcIp;
    @TableField("dst_ip")
    private String dstIp;

    @TableField("resquest_payload")
    private String resquestPayload;

    @TableField("resquest_message")
    private String resquestMessage;

    @TableField("response_code")
    private String responseCode;

    @TableField("response_message")
    private String responseMessage;
}
