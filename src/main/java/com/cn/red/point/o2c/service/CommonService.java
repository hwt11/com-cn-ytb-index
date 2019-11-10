package com.cn.red.point.o2c.service;

import com.cn.red.point.o2c.mapper.LoggerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CommonService {

    @Autowired
    private LoggerMapper loggerMapper;

    protected ExecutorService executorService = Executors.newFixedThreadPool(20);
    /**
     * 更新指令牌日志 type 0加 1减
     * @param userId
     * @param ZLMoney
     * @param type 0加 1减
     * @param msg
     */
    protected void updateZLMoneyLOG(int userId,double ZLMoney,int type,String msg){
        executorService.execute(()->loggerMapper.insertZLMoneyLOG(new HashMap<>(){
            {
                put("userId",userId);
                put("num",ZLMoney);
                put("msg",msg);
                put("type",type);
            }
        }));
    }

}
