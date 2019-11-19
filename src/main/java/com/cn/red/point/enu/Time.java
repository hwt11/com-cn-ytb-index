package com.cn.red.point.enu;

public enum Time {
    DAY(86400),
    WEEK(604800),
    EIGHTDAYS(691200),
    TWODAY(172800),
    TENMINUTE(600),
    THREEHOUR(10800),
    ONEHOUR(3600),
    FIFTEEN(1296000),
    ONEYEAR(31536000),
    THREEDAY(259200),
    MOUNTH(2592000),
    MOUNTH31(2678400),
    GAME(20);

    private int sec;

    Time(int sec) {
        this.sec = sec;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }
}
