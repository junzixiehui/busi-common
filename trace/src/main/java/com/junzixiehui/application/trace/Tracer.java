package com.junzixiehui.application.trace;


import com.junzixiehui.application.trace.constant.Header;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * <p>Description: </p>
 *
 * @author: by qulibin
 * @date: 2018/5/7  15:37
 * @version: 1.0
 */
public class Tracer {


    public static String getTraceId(HttpServletRequest request) {
        String traceId = request.getHeader(Header.TRACE_ID);
        if (StringUtils.isBlank(traceId)){
            return "";
        }
        return traceId;
    }


    public static String genTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
