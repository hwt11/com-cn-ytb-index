package com.cn.red.point.o2c.mapper;

import com.cn.red.point.o2c.common.UserMoney;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IndexMapper {
    Map<String, Object> queryUserAliPay(int userId);
    List<Map<String, Object>> queryOrders(@Param("page") int page, @Param("sort") String sort,@Param("search") String search,@Param("boo") String boo);
    List<Map<String, Object>> queryOrders_DESC(@Param("page") int page, @Param("sort") String sort,@Param("search") String search,@Param("boo") String boo);
    void insertOrder(Map<String,Object> map);
    UserMoney queryUserMoney(int userId);
    Map<String, Object> queryOrder(String orderId);
    void updateUserZLMoney(Map<String,Object> map);
    void insertOrderLog(Map<String,Object> map);
    void updateOrder(Map<String,Object> map);
    Map<String, Object> queryOrderFromMQ(String id);
    Map<String, Object> queryOrderFromMQ15(String id);
    void cancelOrderLog(Map<String,Object> map);
    void cancelOrder(String orderId);
    Map<String,Object> queryOrderByUser(Map<String,Object> map);
    Map<String,Object> querySellOrderByUser(Map<String,Object> map);
    void updateOrderStatus(Map<String,Object> map);
    void updateSellOrderStatus(Map<String,Object> map);
    void updateOrderStatusEND(Map<String,Object> map);
    Map<String,Object> queryOrderByEnemy(Map<String,Object> map);
    Map<String,Object> querySellOrderByEnemy(Map<String,Object> map);
    void insertAliPay(Map<String,Object> map);
    List<Map<String,Object>> findO2cByUserId(@Param("userId")String userId,@Param("pageNum")int pageNum,@Param("boo") String boo);
    List<Map<String,Object>> findO2cByUserIdSell(@Param("userId")String userId,@Param("pageNum")int pageNum,@Param("boo") String boo);
    List<Map<String,Object>> findO2cByUserIdFinish(@Param("userId")String userId,@Param("pageNum")int pageNum,@Param("boo") String boo);
    List<Map<String,Object>> findO2cByUserIdFinishSell(@Param("userId")String userId,@Param("pageNum")int pageNum,@Param("boo") String boo);
    Map<String,Object> O2cBuyDetails(@Param("userId")String userId, @Param("orderId")String orderId,@Param("boo") String boo);
    List<Map<String,Object>> O2cBuyDetailsLog(@Param("userId")String userId, @Param("orderId")String orderId,@Param("page")int page);
    List<Map<String,Object>> O2cBuyDetailsLog_SELL(@Param("userId")String userId, @Param("orderId")String orderId,@Param("page")int page);
    List<Map<String,Object>> orderBuystayLog(@Param("userId")String userId,@Param("page")int page,@Param("boo") String boo);
    Map<String,Object> O2cBuyDetailsMsg(@Param("userId")String userId,@Param("logId")String logId,@Param("boo") String boo);
    Map<String,Object> O2cBuyDetailsMsg_SELL(@Param("userId")String userId,@Param("logId")String logId,@Param("boo") String boo);
    Map<String,Object> O2cBuyTradeOver(@Param("userId")String userId,@Param("logId")String logId);
    Map<String,Object> O2cBuyTradeOver_SELL(@Param("userId")String userId,@Param("logId")String logId);
    Map<String,Object> queryUserOrder(Map<String,Object> map);
    Map<String,Object> queryUserSellOrder(Map<String,Object> map);
    void cancelUserOrder(Map<String,Object> map);
    Map<String,Object> waitForZL(@Param("userId")String userId,@Param("orderId")String orderId);
    Map<String,Object> waitForZL_SELL(@Param("userId")String userId,@Param("orderId")String orderId);
    Map<String,Object> waitForMoney(@Param("userId")String userId,@Param("logId")String logId);
    Map<String,Object> waitForMoney_SELL(@Param("userId")String userId,@Param("logId")String logId);
    Map<String,Object> comitOrderMoney(@Param("userId")String userId,@Param("logId")String logId);
    Map<String,Object> comitOrderMoney_SELL(@Param("userId")String userId,@Param("logId")String logId);
    Map<String,Object> comitOrderZl(@Param("userId")String userId,@Param("logId")String logId);
    Map<String,Object> comitOrderZl_SELL(@Param("userId")String userId,@Param("logId")String logId);
    Map<String,Object> comitOrderThirtyNum(@Param("userId")int userId,@Param("type")String type);
    Map<String,Object> comitOrderThirtyNum_SELL(@Param("userId")int userId,@Param("type")String type);
    int OrderAppeal(@Param("userId")int userId,@Param("logId")int logId);
    int queryOrderCount(int id);
    Map<String, Object> querySellOrder(String orderId);
    Map<String, Object> queryOrderFromMQ_Sell(String id);
    Map<String, Object> queryOrderFromMQ150(String id);
    void updateUserOrder(Map<String,Object> map);
}