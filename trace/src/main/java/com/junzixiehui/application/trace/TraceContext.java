package com.junzixiehui.application.trace;


import com.junzixiehui.application.core.constant.SymbolConstant;
import com.junzixiehui.application.trace.entity.Span;

/**
 * <p>Description: </p>
 *
 * @author: by qulibin
 * @date: 2017/12/29  14:34
 * @version: 1.0
 */
public class TraceContext {


    // 传递parentSpan
    private static ThreadLocal<Span> spanThreadLocal = new InheritableThreadLocal<Span>();



    public static void removeSpan() {
        spanThreadLocal.remove();
    }

    public static Span getSpan() {
        return spanThreadLocal.get();
    }

    public static void setSpan(final Span span) {
        spanThreadLocal.set(span);
    }


    public static void clear(){
        spanThreadLocal.remove();
    }


    public static String getTraceId() {
        Span span = getSpan();
        if (span == null){
            return SymbolConstant.EMPTY;
        }
        return span.getTraceId();
    }


    public static String getTraceIdLog() {
        Span span = getSpan();
        if (span == null){
            return SymbolConstant.EMPTY;
        }
        StringBuilder sb = new StringBuilder("|TraceId:").append("【").append(span.getTraceId()).append("】");
        return sb.toString();
    }



}
