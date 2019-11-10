package com.cn.red.point.mobile;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class ProjectTool {
	
    /** 自定义进制(0,1没有加入,容易与o,l混淆) */
    private static final char[] r=new char[]{'q', 'w', 'e', '8', 'a', 's', '2', 'd', 'z', 'x', '9', 'c', '7', 'p', '5', 'i', 'k', '3', 'm', 'j', 'u', 'f', 'r', '4', 'v', 'y', 'l', 't', 'n', '6', 'b', 'g', 'h'};

    /** (不能与自定义进制有重复) */
    private static final char b='o';

    /** 进制长度 */
    private static final int binLen=r.length;

    /** 序列最小长度 */
    private static final int s=6;
	
	/**
	 * 生成用户邀请码
	 * @return
	 */
	public static String getInvitationCode() {
		int s = (int) (10+(Math.random()*89));
		SimpleDateFormat sdf = new SimpleDateFormat("mmssSSS");
		Long id = Long.parseLong(sdf.format(new Date()));
        char[] buf=new char[32];
        int charPos=32;

        while((id / binLen) > 0) {
            int ind=(int)(id % binLen);
            buf[--charPos]=r[ind];
            id /= binLen;
        }
        buf[--charPos]=r[(int)(id % binLen)];
        String str=new String(buf, charPos, (32 - charPos));
        if(str.length() < s) {
            StringBuilder sb=new StringBuilder();
            sb.append(b);
            Random rnd=new Random();
            for(int i=1; i < s - str.length(); i++) {
            sb.append(r[rnd.nextInt(binLen)]);
            }
            str+=sb.toString();
        }
        return str.substring(str.length()-6);
    }

	/**
	 *  生成 短信 验证码
	 */
	public static String getMobileCode() {
		String endCode = ((int)((Math.random()*899999))+100000)+"";
		return endCode;
	}
	
	/**
	 *  生成user_token
	 */
	public static String getUserToken() {
		UUID randomUUID = UUID.randomUUID();
		return randomUUID.toString();
	}
	
	public static int getHourDiff(Integer hour) {
		Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
		int diff = (int) ((c.getTimeInMillis() - System.currentTimeMillis())/1000);
		if(diff < 0) {
			diff = diff+86400;
		}
		return diff;
	}

	public static int getHourDiff(Integer hour,Integer min) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, min);
		c.set(Calendar.SECOND, 0);
		int diff = (int) ((c.getTimeInMillis() - System.currentTimeMillis())/1000);
		if(diff < 0) {
			diff = diff+86400;
		}
		return diff;
	}
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static BigDecimal getBiLi(String startTime,String endTime,String format) {
		try {
			startTime = format + " "+startTime;
			endTime = format + " "+endTime;
			long stime = sdf.parse(startTime).getTime();
			long etime = sdf.parse(endTime).getTime();
			long currentTimeMillis = System.currentTimeMillis();
			long jg = currentTimeMillis-stime;
			long all =  etime - stime;
			if(jg < 0) {
				return  BigDecimal.ZERO;
			}else if(jg > all) {
				return  BigDecimal.ONE;
			}else {
				return new BigDecimal(jg).divide(new BigDecimal(all),4,BigDecimal.ROUND_HALF_UP);
			}
		} catch (Exception e) {
			return BigDecimal.ONE;
		}
	}
}
