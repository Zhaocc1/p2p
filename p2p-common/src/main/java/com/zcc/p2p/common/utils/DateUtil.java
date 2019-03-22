package com.zcc.p2p.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description:
 * @author:zcc
 * @data:2019/3/1 0001
 */

public class DateUtil {

    /**
     * 获取指定日期之后n天的日期
     * @param date
     * @param days
     * @return
     */
    public static Date getDateAfterDays(Date date,Integer days){


        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(Calendar.DATE,days);

        return calendar.getTime();

    }

    /**
     * 获取指定日期N个月之后的日期
     * @param date
     * @param months
     * @return
     */
    public static Date getDateAfterMonths(Date date,Integer months){

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(Calendar.MONTH,months);

        return calendar.getTime();

    }

    //时间戳
    public static String getTimeStamp(){

        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

    }

    public static void main(String[] args) {

        Date d = DateUtil.getDateAfterDays(new Date(),1);
        System.out.println(d);

        d = DateUtil.getDateAfterMonths(new Date(),1);
        System.out.println(d);

    }

}
