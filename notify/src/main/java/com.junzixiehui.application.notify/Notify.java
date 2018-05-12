package com.junzixiehui.application.notify;



import com.junzixiehui.application.core.util.DateUtils;
import com.junzixiehui.application.core.util.EnvHelper;
import com.junzixiehui.application.core.util.IpUtil;
import com.junzixiehui.application.core.util.SpringContextUtil;
import com.junzixiehui.application.notify.dingding.DingTalkMarkdownMessage;
import com.junzixiehui.application.notify.message.DingTalkNotifier;
import com.junzixiehui.application.trace.TraceContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * <p>Description: 播报工具类 - 统一调用这发送订钉钉消息</p>
 *
 * @author: by qulibin
 * @date: 2018/4/11  15:23
 * @version: 1.0
 */
@Slf4j
public class Notify {

    /**
     * @param notifyFlag NotifyMessageVo.NotifyFlagEnum
     * @author: qulibin
     * @description: 播报报警
     * @date: 15:25 2018/4/11
     * @return:
     */
    public static void broadcast(String title, String text, String notifyFlag) {
        DingTalkNotifier dingTalkNotifier = SpringContextUtil.getBean(DingTalkNotifier.class);


        StringBuilder content = new StringBuilder(text);
        content.append(1)
                .append(". ")
                .append("TraceId:[")
                .append(TraceContext.getTraceId()).append("]")
                .append("\n");

        content.append(2)
                .append(". ")
                .append("HostName:[")
                .append(IpUtil.getHostName()).append("]")
                .append("\n");

        DingTalkMarkdownMessage msg = DingTalkMarkdownMessage.builder()
                .title(buildTitle(title))
                .text(content.toString())
                .build();

        if (NotifyMessageVo.NotifyFlagEnum.METRICS.getCode().equals(notifyFlag)) {
            dingTalkNotifier.broadcastMarkdownMessageV2(msg);
        } else {
            dingTalkNotifier.broadcastMarkdownMessage(msg);
        }

    }

    /**
     * @param url @see com.renrenche.aftersale.order.notify.DingDingGroup
     * @author: qulibin
     * @description: 发送到指定的钉钉群
     * @date: 15:25 2018/4/11
     * @return:
     */
    public static void customBroadcast(String url, String title, String text) {
        if (!EnvHelper.isPro()) {
            log.info("非线上环境不发钉钉消息！" + url + " |title:" + title + "|text:" + text);
            return;
        }

        DingTalkNotifier dingTalkNotifier = SpringContextUtil.getBean(DingTalkNotifier.class);

        StringBuilder content = new StringBuilder(text);
        content.append(1)
                .append(". ")
                .append("TraceId:[")
                .append(TraceContext.getTraceId()).append("]")
                .append("\n");

        content.append(2)
                .append(". ")
                .append("HostName:[")
                .append(IpUtil.getHostName()).append("]")
                .append("\n");

        DingTalkMarkdownMessage msg = DingTalkMarkdownMessage.builder()
                .title(buildTitle(title))
                .text(content.toString())
                .build();

        dingTalkNotifier.customBroadcastMarkdownMessage(url, msg);
    }


    public static void broadcast(NotifyMessageVo notifyMessageVo) {
        DingTalkNotifier dingTalkNotifier = SpringContextUtil.getBean(DingTalkNotifier.class);

        StringBuilder text = new StringBuilder();

        text.append(1)
                .append(". ")
                .append("接口详情:")
                .append(notifyMessageVo.getInterfaceDetail())
                .append("\n");

        text.append(2)
                .append(". ")
                .append("异常详情:")
                .append(notifyMessageVo.getExceptionDetail())
                .append("\n");

        text.append(3)
                .append(". ")
                .append("响应详情:")
                .append(notifyMessageVo.getResponseDetail())
                .append("\n");

        text.append(4)
                .append(". ")
                .append("TraceId:[")
                .append(TraceContext.getTraceId()).append("]")
                .append("\n");

        text.append(5)
                .append(". ")
                .append("HostName:[")
                .append(IpUtil.getHostName()).append("]")
                .append("\n");


        DingTalkMarkdownMessage msg = DingTalkMarkdownMessage.builder()
                .title(buildTitle(notifyMessageVo.getTitle()))
                .text(text.toString())
                .build();

        if (NotifyMessageVo.NotifyFlagEnum.METRICS.getCode().equals(notifyMessageVo.getNotifyFlag())) {
            dingTalkNotifier.broadcastMarkdownMessageV2(msg);
        } else {
            dingTalkNotifier.broadcastMarkdownMessage(msg);
        }

    }


    /**
     * @param notifyFlag NotifyMessageVo.NotifyFlagEnum
     * @author: qulibin
     * @description: 自定义播报
     * @date: 15:25 2018/4/11
     * @return:
     */
    public static void broadcast(String title, String notifyFlag, Map paramMap) {
        DingTalkNotifier dingTalkNotifier = SpringContextUtil.getBean(DingTalkNotifier.class);
        StringBuilder text = new StringBuilder();

        paramMap.forEach((k, v) -> {
            text.append(1)
                    .append(". ")
                    .append(k)
                    .append(v)
                    .append("\n");
        });
        DingTalkMarkdownMessage msg = DingTalkMarkdownMessage.builder()
                .title(buildTitle(title))
                .text(text.toString())
                .build();

        if (NotifyMessageVo.NotifyFlagEnum.METRICS.getCode().equals(notifyFlag)) {
            dingTalkNotifier.broadcastMarkdownMessageV2(msg);
        } else {
            dingTalkNotifier.broadcastMarkdownMessage(msg);
        }

    }


    private static String buildTitle(String title) {
        return DateUtils.currentTimeSecond() + ": " + title;
    }


}
