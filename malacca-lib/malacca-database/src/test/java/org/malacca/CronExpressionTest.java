package org.malacca;


import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/8/16
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class CronExpressionTest {

    public static void main(String[] args) throws ParseException {
        String cron = "0 */10 * ? * *";
        CronExpression cronExpression = new CronExpression(cron);
        Date date = new Date();
        System.out.println(cronExpression.getNextValidTimeAfter(date));
        System.out.println(cronExpression.getTimeBefore(date));
    }

}
