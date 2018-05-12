package com.junzixiehui.application.trace.log;



import com.junzixiehui.application.trace.TraceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统统一日志接口，提供常用的日志方法 ，如果需要扩展需要继承该类 <br>
 * 目前只统一记录错误日志
 *
 * @author chengys4
 * @version 0.1
 */
public class ApplicationLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationLogger.class);
    private static final Logger ERROR_LOGGER = LoggerFactory.getLogger("ERROR_LOGGER");
    private static final Logger PROVIDER_LOGGER = LoggerFactory.getLogger("PROVIDER_LOGGER");
    private static final Logger CONSUME_LOGGER = LoggerFactory.getLogger("CONSUMER_LOGGER");
    private static final Logger QUARTZ_LOGGER = LoggerFactory.getLogger("QUARTZ_LOGGER");
    private static final Logger AUDIT_LOGGER = LoggerFactory.getLogger("AUDIT_LOGGER");

    private ApplicationLogger() {
    }

    /**
     * 记录系统错误日志
     *
     * @param throwable
     */
    public static void error(Throwable throwable) {
        error(null, throwable);
    }

    /**
     * 记录系统错误日志
     *
     * @param errorMsg
     * @param throwable
     */
    public static void error(String errorMsg, Throwable throwable) {
        StringBuilder errorMsgToUse = new StringBuilder();
        errorMsgToUse.append(TraceContext.getTraceIdLog());
        errorMsgToUse.append(errorMsg);

        if (errorMsg == null || errorMsg.trim().length() == 0) {
            StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
            errorMsgToUse.append(ste.getClassName() + "." + ste.getMethodName() + " error");
        }
        Logger loggerToUse = (ERROR_LOGGER != null) ? ERROR_LOGGER : LOGGER;
        if (throwable == null) {
            loggerToUse.error(errorMsgToUse.toString());
        } else {
            loggerToUse.error(errorMsgToUse.toString(), throwable);
        }

    }


    /**
     * 记录系统错误日志
     *
     * @param errorMsg
     */
    public static void error(String errorMsg) {
        error(errorMsg,null);
    }

    /**
     * 记录普通日志
     *
     * @param classmodel 要记录日志的类
     * @param infomsg    日志说明
     */
    public static void infoClassLog(Class<?> classmodel, String infomsg) {
        LoggerFactory.getLogger(classmodel).info(infomsg);
    }

    /**
     * 记录普通日志
     *
     * @param classModel 要记录日志的类
     * @param info    日志说明
     */
    public static void info(Class<?> classModel, String info) {
        LoggerFactory.getLogger(classModel).info(info);
    }


    /**
     * 记录系统中接口被调用时产生的日志
     *
     * @param paramString
     */
    public static void infoInterfaceProvider(String paramString) {
        if (PROVIDER_LOGGER == null) {
            LOGGER.info(paramString);
            return;
        }
        PROVIDER_LOGGER.info(paramString);
    }

    /**
     * 记录系统调用外部接口产生的日志
     *
     * @param paramString
     */
    public static void infoInterfaceConsum(String paramString) {
        if (CONSUME_LOGGER == null) {
            LOGGER.info(paramString);
            return;
        }
        CONSUME_LOGGER.info(paramString);
    }

    /**
     * 记录定时任务产生的日志
     *
     * @param paramString
     */
    public static void infoQuartz(String paramString) {
        if (QUARTZ_LOGGER == null) {
            LOGGER.info(paramString);
            return;
        }
        QUARTZ_LOGGER.info(paramString);
    }

    public static void infoQuartz(Class<?> classmodel, String paramString) {
        if (QUARTZ_LOGGER == null) {
            LOGGER.info(paramString);
            return;
        }
        QUARTZ_LOGGER.info(classmodel.getSimpleName() + "|" + paramString);
    }

    /**
     * 记录定时任务产生的日志
     *
     * @param paramString
     */
    public static void infoAudit(String paramString) {
        if (AUDIT_LOGGER == null) {
            LOGGER.info(paramString);
            return;
        }
        AUDIT_LOGGER.info(paramString);
    }
}
