package com.junzixiehui.application.recoup.elasticjob;


import com.dangdang.ddframe.job.api.ShardingContext;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 普通定时作业抽象类，提供自动处理分片数据
 */
@Component
public abstract class AbstractSimpleJob implements com.dangdang.ddframe.job.api.simple.SimpleJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSimpleJob.class);
    private JobConfig jobConfig;

    /**
     * job配置
     */
    @PostConstruct
    private void init() {
        this.jobConfig = getJobConfig();
    }

    protected abstract JobConfig getJobConfig();

    /**
     * 得到所有分片的待处理任务
     *
     * @return
     */
    protected abstract List<Long/*id*/> getAllShardDatas() throws Exception;

    /**
     * 分片后本节点处理的任务
     *
     * @return
     */
    protected abstract void processMyShardData(Long myShardDataId) throws Exception;

    /**
     * 默认分片项算法
     *
     * @param myShardDataId
     * @param shardingTotalCount
     * @return
     * @throws Exception
     */
    protected int getshardingItem(Long myShardDataId, int shardingTotalCount) throws Exception {
        int shardingItem = myShardDataId.intValue() % shardingTotalCount;
        return shardingItem;
    }

   /* @Override
    public void handleJobExecutionException(final JobException jobException) {
        LOGGER.error(this.jobConfig.getJobName(), jobException);
    }*/

    @Override
    public void execute(final ShardingContext context) {
        StringBuilder logSB = new StringBuilder();
        long start = System.currentTimeMillis();
        logSB.append("【").append(this.jobConfig.getJobName()).append("】Start").append("|");

        //得到分片项
        Integer shardingItems = context.getShardingItem();
        if (shardingItems == null || shardingItems == 0) {
            logSB.append("该服务器未分配到分片项，放弃执行");
            LOGGER.warn(logSB.toString());
            return;
        }
        logSB.append("得到的分片项：" + shardingItems.toString()).append("|");

        //得到所有待处理任务
        List<Long> allShardDatas = null;
        String exceptionMsg = "";
        try {
            allShardDatas = getAllShardDatas();
        } catch (Exception e) {
            exceptionMsg = "，getAllShardDatas异常：" + Throwables.getStackTraceAsString(e);
        }
        if (allShardDatas == null || allShardDatas.isEmpty()) {
            logSB.append("没有待处理的数据，放弃执行").append(exceptionMsg);
            LOGGER.warn(logSB.toString());
            return;
        }
        logSB.append("所有分片待处理的数据量:" + allShardDatas.size()).append("|");

        //得到该实际待处理数据
        List<Long> myShardDatas = Lists.newArrayList();
        int shardingTotalCount = context.getShardingTotalCount();
        try {
            for (Long id : allShardDatas) {
                int shardingItem = getshardingItem(id, shardingTotalCount);
                if (shardingItems == shardingItem) {
                    myShardDatas.add(id);
                }
            }
        } catch (Exception e) {
            exceptionMsg = "，getshardingItem异常：" + Throwables.getStackTraceAsString(e);
        }

        if (myShardDatas == null || myShardDatas.isEmpty()) {
            logSB.append("没有待处理的数据，放弃执行").append(exceptionMsg);
            LOGGER.warn(logSB.toString());
            return;
        }
        logSB.append("分片后待处理的数据量:" + myShardDatas.size()).append("|");

        //交给实际任务类执行
        for (Long myShardData : myShardDatas) {
            try {
                processMyShardData(myShardData);
            } catch (InterruptedException e1) {
                ;
            } catch (Exception e) {
                LOGGER.error(this.jobConfig.getJobName(), e);
            }
        }
        logSB.append("End").append("Time:" + (System.currentTimeMillis() - start));
        LOGGER.info(logSB.toString());
    }
}
