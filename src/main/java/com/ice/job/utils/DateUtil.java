package com.ice.job.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 日期工具类
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/3/13 22:07
 */
public class DateUtil {

    public static long daysBetween(Date compareTime, Date currentDate) {
        long diffInMillies = Math.abs(currentDate.getTime() - compareTime.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
