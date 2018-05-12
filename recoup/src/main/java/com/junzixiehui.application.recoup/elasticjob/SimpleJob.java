package com.junzixiehui.application.recoup.elasticjob;


import com.dangdang.ddframe.job.api.ShardingContext;
import com.google.common.collect.Lists;

import com.junzixiehui.application.core.constant.SymbolConstant;
import com.junzixiehui.application.recoup.dao.RecoupJobDao;
import com.junzixiehui.application.recoup.executor.RecoupJobExecuter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 业务补偿JOB入口,扩展自Elastic-job,并依赖spring
 * 框架内部使用，外部不需要基础继承
 */
@Slf4j
//@Component
public final class SimpleJob implements com.dangdang.ddframe.job.api.simple.SimpleJob {

    @Autowired
    private RecoupJobDao recoupJobDao;
    @Autowired
    private RecoupJobExecuter recoupJobExecuter;



    @Override
    public void execute(ShardingContext context) {
        StringBuilder logSB = new StringBuilder();
        long start = System.currentTimeMillis();

        logSB.append("[通用补偿作业入口]RecoupJob Start").append(SymbolConstant.BAR);
        logSB.append("ShardingContext:" + context.toString()).append(SymbolConstant.BAR);

        //得到分片项
        Integer shardingItems = context.getShardingItem();
        if (shardingItems == 0) {
            logSB.append("该服务器未分配到分片项，放弃执行");
            log.warn(logSB.toString());
            return;
        }

        //得到待处理的所有job Id
        List<Long> toBeExecutedIdList = recoupJobDao.getAllToBeExecutedIdList();
        if (toBeExecutedIdList == null || toBeExecutedIdList.isEmpty()) {
            logSB.append("未找到待执行或执行中的作业，放弃执行");
            log.info(logSB.toString());
            return;
        }

        //处理分片项和实际数据的对应关系
        List<Long> realToBeExecutedIdList = Lists.newArrayList();
        int shardingTotalCount = context.getShardingTotalCount();//分片总数
        for (Long id : toBeExecutedIdList) {
            int shardingItem = id.intValue() % shardingTotalCount;
            if (shardingItems == shardingItem) {
                realToBeExecutedIdList.add(id);
            }
        }

        //本次需要处理的补偿job id
        logSB.append("Execute job count:" + realToBeExecutedIdList.size()).append(SymbolConstant.BAR);
        for (Long id : realToBeExecutedIdList) {
            this.processOne(id);
        }

        logSB.append("Recoup Job End").append(SymbolConstant.BAR);
        logSB.append("Time:" + (System.currentTimeMillis() - start)).append(SymbolConstant.BAR);
        log.info(logSB.toString());
    }

    private void processOne(long recoupJobId) {
        recoupJobExecuter.execute(recoupJobId);
    }
}
