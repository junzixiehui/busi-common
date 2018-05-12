package com.junzixiehui.application.recoup.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 通用业务补偿job实体
 */
@Setter
@Getter
@ToString
public class RecoupJobEntity {
    private long id;
    //执行补偿业务逻辑的类
    private String jobClass;
    //补偿job的名称
    private String jobName;
    //补偿job的描述
    private String jobDesc;
    //jobClass的业务参数json串
    private String jobJsonParam;
    //补偿job状态 @see JobStatusEnum
    private String jobStatus;
    //补偿job执行开始时间,绝对时间如20160720121212
    private long startTime;
    //补偿job执行完成时间,绝对时间如20160720121212
    private long completeTime;
    //补偿截止时间,绝对时间如20160720121212，超过该时间没成功放弃补偿
    private long effectTime;
    //当前重试次数
    private int retryCurTimes;
    //重试总次数,超过该次数还没有补偿成功自动放弃补偿
    private int retryTotalTimes;
    //执行失败原因
    private String failReason;
    //是否开启异步执行,0同步执行 1异步执行
    private int async;
    //设置业务编码，需要业务保证唯一
    private String busCode;
    //执行失败报警标志0 未报警 1已报警
    private int failAlarmFlag;


    //补偿job创建时间,绝对时间如20160720121212
    private long createTime;
    private long updateTime;


}
