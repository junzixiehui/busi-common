package com.junzixiehui.application.ddd.event;




import com.junzixiehui.application.core.api.Resp;
import com.junzixiehui.application.ddd.exception.InfraException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Event Bus
 *
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
@Component
public class EventBus implements EventBusI{
    Logger logger = LoggerFactory.getLogger(EventBus.class);

    @Autowired
    private EventHub eventHub;

    @SuppressWarnings("unchecked")
    @Override
    public Resp fire(Event event) {
        Resp response = null;
        try {
            response = eventHub.getEventHandler(event.getClass()).execute(event);
        }catch (Exception exception) {
            response = handleException(event, response, exception);
        }
        return response;
    }

    private Resp handleException(Event cmd, Resp response, Exception exception) {
        Class responseClz = eventHub.getResponseRepository().get(cmd.getClass());
        try {
            response = (Resp) responseClz.newInstance();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InfraException(e.getMessage());
        }
        logger.error(exception.getMessage(), exception);
        return response;
    }
}
