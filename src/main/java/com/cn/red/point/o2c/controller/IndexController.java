package com.cn.red.point.o2c.controller;

import com.cn.red.point.common.Action;
import com.cn.red.point.common.QueryCmd;
import com.cn.red.point.common.QueryTime;
import com.cn.red.point.common.StringUtils;
import com.cn.red.point.common.enity.Result;
import com.cn.red.point.common.enity.User;
import com.cn.red.point.o2c.service.IndexService;
import com.cn.red.point.redis.RedisUtilsEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;

@RestController
@RequestMapping(value = "o2c", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
public class IndexController extends Action {

    @Autowired
    private IndexService indexService;

//    @RequestMapping("alipay")
//    public String alipay(HttpServletRequest request,
//                         @RequestParam(value = "token") String token) {
//        return ajaxQuery(new QueryCmd() {
//            @Override
//            public Object query(User user) {
//                return indexService.queryUserAliPay(user);
//            }
//        },token,request);
//    }
//
//    @RequestMapping("commitAliPay")
//    public String commitAliPay(HttpServletRequest request,
//                               @RequestParam(value = "token") String token,
//                               @RequestParam(value = "name") String name,
//                               @RequestParam(value = "payno") String payno,
//                               @RequestParam(value = "image") String image,
//                               @RequestParam(value = "phone") String phone,
//                               @RequestParam(value = "password") String password) {
//        return ajaxQuery(new QueryCmd() {
//            @Override
//            public Object query(User user) {
//                return indexService.commitAliPay(user,name,payno,image,phone,password);
//            }
//        },token,request);
//    }

    @RequestMapping("orders")
    public String orders(HttpServletRequest request,
                         @RequestParam(value = "page",defaultValue = "1") int page,
                         @RequestParam(value = "type",defaultValue = "1") int type,
                         @RequestParam(value = "search",defaultValue = "") String search,
                         @RequestParam(value = "boo") String boo) {
        return ajaxSimpleQuery(new QueryCmd() {
            @Override
            public Object query() {
                return indexService.queryOrders(page,type,search,boo);
            }
        },request);
    }

    /**
     * 发布买单
     * @param request
     * @param token
     * @param price
     * @param count
     * @return
     */
    @RequestMapping("buy")
    public String buy(HttpServletRequest request,
                      @RequestParam(value = "token") String token,
                      @RequestParam(value = "price") String price,
                      @RequestParam(value = "count") int count,
                      @RequestParam(value = "password") String passWord){
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                if (!QueryTime.checkTime())
                    return Result.ERROR.setMsg("OTC在早9点至晚9点开放").setData(null);
                if (new BigDecimal(price).compareTo(new BigDecimal(2)) < 0)
                    return Result.ERROR.setMsg("单价不能低于2元").setData(null);
                return indexService.insertOrder(user,price,count,passWord);
            }
        },token,request);
    }

    @RequestMapping("sell")
    public String sell(HttpServletRequest request,
                       @RequestParam(value = "token") String token,
                       @RequestParam(value = "price") String price,
                       @RequestParam(value = "count") int count,
                       @RequestParam(value = "password") String password) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) throws ParseException {
                if (!QueryTime.checkTime())
                    return Result.ERROR.setMsg("OTC在早9点至晚9点开放").setData(null);
                if (new BigDecimal(price).compareTo(new BigDecimal(2)) != 0)
                    return Result.ERROR.setMsg("单价必须为2元").setData(null);
                return indexService.insertSellOrder(user,price,count,password);
            }
        },token,request);
    }

    /**
     * 出售给买单
     */
    @RequestMapping("carryBuy")
    public String carryBuy(HttpServletRequest request,
                           @RequestParam(value = "token") String token,
                           @RequestParam(value = "orderId") String orderId,
                           @RequestParam(value = "count") int count) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.carryBuy(user,orderId,count);
                } catch (Exception e){
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }
            }
        },token,request);
    }

    @RequestMapping("carrySell")
    public String carrySell(HttpServletRequest request,
                            @RequestParam(value = "token") String token,
                            @RequestParam(value = "orderId") String orderId,
                            @RequestParam(value = "count") int count) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                return indexService.carrySell(user,orderId,count);
            }
        },token,request);
    }

    @RequestMapping("cancelOrder")
    public String cancelOrder(HttpServletRequest request,
                              @RequestParam(value = "token") String token,
                              @RequestParam(value = "orderId") String orderId){
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) throws ParseException {
                return indexService.cancelOrderByUser(user,orderId);
            }
        },token,request);
    }

    @RequestMapping("cancelSellOrder")
    public String cancelSellOrder(HttpServletRequest request,
                                  @RequestParam(value = "token") String token,
                                  @RequestParam(value = "orderId") String orderId) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) throws ParseException {
                return indexService.cancelSellByUser(user,orderId);
            }
        },token,request);
    }

    /**
     * 玩家支付图片
     */
    @RequestMapping("payOrder")
    public String payOrder(HttpServletRequest request,
                           @RequestParam(value = "token") String token,
                           @RequestParam(value = "orderId") String orderId,
                           @RequestParam(value = "image") String image,
                           @RequestParam(value = "userPay") String userPay) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.payOrder(user, orderId, image,userPay);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }
            }
        },token,request);
    }

    @RequestMapping("paySellOrder")
    public String paySellOrder(HttpServletRequest request,
                           @RequestParam(value = "token") String token,
                           @RequestParam(value = "orderId") String orderId,
                           @RequestParam(value = "image") String image,
                           @RequestParam(value = "userPay") String userPay) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.paySellOrder(user, orderId, image,userPay);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }
            }
        },token,request);
    }

    /**
     * 最终确认
     */
    @RequestMapping("commitOrder")
    public String commitOrder(HttpServletRequest request,
                              @RequestParam(value = "token") String token,
                              @RequestParam(value = "orderId") String orderId,
                              @RequestParam(value = "password")String password) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.commitOrder(user,orderId,password);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }

            }
        },token,request);
    }

    @RequestMapping("commitSellOrder")
    public String commitSellOrder(HttpServletRequest request,
                              @RequestParam(value = "token") String token,
                              @RequestParam(value = "orderId") String orderId,
                              @RequestParam(value = "password")String password) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.commitSellOrder(user,orderId,password);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }

            }
        },token,request);
    }

    @RequestMapping("getNeedReplay")
    public String needReplay(HttpServletRequest request,
                             @RequestParam(value = "token") String token) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                String val = RedisUtilsEx.get("ORDER_NEED_" + user.getUserId());
                if (StringUtils.isNotEmpty(val))
                    return new Result().setCode("0009").setMsg("").setData(null);
                else
                    return Result.OK.setMsg("").setData(null);
            }
        },token,request);
    }

    /**
     * otc交易记录
     */
    @RequestMapping("historyLogList")
    public String historyLogList(HttpServletRequest request,
                              @RequestParam(value = "token") String token,
                              @RequestParam(value = "status") String status,
                              @RequestParam(value = "type")String type,
                              @RequestParam(value = "page")String page,
                              @RequestParam(value = "boo",defaultValue = "0") String boo) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.historyLogList(user,status,type,page,boo);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }

            }
        },token,request);
    }


    /**
     * otc交易记录详情 发布购买详情
     */
    @RequestMapping("O2cBuyDetails")
    public String O2cBuyDetails(HttpServletRequest request,
                                 @RequestParam(value = "token") String token,
                                 @RequestParam(value = "orderId")String orderId,
                                @RequestParam(value = "boo",defaultValue = "0") String boo) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.O2cBuyDetails(user,orderId,boo);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }

            }
        },token,request);
    }

    /**
     * otc交易记录详情 发布购买详情
     */
    @RequestMapping("O2cBuyDetailsLog")
    public String O2cBuyDetailsLog(HttpServletRequest request,
                                @RequestParam(value = "token") String token,
                                @RequestParam(value = "orderId")String orderId,
                                @RequestParam(value = "page")String page,
                                   @RequestParam(value = "boo",defaultValue = "0") String boo) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.O2cBuyDetailsLog(user,orderId,page,boo);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }

            }
        },token,request);
    }


    /**
     * otc交易记录详情 发布购买详情
     */
    @RequestMapping("orderBuyStayLog")
    public String orderBuystayLog(HttpServletRequest request,
                                   @RequestParam(value = "token") String token,
                                   @RequestParam(value = "page")String page,
                                  @RequestParam(value = "boo",defaultValue = "0") String boo) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.orderBuystayLog(user,page,boo);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }

            }
        },token,request);
    }
    /**
     * otc交易单笔记录信息
     */
    @RequestMapping("O2cBuyDetailsMsg")
    public String O2cBuyDetailsMsg(HttpServletRequest request,
                                   @RequestParam(value = "token") String token,
                                   @RequestParam(value = "logId")String logId,
                                   @RequestParam(value = "boo",defaultValue = "0") String boo) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.O2cBuyDetailsMsg(user,logId,boo);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }

            }
        },token,request);
    }

    /**
     * otc买单完成
     */
    @RequestMapping("O2cBuyTradeOver")
    public String O2cBuyTradeOver(HttpServletRequest request,
                                   @RequestParam(value = "token") String token,
                                   @RequestParam(value = "logId")String logId,
                                  @RequestParam(value = "boo",defaultValue = "0") String boo) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.O2cBuyTradeOver(user,logId,boo);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }

            }
        },token,request);
    }



    /**
     * otc交易行情
     */
    @RequestMapping("O2cQuotation")
    public String O2cQuotation(HttpServletRequest request,
                                   @RequestParam(value = "token") String token) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.O2cQuotation(user);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }

            }
        },token,request);
    }

    /**
     * otc等待对方确认释放指令牌
     */
    @RequestMapping("waitForZL")
    public String waitForZL(HttpServletRequest request,
                               @RequestParam(value = "token") String token,
                               @RequestParam(value = "orderId")String orderId,
                            @RequestParam(value = "boo",defaultValue = "0") String boo) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.waitForZL(user,orderId,boo);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }

            }
        },token,request);
    }

    /**
     * otc等待对方付款
     */
    @RequestMapping("waitForMoney")
    public String waitForMoney(HttpServletRequest request,
                            @RequestParam(value = "token") String token,
                            @RequestParam(value = "logId")String logId,
                               @RequestParam(value = "boo",defaultValue = "0") String boo) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.waitForMoney(user,logId,boo);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }

            }
        },token,request);
    }


    /**
     * otc对方已付款
     */
    @RequestMapping("comitOrderMoney")
    public String comitOrderMoney(HttpServletRequest request,
                               @RequestParam(value = "token") String token,
                               @RequestParam(value = "logId")String logId,
                                  @RequestParam(value = "boo",defaultValue = "0") String boo) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.comitOrderMoney(user,logId,boo);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }

            }
        },token,request);
    }


    /**
     * otc对方已付款
     */
    @RequestMapping("comitOrderZl")
    public String comitOrderZl(HttpServletRequest request,
                                  @RequestParam(value = "token") String token,
                                  @RequestParam(value = "logId")String logId,
                               @RequestParam(value = "boo",defaultValue = "0") String boo) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.comitOrderZl(user,logId,boo);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }

            }
        },token,request);
    }

    /**
     * 30日成交量
     */
    @RequestMapping("comitOrderThirtyNum")
    public String comitOrderThirtyNum(HttpServletRequest request,
                               @RequestParam(value = "token") String token,
                                      @RequestParam(value = "type")String type) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.comitOrderThirtyNum(user,type);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }

            }
        },token,request);
    }


    /**
     * 申诉
     */
    @RequestMapping("orderAppeal")
    public String OrderAppeal(HttpServletRequest request,
                                      @RequestParam(value = "token") String token,
                                      @RequestParam(value = "logId")String logId) {
        return ajaxQuery(new QueryCmd() {
            @Override
            public Object query(User user) {
                try {
                    return indexService.OrderAppeal(user,logId);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.ERROR.setMsg("system error").setData(null);
                }

            }
        },token,request);
    }

}
