package com.junzixiehui.application.notify;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Description: 播报对象--值对象 </p>
 *
 * @author: by qulibin
 * @date: 2018/4/11  15:38
 * @version: 1.0
 */
@Getter
@Setter
@ToString
public class NotifyMessageVo {

    public NotifyMessageVo(){}

    private String title;
    private String interfaceDetail;
    private String exceptionDetail;
    private String responseDetail;
    private NotifyFlagEnum notifyFlag = NotifyFlagEnum.ALARM;


    public NotifyMessageVo title(String title){
        this.setTitle(title);
        return this;
    }

    public NotifyMessageVo interfaceDetail(String interfaceDetail){
        this.setInterfaceDetail(interfaceDetail);
        return this;
    }

    public NotifyMessageVo exceptionDetail(String exceptionDetail){
        this.setExceptionDetail(exceptionDetail);
        return this;
    }

    public NotifyMessageVo responseDetail(String responseDetail){
        this.setResponseDetail(responseDetail);
        return this;
    }

    public NotifyMessageVo notifyFlag(NotifyFlagEnum notifyFlag){
        this.setNotifyFlag(notifyFlag);
        return this;
    }


    public static NotifyMessageVo builder() {
        return new NotifyMessageVo();
    }



    public enum NotifyFlagEnum {
        METRICS("metrics", "指标播报"),
        ALARM("alarm", "报警播报");

        NotifyFlagEnum(String code, String name) {
           this.code = code;
           this.name = name;
        }

        @Getter
        private String code;
        @Getter
        private String name;
    }


}
