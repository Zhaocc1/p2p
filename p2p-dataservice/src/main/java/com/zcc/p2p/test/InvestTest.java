package com.zcc.p2p.test;

import com.zcc.p2p.service.loan.BidInfoService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author:zcc
 * @data:2019/3/2 0002
 */
public class InvestTest {

    public static void main(String[] args) {

        //TODO investTest：并发测试
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        BidInfoService bidInfoService = (BidInfoService) applicationContext.getBean("bidInfoServiceImpl");

        Map<String,Object> paramMap = new HashMap<>();

        paramMap.put("uid",39);
        paramMap.put("bidMoney",1);
        paramMap.put("loanId",1);

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        for(int i=0;i<1000;i++){

            executorService.submit(new Callable<Object>() {

                @Override
                public Object call() throws Exception {
                    return bidInfoService.invest(paramMap);
                }
            });

        }

        executorService.shutdown();
    }

}
