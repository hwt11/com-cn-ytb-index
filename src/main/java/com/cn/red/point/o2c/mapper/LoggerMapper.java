package com.cn.red.point.o2c.mapper;

import com.cn.red.point.common.MoneyLOG;

import java.util.HashMap;
import java.util.Map;

public interface LoggerMapper {
    void insertMLMoneyLog(Map<String, Object> map);
    void insertMoneyLog(MoneyLOG moneyLOG);
    void insertZLMoneyLOG(Map<String, Object> map);
    void updateMoneyByRefereeLib(Map map);
    Map<String,Object> queryAddressId(String address);
    Map<String,Object> queryAddress(int userId);
    void updateAddressReturn(Map<String, Object> map);
    void updateAddressReturnBYName(Map<String, Object> map);
    Map<String,Object> queryName(String pid);
    void insertChinaLog(Map<String, Object> map);
    void updateReferee(Map<String, Object> map);
    String queryVIP(String userId);
    void updateZLMoney(Map<String, Object> map);
    String queryReferee(int userId);
    void updateZLPM(HashMap<String, Object> stringObjectHashMap);
    String queryReferee2(String addressId);
    void updateUserMoney(Map<String, Object> map);
}
