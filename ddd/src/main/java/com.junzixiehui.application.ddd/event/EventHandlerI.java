package com.junzixiehui.application.ddd.event;


import com.junzixiehui.application.core.api.Resp;

/**
 * event handler
 *
 */
public interface EventHandlerI<R extends Resp, E extends Event> {

    public R execute(E e);
}
