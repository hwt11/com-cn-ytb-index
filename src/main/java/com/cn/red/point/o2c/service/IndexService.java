package com.cn.red.point.o2c.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.red.point.common.*;
import com.cn.red.point.common.enity.Result;
import com.cn.red.point.common.enity.User;
import com.cn.red.point.enu.Time;
import com.cn.red.point.o2c.common.UserMoney;
import com.cn.red.point.o2c.mapper.IndexMapper;
import com.cn.red.point.redis.RedisUtilsEx;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class IndexService extends CommonService{

    private static  long overTimeNIN = 86400; // 900秒
    private static  long overTime24Hour = 86400; // 24小时

    @Autowired
    private IndexMapper indexMapper;

    public Result queryUserAliPay(User user) {
        if (StringUtils.isNotEmpty(RedisUtilsEx.get(RedisKeys.USER_O2C + user.getUserId())))
            return Result.ERROR.setMsg("您的O2C账户被停用").setData(null);
        Map<String,Object> map = indexMapper.queryUserAliPay(user.getUserId());
        if (map == null)
            return Result.ERROR.setMsg("您尚未在O2C中认证支付宝").setData(null);
        if (map.get("type").toString().equals("1")) {
            RedisUtilsEx.set(RedisKeys.USER_O2C + user.getUserId(),"1", Time.ONEYEAR.getSec());
            return Result.ERROR.setMsg("您的O2C账户被停用").setData(null);
        }
        return Result.OK.setMsg("success").setData(map);
    }

    public Result queryOrders(int page,int type,String search,String boo) {
        page = (page - 1) * 20;
        if (type == 0) {
            List<Map<String, Object>> orders = indexMapper.queryOrders(page,"price",search,boo);
            for(Map<String,Object> map : orders){
                String userId = map.get("userId").toString();
                BigDecimal bigDecimal = sumOrderNum(userId);
                map.put("threeDayNum",bigDecimal);
            }
            return Result.OK.setMsg("success").setData(orders);
        } else if (type == 1) {
            List<Map<String, Object>> orders = indexMapper.queryOrders(page,"count",search,boo);
            for(Map<String,Object> map : orders){
                String userId = map.get("userId").toString();
                BigDecimal bigDecimal = sumOrderNum(userId);
                map.put("threeDayNum",bigDecimal);
            }
            return Result.OK.setMsg("success").setData(orders);
        } else if (type == 2) {
            List<Map<String, Object>> orders = indexMapper.queryOrders_DESC(page,"price",search,boo);
            for(Map<String,Object> map : orders){
                String userId = map.get("userId").toString();
                BigDecimal bigDecimal = sumOrderNum(userId);
                map.put("threeDayNum",bigDecimal);
            }
            return Result.OK.setMsg("success").setData(orders);
        } else if (type == 3) {
            List<Map<String, Object>> orders = indexMapper.queryOrders_DESC(page,"count",search,boo);
            for(Map<String,Object> map : orders){
                String userId = map.get("userId").toString();
                BigDecimal bigDecimal = sumOrderNum(userId);
                map.put("threeDayNum",bigDecimal);
            }
            return Result.OK.setMsg("success").setData(orders);
        } else if (type == 4) {
            List<Map<String, Object>> orders = indexMapper.queryOrders(page,"signtime",search,boo);
            for(Map<String,Object> map : orders){
                String userId = map.get("userId").toString();
                BigDecimal bigDecimal = sumOrderNum(userId);
                map.put("threeDayNum",bigDecimal);
            }
            return Result.OK.setMsg("success").setData(orders);
        } else if (type == 5) {
            List<Map<String, Object>> orders = indexMapper.queryOrders_DESC(page,"signtime",search,boo);
            for(Map<String,Object> map : orders){
                String userId = map.get("userId").toString();
                BigDecimal bigDecimal = sumOrderNum(userId);
                map.put("threeDayNum",bigDecimal);
            }
            return Result.OK.setMsg("success").setData(orders);
        }
        return Result.ERROR.setMsg("no data").setData(null);
    }

    public BigDecimal sumOrderNum(String userId){
        Set<String> keys = RedisUtilsEx.keys("otc_order_num_" + userId + "_*");
        BigDecimal num = BigDecimal.ZERO;
        if(keys != null){
            for (String str : keys) {
                num.add(new BigDecimal(RedisUtilsEx.get(str)));
            }
        }
        return num;
    }

    private static final BigDecimal MAX = new BigDecimal(1000000);

    public Result insertOrder(User user, String price, int count,String passWord) {
        if (StringUtils.isNotEmpty(RedisUtilsEx.get(RedisKeys.USER_O2C + user.getUserId())))
            return Result.ERROR.setMsg("您的O2C账户被停用").setData(null);
        if (new BigDecimal(count).compareTo(new BigDecimal(100)) < 0)
            return Result.ERROR.setMsg("最低数量100个").setData(null);
//        passWord = Sha256.getSHA256(user.getUserId() + passWord);
//        Map<String,Object> map = indexMapper.queryUserAliPay(user.getUserId());
//        if (map == null)
//            return Result.ERROR.setMsg("请认证支付宝账户").setData(null);
//        if (!map.get("password").equals(passWord))
//            return Result.ERROR.setMsg("支付密码错误").setData(null);
//        String val = RedisUtilsEx.get(RedisKeys.USER_O2C_COUNT + QueryTime.queryToday() + user.getUserId());
//        if (StringUtils.isEmpty(val))
//            RedisUtilsEx.set(RedisKeys.USER_O2C_COUNT + QueryTime.queryToday() + user.getId(),String.valueOf(count),Time.ONEYEAR.getSec());
//        else {
//            BigDecimal c = new BigDecimal(val).add(new BigDecimal(count)).setScale(0, RoundingMode.DOWN);
//            if (c.compareTo(MAX) > 0)
//                return Result.ERROR.setMsg("每日最多挂单量为" + MAX.toString());
//            RedisUtilsEx.set(RedisKeys.USER_O2C_COUNT + QueryTime.queryToday() + user.getId(),c.toString(),Time.DAY.getSec());
//        }
        indexMapper.insertOrder(new HashMap<>() {
            {
                put("userId",user.getUserId());
                put("image",user.getHeadImg());
                put("nick",user.getNickName());
                put("price",price);
                put("count",count);
                put("type",0);
            }
        });
        return Result.OK.setMsg("挂单成功").setData(null);
    }

    @Transactional(timeout = 5)
    public Result insertSellOrder(User user, String price, int count,String passWord) {
        if (StringUtils.isNotEmpty(RedisUtilsEx.get(RedisKeys.USER_O2C + user.getUserId())))
            return Result.ERROR.setMsg("您的O2C账户被停用").setData(null);
        if (new BigDecimal(count).compareTo(new BigDecimal(100)) < 0)
            return Result.ERROR.setMsg("最低数量100个").setData(null);
        int num = indexMapper.queryOrderCount(user.getUserId());
        if (num > 3)
            return Result.ERROR.setMsg("最多只能挂3单").setData(null);
        if (new BigDecimal(count).compareTo(MAX) > 0)
            return Result.ERROR.setMsg("每日最多挂单量为" + MAX.toString());
//        passWord = Sha256.getSHA256(user.getUserId() + passWord);
//        Map<String,Object> map = indexMapper.queryUserAliPay(user.getUserId());
//        if (map == null)
//            return Result.ERROR.setMsg("请认证支付宝账户").setData(null);
//        if (!map.get("password").equals(passWord))
//            return Result.ERROR.setMsg("支付密码错误").setData(null);
        String val = RedisUtilsEx.get(RedisKeys.USER_O2C_COUNT + QueryTime.queryToday() + user.getUserId());
        if (StringUtils.isEmpty(val))
            RedisUtilsEx.set(RedisKeys.USER_O2C_COUNT + QueryTime.queryToday() + user.getUserId(),String.valueOf(count),Time.ONEYEAR.getSec());
        else {
            BigDecimal c = new BigDecimal(val).add(new BigDecimal(count)).setScale(4, RoundingMode.DOWN);
            if (c.compareTo(MAX) > 0)
                return Result.ERROR.setMsg("每日最多挂单量为" + MAX.toString());
            RedisUtilsEx.set(RedisKeys.USER_O2C_COUNT + QueryTime.queryToday() + user.getUserId(),c.toString(),Time.ONEYEAR.getSec());
        }
        UserMoney userMoney = indexMapper.queryUserMoney(user.getUserId());
        if (userMoney.getYtl_money().compareTo(new BigDecimal(count)) < 0)
            return Result.ERROR.setMsg("ytl数量不足").setData(null);
        BigDecimal percent = vipPercentFromMoney(userMoney);
        BigDecimal p = new BigDecimal(count).divide(BigDecimal.ONE.add(BigDecimal.ONE.subtract(percent)),2,RoundingMode.DOWN);
        indexMapper.updateUserZLMoney(new HashMap<>(){
            {
                put("userId",user.getUserId());
                put("ytlmoney",userMoney.getYtl_money().subtract(new BigDecimal(count)).setScale(4,RoundingMode.HALF_DOWN));
                put("coldytlmoney",userMoney.getCold_ytl().add(new BigDecimal(count)).setScale(4,RoundingMode.HALF_DOWN));
            }
        });
        indexMapper.insertOrder(new HashMap<>() {
            {
                put("userId",user.getUserId());
                put("image",user.getHeadImg());
                put("nick",user.getNickName());
                put("price",price);
                put("count",p);
                put("type",1);
            }
        });
        return Result.OK.setMsg("挂单成功").setData(null);
    }

    @Transactional(timeout = 5)
    public Result carryBuy(User user, String orderId, int count) {
        if (StringUtils.isNotEmpty(RedisUtilsEx.get(RedisKeys.USER_O2C + user.getUserId())))
            return Result.ERROR.setMsg("您的O2C账户被停用").setData(null);
        if (count < 100)
            return Result.ERROR.setMsg("最低数量100个").setData(null);
        Map<String,Object> alipay = indexMapper.queryUserAliPay(user.getUserId());
        UserMoney userMoney = indexMapper.queryUserMoney(user.getUserId());
        BigDecimal needCount = new BigDecimal(count);
        if (userMoney.getYtl_money().compareTo(needCount) < 0)
            return Result.ERROR.setMsg("ytl数量不足").setData(null);
        Map<String,Object> order = indexMapper.queryOrder(orderId);
        if (order == null)
            return Result.ERROR.setData(null).setMsg("订单不存在");
        if (new BigDecimal(order.get("count").toString()).compareTo(new BigDecimal(count)) < 0)
            return Result.ERROR.setMsg("订单数量不足").setData(null);
        indexMapper.updateUserZLMoney(new HashMap<>(){
            {
                put("userId",user.getUserId());
                put("zlmoney",userMoney.getYtl_money().subtract(needCount).setScale(4,RoundingMode.HALF_DOWN));
                put("coldzlmoney",userMoney.getCold_ytl().add(needCount).setScale(4,RoundingMode.HALF_DOWN));
            }
        });
        BigDecimal percent = vipPercentFromMoney(userMoney);
        BigDecimal p = needCount.divide(BigDecimal.ONE.add(BigDecimal.ONE.subtract(percent)),4,RoundingMode.DOWN);
        Map<String,Object> param = new HashMap<>(){
            {
                put("price",order.get("price"));
                put("userId",order.get("userid"));
                put("orderId",orderId);
                put("enemyId",user.getUserId());
                put("num",p);
                put("enemynick",user.getNickName());
                put("enemyimage",user.getHeadImg());
                put("enemyalipay",alipay.get("payno"));
                put("enemypayimage",alipay.get("image"));
                put("enemyname",alipay.get("name"));
                put("status",10);
                put("type",0);
            }
        };
        RedisUtilsEx.set("ORDER_NEED_" + order.get("userid"),"1",Time.THREEHOUR.getSec());
        RedisUtilsEx.set("ORDER_NEED_" + user.getUserId(),"1",Time.THREEHOUR.getSec());
        indexMapper.insertOrderLog(param);
        BigDecimal last = new BigDecimal(order.get("count").toString()).subtract(p).setScale(0,RoundingMode.DOWN);
        indexMapper.updateOrder(new HashMap<>(){
            {
                put("orderId",orderId);
                put("count",last);
                put("status",last.compareTo(BigDecimal.TEN) < 0 ? "50":"5");
                put("endtime",last.compareTo(BigDecimal.TEN) < 0 ? QueryTime.queryTodaySec() : "");
            }
        });
        SEND_RABBITMQ(JSONObject.toJSONString(new HashMap<>(){
            {
                put("id",param.get("id"));
                put("status",10);
            }
        }),"1800000");
//        SMSTool.sendMsg(alipay.get("phone").toString());
        updateZLMoneyLOG(user.getUserId(),needCount.doubleValue(),1,"O2C锁定");
        return Result.OK.setMsg("出售成功").setData(null);
    }

    @Transactional(timeout = 20)
    public Result carrySell(User user, String orderId, int count) {
        if (StringUtils.isNotEmpty(RedisUtilsEx.get(RedisKeys.USER_O2C + user.getUserId())))
            return Result.ERROR.setMsg("您的O2C账户被停用").setData(null);
        if (count < 100)
            return Result.ERROR.setMsg("最低数量100个").setData(null);
        Map<String,Object> order = indexMapper.querySellOrder(orderId);
        if (order == null)
            return Result.ERROR.setData(null).setMsg("订单不存在");
        Map<String,Object> alipay = indexMapper.queryUserAliPay(Integer.parseInt(order.get("userid").toString()));
        if (new BigDecimal(order.get("count").toString()).compareTo(new BigDecimal(count)) < 0)
            return Result.ERROR.setMsg("订单数量不足").setData(null);
        Map<String,Object> param = new HashMap<>(){
            {
                put("price",order.get("price"));
                put("userId",order.get("userid"));
                put("orderId",orderId);
                put("enemyId",user.getUserId());
                put("num",count);
                put("enemynick",user.getNickName());
                put("enemyimage",user.getHeadImg());
                put("enemyalipay",alipay.get("payno"));
                put("enemypayimage",alipay.get("image"));
                put("enemyname",alipay.get("name"));
                put("status",100);
                put("type",1);
            }
        };
        RedisUtilsEx.set("ORDER_NEED_" + order.get("userid"),"1",Time.THREEHOUR.getSec());
        RedisUtilsEx.set("ORDER_NEED_" + user.getUserId(),"1",Time.THREEHOUR.getSec());
        indexMapper.insertOrderLog(param);
        BigDecimal last = new BigDecimal(order.get("count").toString()).subtract(new BigDecimal(count)).setScale(0,RoundingMode.DOWN);
        indexMapper.updateOrder(new HashMap<>(){
            {
                put("orderId",orderId);
                put("count",last);
                put("status",last.compareTo(BigDecimal.TEN) < 0 ? "50":"5");
                put("endtime",last.compareTo(BigDecimal.TEN) < 0 ? QueryTime.queryTodaySec() : "");
            }
        });
        SEND_RABBITMQ(JSONObject.toJSONString(new HashMap<>(){
            {
                put("id",param.get("id"));
                put("status",100);
            }
        }),"1800000");
        Map<String,Object> map = new HashMap<>();
        map.put("logId",param.get("id"));
        return Result.OK.setMsg("确认成功，请及时付款").setData(map);
    }

    @Transactional(timeout = 5)
    public void cancelOrder(Map<String, Object> map) {
        if (map == null)
            return;
        if (map.get("status").toString().equals("10")) {
            Map<String,Object> order = indexMapper.queryOrderFromMQ(map.get("id").toString());
            if (order == null)
                return;
            indexMapper.cancelOrderLog(new HashMap<>(){
                {
                    put("id",map.get("id").toString());
                    put("status",12);
                }
            });
            indexMapper.cancelOrder(order.get("orderid").toString());
            UserMoney userMoney = indexMapper.queryUserMoney(Integer.parseInt(order.get("enemyid").toString()));
            BigDecimal percent = vipPercentFromMoney(userMoney);
            BigDecimal needCount = new BigDecimal(order.get("num").toString()).multiply(BigDecimal.ONE.add(BigDecimal.ONE.subtract(percent)).setScale(4,RoundingMode.HALF_DOWN));
            indexMapper.updateUserZLMoney(new HashMap<>(){
                {
                    put("userId",order.get("enemyid"));
                    put("zlmoney",userMoney.getYtl_money().add(userMoney.getCold_ytl().compareTo(needCount) < 0 ? userMoney.getCold_ytl() : needCount).setScale(6,RoundingMode.HALF_DOWN));
                    put("coldzlmoney",userMoney.getCold_ytl().compareTo(needCount) < 0 ? 0 : userMoney.getCold_ytl().subtract(needCount).setScale(6,RoundingMode.HALF_DOWN));
                }
            });
            updateZLMoneyLOG(Integer.parseInt(order.get("enemyid").toString()),needCount.doubleValue(),0,"O2C自动撤单返回");
            String key = "O2C_USER_CANCEL_" + order.get("userid").toString() + QueryTime.queryToday();
            String val = RedisUtilsEx.get(key);
            if (StringUtils.isEmpty(val))
                RedisUtilsEx.set(key,"1",Time.DAY.getSec());
            else {
                int c = Integer.parseInt(val) + 1;
                RedisUtilsEx.set(key,String.valueOf(c),Time.DAY.getSec());
                if (c >= 2)
                    RedisUtilsEx.set(RedisKeys.USER_O2C + order.get("userid").toString(),"1",Time.ONEYEAR.getSec());
            }
        } else if (map.get("status").toString().equals("15")){
            Map<String,Object> order = indexMapper.queryOrderFromMQ15(map.get("id").toString());
            if (order == null)
                return;
            UserMoney userMoney = indexMapper.queryUserMoney(Integer.parseInt(order.get("enemyid").toString()));
            BigDecimal percent = vipPercentFromMoney(userMoney);
            BigDecimal needCount = new BigDecimal(order.get("num").toString()).multiply(BigDecimal.ONE.add(BigDecimal.ONE.subtract(percent)).setScale(4,RoundingMode.HALF_DOWN));
            indexMapper.updateUserZLMoney(new HashMap<>(){
                {
                    put("userId",order.get("enemyid"));
                    put("zlmoney",userMoney.getYtl_money());
                    put("coldzlmoney",userMoney.getCold_ytl().compareTo(needCount) < 0 ? 0 : userMoney.getCold_ytl().subtract(needCount).setScale(6,RoundingMode.HALF_DOWN));
                }
            });
            updateZLMoneyLOG(Integer.parseInt(order.get("enemyid").toString()),needCount.doubleValue(),1,"O2C市场寄售");
            UserMoney _userMoney = indexMapper.queryUserMoney(Integer.parseInt(order.get("userid").toString()));
            indexMapper.updateUserZLMoney(new HashMap<>(){
                {
                    put("userId",order.get("userid"));
                    put("zlmoney",_userMoney.getYtl_money().add(new BigDecimal(order.get("num").toString())).setScale(6,RoundingMode.HALF_DOWN));
                    put("coldzlmoney",_userMoney.getCold_ytl());
                }
            });
            indexMapper.updateOrderStatusEND(new HashMap<>(){
                {
                    put("id",map.get("id").toString());
                    put("status",19);
                    put("old",15);
                }
            });
            executorService.execute(()->{
                String time = QueryTime.queryToday();
                String dayTop = RedisUtilsEx.get(RedisKeys.O2C_TOP_PRICE + time);
                if (StringUtils.isEmpty(dayTop))
                    RedisUtilsEx.set(RedisKeys.O2C_TOP_PRICE + time,order.get("price").toString(),Time.TWODAY.getSec());
                else {
                    if (new BigDecimal(dayTop).compareTo(new BigDecimal(order.get("price").toString())) < 0)
                        RedisUtilsEx.set(RedisKeys.O2C_TOP_PRICE + time,order.get("price").toString(),Time.TWODAY.getSec());
                }
                String dayLow = RedisUtilsEx.get(RedisKeys.O2C_LOW_PRICE + time);
                if (StringUtils.isEmpty(dayLow))
                    RedisUtilsEx.set(RedisKeys.O2C_LOW_PRICE + time,order.get("price").toString(),Time.TWODAY.getSec());
                else {
                    if (new BigDecimal(order.get("price").toString()).compareTo(new BigDecimal(dayLow)) < 0)
                        RedisUtilsEx.set(RedisKeys.O2C_LOW_PRICE + time,order.get("price").toString(),Time.TWODAY.getSec());
                }
                String number = RedisUtilsEx.get(RedisKeys.O2C_COUNT + time);
                if (StringUtils.isEmpty(number))
                    RedisUtilsEx.set(RedisKeys.O2C_COUNT + time,order.get("num").toString(),Time.MOUNTH.getSec());
                else
                    RedisUtilsEx.set(RedisKeys.O2C_COUNT + time,new BigDecimal(order.get("num").toString()).add(new BigDecimal(number)).setScale(4,RoundingMode.DOWN).toString(),Time.MOUNTH.getSec());
                RedisUtilsEx.set(RedisKeys.NOW_PRICE,order.get("price").toString(),Time.MOUNTH.getSec());
            });
            updateZLMoneyLOG(Integer.parseInt(order.get("userid").toString()),Double.parseDouble(order.get("num").toString()),0,"O2C市场收购");
        } else if (map.get("status").toString().equals("100")) {
            Map<String,Object> order = indexMapper.queryOrderFromMQ_Sell(map.get("id").toString());
            if (order == null)
                return;
            indexMapper.cancelOrderLog(new HashMap<>(){
                {
                    put("id",map.get("id").toString());
                    put("status",120);
                }
            });
            indexMapper.updateUserOrder(new HashMap<>(){
                {
                    put("orderId",order.get("orderid").toString());
                    put("num",order.get("num").toString());
                }
            });
            String key = "O2C_USER_CANCEL_" + order.get("enemyid").toString() + QueryTime.queryToday();
            String val = RedisUtilsEx.get(key);
            if (StringUtils.isEmpty(val))
                RedisUtilsEx.set(key,"1",Time.DAY.getSec());
            else {
                int c = Integer.parseInt(val) + 1;
                RedisUtilsEx.set(key,String.valueOf(c),Time.DAY.getSec());
                if (c >= 2)
                    RedisUtilsEx.set(RedisKeys.USER_O2C + order.get("enemyid").toString(),"1",Time.ONEYEAR.getSec());
            }
        } else if (map.get("status").toString().equals("150")) {
            Map<String,Object> order = indexMapper.queryOrderFromMQ150(map.get("id").toString());
            if (order == null)
                return;
            UserMoney userMoney = indexMapper.queryUserMoney(Integer.parseInt(order.get("userid").toString()));
            BigDecimal percent = vipPercentFromMoney(userMoney);
            BigDecimal needCount = new BigDecimal(order.get("num").toString()).multiply(BigDecimal.ONE.add(BigDecimal.ONE.subtract(percent)).setScale(4,RoundingMode.HALF_DOWN));
            indexMapper.updateUserZLMoney(new HashMap<>(){
                {
                    put("userId",order.get("userid"));
                    put("zlmoney",userMoney.getYtl_money());
                    put("coldzlmoney",userMoney.getCold_ytl().compareTo(needCount) < 0 ? 0 : userMoney.getCold_ytl().subtract(needCount).setScale(6,RoundingMode.HALF_DOWN));
                }
            });
            updateZLMoneyLOG(Integer.parseInt(order.get("userid").toString()),needCount.doubleValue(),1,"O2C市场寄售");
            UserMoney _userMoney = indexMapper.queryUserMoney(Integer.parseInt(order.get("enemyid").toString()));
            indexMapper.updateUserZLMoney(new HashMap<>(){
                {
                    put("userId",order.get("enemyid"));
                    put("zlmoney",_userMoney.getYtl_money().add(new BigDecimal(order.get("num").toString())).setScale(6,RoundingMode.HALF_DOWN));
                    put("coldzlmoney",_userMoney.getCold_ytl());
                }
            });
            indexMapper.updateOrderStatusEND(new HashMap<>(){
                {
                    put("id",map.get("id").toString());
                    put("status",190);
                    put("old",150);
                }
            });
            executorService.execute(()->{
                String time = QueryTime.queryToday();
                String dayTop = RedisUtilsEx.get(RedisKeys.O2C_TOP_PRICE + time);
                if (StringUtils.isEmpty(dayTop))
                    RedisUtilsEx.set(RedisKeys.O2C_TOP_PRICE + time,order.get("price").toString(),Time.TWODAY.getSec());
                else {
                    if (new BigDecimal(dayTop).compareTo(new BigDecimal(order.get("price").toString())) < 0)
                        RedisUtilsEx.set(RedisKeys.O2C_TOP_PRICE + time,order.get("price").toString(),Time.TWODAY.getSec());
                }
                String dayLow = RedisUtilsEx.get(RedisKeys.O2C_LOW_PRICE + time);
                if (StringUtils.isEmpty(dayLow))
                    RedisUtilsEx.set(RedisKeys.O2C_LOW_PRICE + time,order.get("price").toString(),Time.TWODAY.getSec());
                else {
                    if (new BigDecimal(order.get("price").toString()).compareTo(new BigDecimal(dayLow)) < 0)
                        RedisUtilsEx.set(RedisKeys.O2C_LOW_PRICE + time,order.get("price").toString(),Time.TWODAY.getSec());
                }
                String number = RedisUtilsEx.get(RedisKeys.O2C_COUNT + time);
                if (StringUtils.isEmpty(number))
                    RedisUtilsEx.set(RedisKeys.O2C_COUNT + time,order.get("num").toString(),Time.MOUNTH.getSec());
                else
                    RedisUtilsEx.set(RedisKeys.O2C_COUNT + time,new BigDecimal(order.get("num").toString()).add(new BigDecimal(number)).setScale(4,RoundingMode.DOWN).toString(),Time.MOUNTH.getSec());
                RedisUtilsEx.set(RedisKeys.NOW_PRICE,order.get("price").toString(),Time.MOUNTH.getSec());
            });
            updateZLMoneyLOG(Integer.parseInt(order.get("enemyid").toString()),Double.parseDouble(order.get("num").toString()),0,"O2C市场收购");
        }
    }

    @Transactional
    public Result cancelOrderByUser(User user, String orderId) {
        Map<String,Object> map = indexMapper.queryUserOrder(new HashMap<>(){
            {
                put("userId",user.getUserId());
                put("orderId",orderId);
            }
        });
        if (map == null)
            return Result.ERROR.setMsg("订单不存在").setData(null);
        indexMapper.cancelUserOrder(new HashMap<>(){
            {
                put("orderId",orderId);
                put("status",map.get("status").equals("0") ? "20" : "15");
            }
        });
        return Result.OK.setMsg("success").setData(null);
    }

    @Transactional(timeout = 5)
    public Result cancelSellByUser(User user, String orderId) {
        Map<String,Object> map = indexMapper.queryUserSellOrder(new HashMap<>(){
            {
                put("userId",user.getUserId());
                put("orderId",orderId);
            }
        });
        if (map == null)
            return Result.ERROR.setMsg("订单不存在").setData(null);
        UserMoney userMoney = indexMapper.queryUserMoney(user.getUserId());
        BigDecimal percent = vipPercentFromMoney(userMoney);
        BigDecimal needCount = new BigDecimal(map.get("count").toString()).multiply(BigDecimal.ONE.add(BigDecimal.ONE.subtract(percent)).setScale(4,RoundingMode.HALF_DOWN));
        BigDecimal re = userMoney.getCold_ytl().compareTo(needCount) < 0 ? userMoney.getCold_ytl() : needCount;
        indexMapper.updateUserZLMoney(new HashMap<>(){
            {
                put("userId",user.getUserId());
                put("zlmoney",userMoney.getYtl_money().add(re).setScale(6,RoundingMode.HALF_DOWN));
                put("coldzlmoney",userMoney.getCold_ytl().subtract(re).setScale(6,RoundingMode.HALF_DOWN));
            }
        });
        updateZLMoneyLOG(user.getUserId(),re.doubleValue(),0,"撤单返回");
        indexMapper.cancelUserOrder(new HashMap<>(){
            {
                put("orderId",orderId);
                put("status",map.get("status").equals("0") ? "200" : "150");
            }
        });
        return Result.OK.setMsg("success").setData(null);
    }

    @Transactional(timeout = 5)
    public Result payOrder(User user, String orderId,String image,String userPay) {
        Map<String,Object> order = indexMapper.queryOrderByUser(new HashMap<>(){
            {
                put("userId",user.getUserId());
                put("orderId",orderId);
            }
        });
        if (order == null)
            return Result.ERROR.setMsg("订单不存在").setData(null);
        indexMapper.updateOrderStatus(new HashMap<>(){
            {
                put("orderId",orderId);
                put("payreturn",image);
                put("userPayNum",userPay);
            }
        });
        RedisUtilsEx.set("ORDER_NEED_" + order.get("userid"),"1",Time.THREEHOUR.getSec());
        RedisUtilsEx.set("ORDER_NEED_" + order.get("enemyid"),"1",Time.THREEHOUR.getSec());
        SEND_RABBITMQ(JSONObject.toJSONString(new HashMap<>(){
            {
                put("id",orderId);
                put("status",15);
            }
        }),"7200000");
        return Result.OK.setMsg("确认成功").setData(null);
    }

    @Transactional(timeout = 5)
    public Result paySellOrder(User user, String orderId,String image,String userPay) {
        Map<String,Object> order = indexMapper.querySellOrderByUser(new HashMap<>(){
            {
                put("userId",user.getUserId());
                put("orderId",orderId);
            }
        });
        if (order == null)
            return Result.ERROR.setMsg("订单不存在").setData(null);
        indexMapper.updateSellOrderStatus(new HashMap<>(){
            {
                put("orderId",orderId);
                put("payreturn",image);
                put("userPayNum",userPay);
            }
        });
        SEND_RABBITMQ(JSONObject.toJSONString(new HashMap<>(){
            {
                put("id",orderId);
                put("status",150);
            }
        }),"7200000");
        RedisUtilsEx.set("ORDER_NEED_" + order.get("userid"),"1",Time.THREEHOUR.getSec());
        RedisUtilsEx.set("ORDER_NEED_" + order.get("enemyid"),"1",Time.THREEHOUR.getSec());
        return Result.OK.setMsg("确认成功").setData(null);
    }

    @Transactional(timeout = 5)
    public Result commitOrder(User user, String orderId,String passWord) {
        Map<String,Object> order = indexMapper.queryOrderByEnemy(new HashMap<>(){
            {
                put("enemyId",user.getUserId());
                put("orderId",orderId);
            }
        });
        if (order == null)
            return Result.ERROR.setMsg("订单不存在").setData(null);
        UserMoney userMoney = indexMapper.queryUserMoney(Integer.parseInt(order.get("enemyid").toString()));
        BigDecimal percent = vipPercentFromMoney(userMoney);
        BigDecimal needCount = new BigDecimal(order.get("num").toString()).multiply(BigDecimal.ONE.add(BigDecimal.ONE.subtract(percent)).setScale(4,RoundingMode.HALF_DOWN));
        indexMapper.updateUserZLMoney(new HashMap<>(){
            {
                put("userId",order.get("enemyid"));
                put("zlmoney",userMoney.getYtl_money());
                put("coldzlmoney",userMoney.getCold_ytl().compareTo(needCount) < 0 ? 0 : userMoney.getCold_ytl().subtract(needCount).setScale(4,RoundingMode.HALF_DOWN));
            }
        });
        updateZLMoneyLOG(Integer.parseInt(order.get("enemyid").toString()),needCount.doubleValue(),1,"O2C市场寄售");
        UserMoney _userMoney = indexMapper.queryUserMoney(Integer.parseInt(order.get("userid").toString()));
        indexMapper.updateUserZLMoney(new HashMap<>(){
            {
                put("userId",order.get("userid"));
                put("zlmoney",_userMoney.getYtl_money().add(new BigDecimal(order.get("num").toString())).setScale(4,RoundingMode.HALF_DOWN));
                put("coldzlmoney",_userMoney.getCold_ytl());
            }
        });
        indexMapper.updateOrderStatusEND(new HashMap<>(){
            {
                put("id",orderId);
                put("status",20);
                put("old",15);
            }
        });
        RedisUtilsEx.set("ORDER_NEED_" + order.get("userid"),"1",Time.THREEHOUR.getSec());
        RedisUtilsEx.set("ORDER_NEED_" + order.get("enemyid"),"1",Time.THREEHOUR.getSec());
        executorService.execute(()->{
            String time = QueryTime.queryToday();
            String dayTop = RedisUtilsEx.get(RedisKeys.O2C_TOP_PRICE + time);
            if (StringUtils.isEmpty(dayTop))
                RedisUtilsEx.set(RedisKeys.O2C_TOP_PRICE + time,order.get("price").toString(),Time.TWODAY.getSec());
            else {
                if (new BigDecimal(dayTop).compareTo(new BigDecimal(order.get("price").toString())) < 0)
                    RedisUtilsEx.set(RedisKeys.O2C_TOP_PRICE + time,order.get("price").toString(),Time.TWODAY.getSec());
            }
            String dayLow = RedisUtilsEx.get(RedisKeys.O2C_LOW_PRICE + time);
            if (StringUtils.isEmpty(dayLow))
                RedisUtilsEx.set(RedisKeys.O2C_LOW_PRICE + time,order.get("price").toString(),Time.TWODAY.getSec());
            else {
                if (new BigDecimal(order.get("price").toString()).compareTo(new BigDecimal(dayLow)) < 0)
                    RedisUtilsEx.set(RedisKeys.O2C_LOW_PRICE + time,order.get("price").toString(),Time.TWODAY.getSec());
            }
            String number = RedisUtilsEx.get(RedisKeys.O2C_COUNT + time);
            if (StringUtils.isEmpty(number))
                RedisUtilsEx.set(RedisKeys.O2C_COUNT + time,order.get("num").toString(),Time.MOUNTH.getSec());
            else
                RedisUtilsEx.set(RedisKeys.O2C_COUNT + time,new BigDecimal(order.get("num").toString()).add(new BigDecimal(number)).setScale(4,RoundingMode.DOWN).toString(),Time.MOUNTH.getSec());
            RedisUtilsEx.set(RedisKeys.NOW_PRICE,order.get("price").toString(),Time.MOUNTH.getSec());
        });

        updateZLMoneyLOG(Integer.parseInt(order.get("userid").toString()),Double.parseDouble(order.get("num").toString()),0,"O2C市场收购");
        setRedisUserNum(user.getUserId(),needCount);
        setRedisUserNum(Integer.parseInt(order.get("enemyid").toString()),needCount);
        return Result.OK.setMsg("确认成功").setData(null);
    }

    @Transactional(timeout = 5)
    public Result commitSellOrder(User user, String orderId,String passWord) {
        Map<String,Object> order = indexMapper.querySellOrderByEnemy(new HashMap<>(){
            {
                put("enemyId",user.getUserId());
                put("orderId",orderId);
            }
        });
        RedisUtilsEx.set("ORDER_NEED_" + order.get("userid"),"1",Time.THREEHOUR.getSec());
        RedisUtilsEx.set("ORDER_NEED_" + order.get("enemyid"),"1",Time.THREEHOUR.getSec());
        if (order == null)
            return Result.ERROR.setMsg("订单不存在").setData(null);
        UserMoney userMoney = indexMapper.queryUserMoney(Integer.parseInt(order.get("userid").toString()));
        BigDecimal percent = vipPercentFromMoney(userMoney);
        BigDecimal needCount = new BigDecimal(order.get("num").toString()).multiply(BigDecimal.ONE.add(BigDecimal.ONE.subtract(percent)).setScale(4,RoundingMode.HALF_DOWN));
        indexMapper.updateUserZLMoney(new HashMap<>(){
            {
                put("userId",order.get("userid"));
                put("zlmoney",userMoney.getYtl_money());
                put("coldzlmoney",userMoney.getCold_ytl().compareTo(needCount) < 0 ? 0 : userMoney.getCold_ytl().subtract(needCount).setScale(4,RoundingMode.HALF_DOWN));
            }
        });
        updateZLMoneyLOG(Integer.parseInt(order.get("userid").toString()),needCount.doubleValue(),1,"O2C市场寄售");
        UserMoney _userMoney = indexMapper.queryUserMoney(Integer.parseInt(order.get("enemyid").toString()));
        indexMapper.updateUserZLMoney(new HashMap<>(){
            {
                put("userId",order.get("enemyid"));
                put("zlmoney",_userMoney.getYtl_money().add(new BigDecimal(order.get("num").toString())).setScale(4,RoundingMode.HALF_DOWN));
                put("coldzlmoney",_userMoney.getCold_ytl());
            }
        });
        indexMapper.updateOrderStatusEND(new HashMap<>(){
            {
                put("id",orderId);
                put("status",200);
                put("old",150);
            }
        });
        RedisUtilsEx.set("ORDER_NEED_" + order.get("userid"),"1",Time.THREEHOUR.getSec());
        RedisUtilsEx.set("ORDER_NEED_" + order.get("enemyid"),"1",Time.THREEHOUR.getSec());
        executorService.execute(()->{
            String time = QueryTime.queryToday();
            String dayTop = RedisUtilsEx.get(RedisKeys.O2C_TOP_PRICE + time);
            if (StringUtils.isEmpty(dayTop))
                RedisUtilsEx.set(RedisKeys.O2C_TOP_PRICE + time,order.get("price").toString(),Time.TWODAY.getSec());
            else {
                if (new BigDecimal(dayTop).compareTo(new BigDecimal(order.get("price").toString())) < 0)
                    RedisUtilsEx.set(RedisKeys.O2C_TOP_PRICE + time,order.get("price").toString(),Time.TWODAY.getSec());
            }
            String dayLow = RedisUtilsEx.get(RedisKeys.O2C_LOW_PRICE + time);
            if (StringUtils.isEmpty(dayLow))
                RedisUtilsEx.set(RedisKeys.O2C_LOW_PRICE + time,order.get("price").toString(),Time.TWODAY.getSec());
            else {
                if (new BigDecimal(order.get("price").toString()).compareTo(new BigDecimal(dayLow)) < 0)
                    RedisUtilsEx.set(RedisKeys.O2C_LOW_PRICE + time,order.get("price").toString(),Time.TWODAY.getSec());
            }
            String number = RedisUtilsEx.get(RedisKeys.O2C_COUNT + time);
            if (StringUtils.isEmpty(number))
                RedisUtilsEx.set(RedisKeys.O2C_COUNT + time,order.get("num").toString(),Time.MOUNTH.getSec());
            else
                RedisUtilsEx.set(RedisKeys.O2C_COUNT + time,new BigDecimal(order.get("num").toString()).add(new BigDecimal(number)).setScale(4,RoundingMode.DOWN).toString(),Time.MOUNTH.getSec());
            RedisUtilsEx.set(RedisKeys.NOW_PRICE,order.get("price").toString(),Time.MOUNTH.getSec());
        });

        updateZLMoneyLOG(Integer.parseInt(order.get("enemyid").toString()),Double.parseDouble(order.get("num").toString()),0,"O2C市场收购");
        setRedisUserNum(user.getUserId(),needCount);
        setRedisUserNum(Integer.parseInt(order.get("userid").toString()),needCount);
        return Result.OK.setMsg("确认成功").setData(null);
    }

    public Result O2cGetKline(User user){
        String today = QueryTime.queryToday();
        String s = RedisUtilsEx.get("getKLine_" + today);
        List<Map<String, Object>> kline = new ArrayList<>();
        if(StringUtils.isEmpty(s)){
            kline = indexMapper.getKilne();
            RedisUtilsEx.set("getKLine_" + today,JSONObject.toJSONString(kline),Time.DAY.getSec());
        }else{
            kline = JSONObject.parseObject(s,List.class);
        }
        Map<String, Object> klineToday = indexMapper.getKlineToday();
        if(klineToday != null){
            kline.add(klineToday);
        }else{
            Map<String,Object> map = new HashMap<>();
            map.put("date",QueryTime.queryTodayToKline());
            map.put("price","0");
            kline.add(map);
        }


        return Result.OK.setMsg("success").setData(kline);
    }

    public static void main(String[] args) {
        System.out.println(QueryTime.queryTodayToKline());
    }
    public void setRedisUserNum(int userId,BigDecimal needCount){
        executorService.execute(()->{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String format1 = format.format(new Date());
            String userIdNum = RedisUtilsEx.get("otc_order_num_" + userId + "_" + format1);
            if(StringUtils.isNotEmpty(userIdNum)){
                BigDecimal newNeedCount = new BigDecimal(userIdNum).add(needCount).setScale(4,RoundingMode.DOWN);
                RedisUtilsEx.set("otc_order_num_"+userId+"_"+format1,newNeedCount.toString(),2592000);
            }else{
                RedisUtilsEx.set("otc_order_num_"+userId+"_"+format1,needCount.toString(),2592000);
            }
        });
    }



//    public Result commitAliPay(User user, String name, String payno, String image, String phone, String password) {
//        Map<String,Object> map = indexMapper.queryUserAliPay(user.getUserId());
//        if (map != null)
//            return Result.ERROR.setMsg("已认证").setData(null);
//        indexMapper.insertAliPay(new HashMap<>(){
//            {
//                put("userId",user.getUserId());
//                put("name",name);
//                put("payno",payno);
//                put("image",image);
//                put("phone",phone);
//                put("password",Sha256.getSHA256(user.getUserId() + password));
//            }
//        });
//        RedisUtilsEx.set("ORDER_NEED_" + user.getUserId(),"1",Time.THREEHOUR.getSec());
//        RedisUtilsEx.set("ORDER_NEED_" + user.getUserId(),"1",Time.THREEHOUR.getSec());
//        return Result.OK.setMsg("success").setData(null);
//    }

    @Resource
    private RabbitTemplate rabbitTemplate;

    private void SEND_RABBITMQ(String mess,String time){
        executorService.execute(()->{
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            MessagePostProcessor messagePostProcessor = message -> {
                MessageProperties messageProperties = message.getMessageProperties();
                messageProperties.setContentEncoding("utf-8");
                messageProperties.setExpiration(time);
                return message;
            };
            rabbitTemplate.convertAndSend("DL_EXCHANGE", "DL_KEY", mess, messagePostProcessor, correlationData);
        });
    }

    private BigDecimal vipPercentFromMoney(UserMoney user) {
        return new BigDecimal(0.99);
    }

    public Result historyLogList(User user,String status,String type,String page,String boo){
        String userId = user.getUserId()+"";
        int pageNum = (Integer.parseInt(page)-1);
        if(status.equals("0")){
            if(type.equals("buy")){
                return Result.OK.setMsg("success").setData(indexMapper.findO2cByUserId(userId,pageNum*10,boo));
            }else{
                return Result.OK.setMsg("success").setData(indexMapper.findO2cByUserIdSell(userId,pageNum*10,boo));
            }
        }else if(status.equals("1")){
            if(type.equals("buy")){
                return Result.OK.setMsg("success").setData(indexMapper.findO2cByUserIdFinish(userId,pageNum*10,boo));
            }else{
                return Result.OK.setMsg("success").setData(indexMapper.findO2cByUserIdFinishSell(userId,pageNum*10,boo));
            }
        }else {
            return Result.ERROR.setMsg("error").setData(null);
        }
    }

    public Result O2cBuyDetails(User user,String orderId,String boo){
        Map<String, Object> stringObjectMap = indexMapper.O2cBuyDetails(user.getUserId() + "", orderId,boo);
        if(stringObjectMap != null){
            if(StringUtils.isNotEmpty(user.getNickName())){
                stringObjectMap.put("nick",user.getNickName());
            }else{
                stringObjectMap.put("nick","");
            }
            if(StringUtils.isNotEmpty(user.getHeadImg())){
                stringObjectMap.put("image",user.getHeadImg());
            }else{
                stringObjectMap.put("image","");
            }
            return new Result("0000","success",stringObjectMap);
        }
        return new Result("2000","订单不存在",null);
    }

    public Result O2cBuyDetailsLog(User user,String orderId,String page,String boo){
        List<Map<String, Object>> maps;
        if (boo.equals("0"))
            maps = indexMapper.O2cBuyDetailsLog(user.getUserId() + "", orderId,Integer.valueOf(page)-1);
        else
            maps = indexMapper.O2cBuyDetailsLog_SELL(user.getUserId() + "", orderId,Integer.valueOf(page)-1);
        return new Result("0000","success",maps);
    }

    public Result orderBuystayLog(User user,String page,String boo){
        List<Map<String, Object>> maps = indexMapper.orderBuystayLog(user.getUserId() + "", Integer.valueOf(page)-1,boo);
        return new Result("0000","success",maps);
    }

    public Result O2cBuyTradeOver(User user,String logId,String boo){
        Map<String, Object> stringObjectMap;
        if (boo.equals("0"))
            stringObjectMap = indexMapper.O2cBuyTradeOver(user.getUserId() + "", logId);
        else
            stringObjectMap = indexMapper.O2cBuyTradeOver_SELL(user.getUserId() + "", logId);
        if(stringObjectMap != null){
            return new Result("0000","success",stringObjectMap);
        }
        return new Result("2000","success",stringObjectMap);
    }


    public Result O2cBuyDetailsMsg(User user,String logId,String boo) throws ParseException {
        Map<String, Object> stringObjectMap;
        if (boo.equals("0")) {
            stringObjectMap = indexMapper.O2cBuyDetailsMsg(user.getUserId() + "", logId,boo);
        } else {
            stringObjectMap = indexMapper.O2cBuyDetailsMsg_SELL(user.getUserId() + "", logId,boo);
        }
        String signintime = stringObjectMap.get("signtime").toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long l = (sdf.parse(signintime).getTime() / 1000) + overTimeNIN; //超时时间
        long time = System.currentTimeMillis()/1000;//当前时间
        stringObjectMap.put("endTime",l-time);
        return new Result("0000","success",stringObjectMap);
    }



    public Result O2cQuotation(User user){
//        Map<String, Object> yesDayCaverage = indexMapper.yesDayCaverage(); //昨日数据
//        Map<String, Object> nowCaverage = indexMapper.nowCaverage(); // 当日数据
//        Map<String, Object> selectOtcAllNum = indexMapper.selectOtcAllNum(); //成交总量
//        Map<String, Object> thisPrice = indexMapper.thisPrice(); //当前成交价格
//        Map<String, Object> yesDayPrice = indexMapper.yesDayPrice();
        Map<String,Object> result = new HashMap<>();
        String yesterday =  QueryTime.queryYesterday();
        String today = QueryTime.queryToday();
        String yes_price = RedisUtilsEx.get(RedisKeys.O2C_TOP_PRICE + yesterday);
        String today_price = RedisUtilsEx.get(RedisKeys.O2C_TOP_PRICE + today);
        String yes_low_price = RedisUtilsEx.get(RedisKeys.O2C_LOW_PRICE + yesterday);
        String today_low_price = RedisUtilsEx.get(RedisKeys.O2C_LOW_PRICE + today);
        String otc_count = RedisUtilsEx.get(RedisKeys.O2C_COUNT + yesterday);
        String count_today = RedisUtilsEx.get(RedisKeys.O2C_COUNT + today);
        yes_price = StringUtils.isEmpty(yes_price) ? "0" : yes_price;
        today_price = StringUtils.isEmpty(today_price) ? "0" : today_price;
        yes_low_price = StringUtils.isEmpty(yes_low_price) ? "0" : yes_low_price;
        today_low_price = StringUtils.isEmpty(today_low_price) ? "0" : today_low_price;
        result.put("caveragePrice",(new BigDecimal(yes_price).add(new BigDecimal(yes_low_price)).divide(new BigDecimal(2),2,RoundingMode.DOWN)));// 昨日均价
        result.put("caverageNum" ,StringUtils.isEmpty(otc_count) ? "0" : otc_count);//昨日成交量
        result.put("caverageMaxPrice" ,yes_price);//昨日最高价
        result.put("toDayPrice",(new BigDecimal(today_price).add(new BigDecimal(today_low_price))).divide(new BigDecimal(2),2,RoundingMode.DOWN));//今日均价
        result.put("toDayNum",StringUtils.isEmpty(count_today) ? "0" : count_today);//今日成交量
        result.put("toDayMaxPrice",StringUtils.isEmpty(today_price) ? yes_price : today_price);//今日最高价
        Set<String> keys = RedisUtilsEx.keys(RedisKeys.O2C_COUNT + "*");
        BigDecimal total = BigDecimal.ZERO;
        for (String key : keys)
            total = total.add(new BigDecimal(RedisUtilsEx.get(key)));
        result.put("allNum",total);//成交总量
        String now_price = RedisUtilsEx.get(RedisKeys.NOW_PRICE);
        result.put("thisPrice",StringUtils.isEmpty(now_price) ? 0 : now_price);//当前成交价格
        if (yes_price.equals("0"))
            result.put("bili","0%");
        else
            result.put("bili",(new BigDecimal(today_price).subtract(new BigDecimal(yes_price)))
                    .divide(new BigDecimal(yes_price),4,RoundingMode.DOWN)
                    .multiply(new BigDecimal(100)).setScale(4,RoundingMode.DOWN).toString() + "%");
        return new Result("0000","success",result);
    }

    public Result waitForZL(User user,String orderId,String boo) throws ParseException {
        Map<String, Object> stringObjectMap;
        if (boo.equals("0"))
            stringObjectMap = indexMapper.waitForZL(user.getUserId() + "", orderId);
        else
            stringObjectMap = indexMapper.waitForZL_SELL(user.getUserId() + "", orderId);

        if(stringObjectMap != null ){
            String endTime = stringObjectMap.get("endTime").toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long l = (sdf.parse(endTime).getTime() / 1000) + overTime24Hour; //超时时间
            long time = System.currentTimeMillis()/1000;//当前时间
            stringObjectMap.put("endTimeHour",l-time);
            return new Result("0000","success",stringObjectMap);
        }
        return new Result("2000","success",null);
    }

    public Result waitForMoney(User user,String logId,String boo) throws ParseException {
        Map<String, Object> map;
        if (boo.equals("0")){
            map = indexMapper.waitForMoney(user.getUserId() + "", logId);
        } else
            map = indexMapper.waitForMoney_SELL(user.getUserId() + "", logId);
        if(map != null){
            String signintime = map.get("signtime").toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long l = (sdf.parse(signintime).getTime() / 1000) + overTimeNIN; //超时时间
            long time = System.currentTimeMillis()/1000;//当前时间
            map.put("endTime",l-time);
            UserMoney userMoney = indexMapper.queryUserMoney(user.getUserId());
            BigDecimal percent = vipPercentFromMoney(userMoney);
            BigDecimal needCount = new BigDecimal(map.get("num").toString()).multiply(BigDecimal.ONE.add(BigDecimal.ONE.subtract(percent)).setScale(4,RoundingMode.HALF_DOWN));
            map.put("needCount",needCount);
            map.put("fee",needCount.subtract(new BigDecimal(map.get("num").toString())));
            map.put("money",new BigDecimal(map.get("price").toString()).multiply(new BigDecimal(map.get("num").toString())));
            return new Result("0000","success",map);
        }
        return new Result("2000","订单不存在",null);
    }

    public Result comitOrderMoney(User user,String logId,String boo) throws ParseException {
        Map<String, Object> map;
        if (boo.equals("0"))
            map = indexMapper.comitOrderMoney(user.getUserId() + "", logId);
        else
            map = indexMapper.comitOrderMoney_SELL(user.getUserId() + "", logId);
        if(map != null){
            String endTime = map.get("signtime").toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long l = (sdf.parse(endTime).getTime() / 1000) + overTime24Hour; //超时时间
            long time = System.currentTimeMillis()/1000;//当前时间
            map.put("endTimeHour",l-time);
            UserMoney userMoney = indexMapper.queryUserMoney(user.getUserId());
            BigDecimal percent = vipPercentFromMoney(userMoney);
            BigDecimal needCount = new BigDecimal(map.get("num").toString()).multiply(BigDecimal.ONE.add(BigDecimal.ONE.subtract(percent)).setScale(4,RoundingMode.HALF_DOWN));
            map.put("needCount",percent.multiply(new BigDecimal(100)).toPlainString()+"%");
            map.put("fee",needCount.subtract(new BigDecimal(map.get("num").toString())));
            return new Result("0000","success",map);
        }
        return new Result("2000","订单不存在",null);

    }

    public  Result comitOrderZl(User user,String logId,String boo){
        Map<String, Object> map;
        if (boo.equals("0"))
            map = indexMapper.comitOrderZl(user.getUserId() + "", logId);
        else
            map = indexMapper.comitOrderZl_SELL(user.getUserId() + "", logId);
        if(map != null){
            UserMoney userMoney = indexMapper.queryUserMoney(user.getUserId());
            BigDecimal percent = vipPercentFromMoney(userMoney);
            BigDecimal needCount = new BigDecimal(map.get("num").toString()).multiply(BigDecimal.ONE.add(BigDecimal.ONE.subtract(percent)).setScale(4,RoundingMode.HALF_DOWN));
            map.put("needCount",needCount);
            map.put("fee",needCount.subtract(new BigDecimal(map.get("num").toString())));
            map.put("money",new BigDecimal(map.get("price").toString()).multiply(new BigDecimal(map.get("num").toString())));
            return new Result("0000","success",map);
        }
        return new Result("2000","订单不存在",null);
    }

    public Result comitOrderThirtyNum(User user,String type){
        Map<String, Object> stringObjectMap = indexMapper.comitOrderThirtyNum(user.getUserId(),type);
        if(stringObjectMap != null){
            return new Result("0000","success",stringObjectMap);
        }
        return new Result("0000","success",new HashMap<String,Object>(){
            {
                put("threeDayNum","0");
            }
        });
    }

    public Result OrderAppeal(User user, String logId){
        int i = indexMapper.OrderAppeal(user.getUserId(), Integer.parseInt(logId));
        if( i == 1){
            return new Result("0000","申诉成功","");
        }else{
            return new Result("2000","申诉失败","");
        }
    }
    public Result addAliUrl(User user, String aliUrl,String token){
        int i = indexMapper.addAliUrl(String.valueOf(user.getUserId()), aliUrl);
        if( i == 1){
            user.setAliQrCode(aliUrl);
            RedisUtilsEx.set(token,JSONObject.toJSONString(user));
            return new Result("0000","添加成功","");
        }else{
            return new Result("4000","添加失败","");
        }
    }


}
