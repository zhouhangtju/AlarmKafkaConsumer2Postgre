package com.mobile.outalarm2.db;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
public class AlarmResult {

    @JSONField(name = "CREATE_TIME")
    private String createTime;

    @JSONField(name = "EVENT_NAME")
    private String eventName;
    @JSONField(name = "PAYLOAD")
    private String payload;

    @JSONField(name = "DEVICE_TYPE")
    private String deviceType;

    @JSONField(name = "SRC_IP")
    private String srcIp;
    @JSONField(name = "DST_IP")
    private String dstIp;

    @JSONField(name = "REQUEST_PAYLOAD")
    private String resquestPayload;

    @JSONField(name = "RESPONSE_MESSAGE")
    private String resquestMessage;

    @JSONField(name = "RESPONSE_CODE")
    private String responseCode;

    @JSONField(name = "RESPONSE_MESSAGE")
    private String responseMessage;


}
