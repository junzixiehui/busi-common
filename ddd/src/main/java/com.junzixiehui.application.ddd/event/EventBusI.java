package com.junzixiehui.application.ddd.event;


import com.junzixiehui.application.core.api.Resp;

/**
 * EventBus interface
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
public interface EventBusI {

    /**
     * Send event to EventBus
     * 
     * @param event
     * @return Response
     */
    public Resp fire(Event event);
}
