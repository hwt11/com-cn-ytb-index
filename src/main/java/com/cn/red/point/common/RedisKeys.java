package com.cn.red.point.common;

public class RedisKeys {
    public static final String MINERAL_LIVE_TIME = "MINERAL_TIME-";       //矿机的唯一标识  USER_TIME-10004-11-15-30-4000-2000-addressId
    public static final String MINERAL_ADDRESS_COUNT = "MINERAL_ADDRESS_COUNT_";    //区域下的矿机数量 MINERAL_ADDRESS_COUNT_ + addressId
    public static final String USER_OSTRICH = "USER_OSTRICH_";                      //用户拥有的鸵鸟     USER_OSTRICH_ + userId + _ + ostrichId
    public static final String OSTRICH_WORK_MINERAL = "OSTRICH_WORK_MINERAL_";      //鸵鸟工作的矿机  OSTRICH_WORK_MINERAL_ ostrichId  =  mineralId
    public static final String OSTRICH_DAY_WORK_TIME = "OSTRICH_DAY_WORK_TIME_";    //鸵鸟每天的工作次数 OSTRICH_DAY_WORK_TIME_ QueryTime.Today + _ + ostrichId
    public static final String OSTRICH_WORK_PERCENT_ADD = "OSTRICH_WORK_PERCENT_ADD_";  //用户矿机，鸵鸟增值 OSTRICH_WORK_PERCENT_ADD_ + Today _ userId + _ + mineralId;
    public static final String OSTRICH_WORK_PERCENT_SUB = "OSTRICH_WORK_PERCENT_SUB_";  //用户矿机，百分比减少 OSTRICH_WORK_PERCENT_SUB_ + Today _ userId + _ + mineralId;
    public static final String OSTRICH_TAKE_MY_MINERAL = "OSTRICH_TAKE_MY_MINERAL_";    //偷走我的矿机资源最近5个人 OSTRICH_TAKE_MY_MINERAL_ + userId + mineralId + today
    public static final String OSTRICH_TAKE_WHO_MINERAL = "OSTRICH_TAKE_WHO_MINERAL_";    //偷走我的矿机资源最近10个人 OSTRICH_TAKE_WHO_MINERAL_ + userId + mineralId + today
    public static final String MINERAL_TEN_SEC_GET_ = "MINERAL_TEN_SEC_GET_";  //矿机每10秒种更新一次收益
    public static final String BOSS_PERCENT = "BOSS_PERCENT";                   //比例
    public static final String MINERAL_ZLMONEY_ALL = "MINERAL_ZLMONEY_ALL_";    //矿机总共获得的产值    MINERAL_ZLMONEY_ALL + userid + mineralid
    public static final String USER_BUY_ADDRESS = "USER_BUY_ADDRESS_";          //用户购买的地区       USER_BUY_ADDRESS_ + addressId    ; value = userId
    public static final String ZL_TO_ML_PERCENT = "ZL_TO_ML_PERCENT";           //指令兑换锚令的比例
    public static final String ZL_TO_ML_MAX = "ZL_TO_ML_MAX_";                  //指令兑换锚令的每天最大数
    public static final String RMB_TO_ML_MAX = "RMB_TO_ML_MAX_";                  //指令兑换锚令的每天最大数
    public static final String C2C_BUY_AVERAGE = "C2C_BUY_AVERAGE_";            //C2C购买均价
    public static final String C2C_SELL_AVERAGE = "C2C_SELL_AVERAGE_";            //C2C出售均价
    public static final String C2C_DAY_COUNT = "C2C_DAY_COUNT_";                //C2C每日成交量
    public static final String USER_CITY_ADMIN = "USER-CITY-ADMIN_";            //城主
    public static final String USER_EQUITY_COLD = "USER_EQUITY_COLD_";          //股市交易冻结
    public static final String C2C_SELL_TOP = "C2C_SELL_TOP_";                  //每日最高价
    public static final String C2C_SELL_LOW = "C2C_SELL_LOW_";                  //每日最低价
    public static final String C2C_BUY_TOP = "C2C_BUY_TOP_";                  //每日最高价
    public static final String C2C_BUY_LOW = "C2C_BUY_LOW_";                  //每日最低价
    public static final String REFEREE_ML = "REFEREE_ML_";                      //大班锚令
    public static final String REFEREE2_ML = "REFEREE2_ML_";                      //小班锚令
    public static final String MONEY_REFEREE = "MONEY_REFEREE_";                //大班RMB奖励总和
    public static final String MONEY_REFEREE2 = "MONEY_REFEREE2_";                //小班RMB奖励总和
    public static final String ZL_MONEY_REFEREE = "ZL_MONEY_REFEREE_";              //指令统计
    public static final String ZERO_LU_GUAN_= "ZERO-LU-GUAN-";                  //零撸人员
    public static final String ZERO_LU_GUAN_FIVE= "ZERO_LU_GUAN_FIVE_";                  //零撸人员  5个日活奖励
    public static final String ZERO_LU_GUAN_TEN= "ZERO_LU_GUAN_TEN_";                  //零撸人员      10个日活奖励
    public static final String ZL_SORT_ = "ZL_SORT_"                ;               //指令排名
    public static final String VIP_FOUR_MINERAL = "VIP_FOUR_MINERAL_";              //VIP购买4号矿机数量
    public static final String VIP2_FOUR_MINERAL = "VIP2_FOUR_MINERAL_";            //VI[2购买4号矿机数量
    public static final String USER_O2C = "USER_O2C_";
    public static final String USER_O2C_COUNT = "USER_O2C_COUNT_";
    public static final String O2C_TOP_PRICE = "O2C_TOP_PRICE_";           //O2C最高价
    public static final String O2C_LOW_PRICE = "O2C_LOW_PRICE_";                //O2C最低价
    public static final String O2C_COUNT = "O2C_COUNT_";                //O2C总量
    public static final String NOW_PRICE = "NOW_PRICE";    //当前成交价格
}
