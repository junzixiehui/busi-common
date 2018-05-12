package com.junzixiehui.application.ddd.enums;

import lombok.Getter;

/**
 * @author: qulibin
 * @description:
 * @date: 16:50 2018/4/16
 * @modify：
 */
public enum EventType {
    CREATE("CREATE","CREATE"),
    UPDATE("CREATE","CREATE"),
    DELETE("DELETE","DELETE");


    EventType(String code, String name) {
        this.code = code;
        this.name = name;
    }


    @Getter
    private String code;
    @Getter
    private String name;
}
