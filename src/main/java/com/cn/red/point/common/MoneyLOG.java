package com.cn.red.point.common;

public class MoneyLOG {
    private int userId;
    private String content;
    private String price;
    private String type; //0充值 1提现 2消费 3 c2c市场收入,4城主分成,5城主提现
    private String xtype; //提现状态（0申请中，10 成功，5搁置，-1 失败）

    public MoneyLOG(int userId, String content, String price,String type,String xtype) {
        this.userId = userId;
        this.content = content;
        this.price = price;
        this.type = type;
        this.xtype = xtype;
    }

    public MoneyLOG(int userId, String content, String price,String type) {
        this.userId = userId;
        this.content = content;
        this.price = price;
        this.type = type;
    }

    public String getXtype() {
        return xtype;
    }

    public void setXtype(String xtype) {
        this.xtype = xtype;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
