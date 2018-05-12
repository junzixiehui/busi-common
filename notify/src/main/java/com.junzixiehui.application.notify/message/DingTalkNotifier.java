package com.junzixiehui.application.notify.message;




import com.junzixiehui.application.core.thread.NamedThreadFactory;
import com.junzixiehui.application.notify.dingding.DingTalkMarkdownMessage;
import com.junzixiehui.application.trace.http.HttpClientUtils;
import com.junzixiehui.application.trace.log.ApplicationLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: qulibin
 * @description: 钉钉工具
 * @date: 15:25 2017/12/22
 * @modify：
 */
@Service
@Slf4j
public class DingTalkNotifier {
    @Autowired
    private HttpClientUtils httpClientUtils;

    @Value("${dingding.webhookUrl.aftersale_order_metrics}")
    private String dingtalkWebhookUrl;


    @Value("${dingding.webhookUrl.write_migration_broadcast}")
    private String dingtalkWebhookUrlWriteMigrationBroadcast;

    int threadLen = Runtime.getRuntime().availableProcessors();

    private final ExecutorService dingdingExecuter = Executors.newFixedThreadPool(threadLen, new NamedThreadFactory("DingTalk-Broadcast-Pool"));


    /**
     * @author: qulibin
     * @description: 售后订单中心监控报警-包括错误
     * @date: 15:10 2018/3/22
     * @return:
     */
    public void broadcastMarkdownMessage(DingTalkMarkdownMessage msg) {
        dingdingExecuter.submit(() -> {
            try {
                httpClientUtils.httpPostJson(dingtalkWebhookUrl, msg.toString());
            } catch (Exception e) {
                ApplicationLogger.error("broadcastMarkdownMessageV2 error", e);
            }
        });
    }



    /**
     * @author: qulibin
     * @description: 售后订单中心指标播报-数据对比等
     * @date: 15:10 2018/3/22
     * @return:
     */
    public void broadcastMarkdownMessageV2(DingTalkMarkdownMessage msg) {
        dingdingExecuter.submit(() -> {
            try {
                httpClientUtils.httpPostJson(dingtalkWebhookUrlWriteMigrationBroadcast, msg.toString());
            } catch (Exception e) {
                ApplicationLogger.error("broadcastMarkdownMessageV2 error", e);
            }
        });
    }


    /**
     * @author: qulibin
     * @description:  自定义发送到指定群
     * @date: 15:10 2018/3/22
     * @return:
     */
    public void customBroadcastMarkdownMessage(String dingtalkWebhookUrl,DingTalkMarkdownMessage msg) {
        dingdingExecuter.submit(() -> {
            try {
                httpClientUtils.httpPostJson(dingtalkWebhookUrl, msg.toString());
            } catch (Exception e) {
                ApplicationLogger.error("customBroadcastMarkdownMessage error", e);
            }
        });
    }



}
