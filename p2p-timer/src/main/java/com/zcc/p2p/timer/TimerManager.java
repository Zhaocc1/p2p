package com.zcc.p2p.timer;

import com.zcc.p2p.service.user.IncomeRecordService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author:zcc
 * @data:2019/3/1 0001
 */
@Component
public class TimerManager {

    Logger logger = LogManager.getLogger(TimerManager.class);

    @Autowired
    private IncomeRecordService incomeRecordService;

    //@Scheduled(cron = "0/5 * * * * ?")
    public void generateIncomePlan(){

        logger.info("开始生成收益--------------");

        incomeRecordService.generateIncomePlan();

        logger.info("生成收益结束--------------");

    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void generateIncomeBack(){

        logger.info("返还开始-----------------");

        incomeRecordService.generateIncomeBack();

        logger.info("返还结束-----------------");

    }

}
