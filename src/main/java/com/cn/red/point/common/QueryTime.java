package com.cn.red.point.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class QueryTime {

    public static String queryToday(){
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格
        return dateFormat.format(now);
    }

    public static String queryTodaySec(){
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格
        return dateFormat.format(now);
    }

    public static String querySec(){
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");//可以方便地修改日期格
        return dateFormat.format(now);
    }

    public static String queryYesterday(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        return sp.format(cal.getTime());//获取昨天日期
    }

    public static String queryOneYearLater(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR,1);
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        return sp.format(cal.getTime());//获取昨天日期
    }

    public static String querySomeDay(int i) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,i);
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        return sp.format(cal.getTime());
    }

    public static String queryMonth(){
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");//可以方便地修改日期格
        return dateFormat.format(now);
    }

    public static long getDaySub(String beginDateStr,String endDateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = format.parse(beginDateStr);
        Date endDate = format.parse(endDateStr);
        return (endDate.getTime() - beginDate.getTime())/(24*60*60*1000);
    }

    public static long getDaySubSec(String beginDateStr,String endDateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginDate = format.parse(beginDateStr);
        Date endDate = format.parse(endDateStr);
        return (endDate.getTime() - beginDate.getTime())/(24*60*60*1000);
    }

//    public static void main(String[] args) throws ParseException {
//        String format = "HH:mm:ss";
//        Date nowTime = new SimpleDateFormat(format).parse("09:28:00");
//        Date startTime = new SimpleDateFormat(format).parse("09:27:00");
//        Date endTime = new SimpleDateFormat(format).parse("19:27:59");
//        System.out.println(isEffectiveDate(nowTime, startTime, endTime));
//    }

    public static boolean checkTime(){
        String format = "HH:mm:ss";
        try {
            return isEffectiveDate(new SimpleDateFormat(format).parse(querySec()),new SimpleDateFormat(format).parse("09:00:00"),new SimpleDateFormat(format).parse("21:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    // 周天1，周六7
    public static int getDay(){
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public static long getDayLong(){
        return System.currentTimeMillis();
    }

//    public static void main(String[] args) throws ParseException {
//        System.out.println(getDaySubSec("2019-08-20 20:51:19","2019-09-09 21:00:00"));
//    }
}
