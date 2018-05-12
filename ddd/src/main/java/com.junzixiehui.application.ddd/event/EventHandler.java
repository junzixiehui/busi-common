package com.junzixiehui.application.ddd.event;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;


@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface EventHandler {
}
