package com.cn.red.point.enu;

public enum Status {
    HS("HS","http://web.juhe.cn:8080/finance/stock/hs"),
    HK("HK","http://web.juhe.cn:8080/finance/stock/hk"),
    USA("USA","http://web.juhe.cn:8080/finance/stock/usa"),
    HKALL("HKALL","http://web.juhe.cn:8080/finance/stock/hkall"),
    USAALL("USAALL","http://web.juhe.cn:8080/finance/stock/usaall"),
    SZALL("SZALL","http://web.juhe.cn:8080/finance/stock/szall"),
    SHALL("SHALL","http://web.juhe.cn:8080/finance/stock/shall");

    private String URL;
    private String KEY;

    public String getKEY() {
        return KEY;
    }

    public void setKEY(String KEY) {
        this.KEY = KEY;
    }

    Status(String URL, String KEY) {
        this.URL = URL;
        this.KEY = KEY;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    Status(String URL) {
        this.URL = URL;
    }
}
