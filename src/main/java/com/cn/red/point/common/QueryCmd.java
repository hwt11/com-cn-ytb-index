package com.cn.red.point.common;

import com.alibaba.fastjson.JSONObject;
import com.cn.red.point.common.enity.User;
import org.springframework.ui.ModelMap;

import java.text.ParseException;

public abstract class QueryCmd {
    public static final QueryCmd QUERY_CMD = new QueryCmd() {
        @Override
        public void execute(ModelMap modelMap) {
            modelMap.addAttribute("Data",JSONObject.toJSON(new Object()));
        }
    };

    public QueryCmd(){}

    public void execute(ModelMap modelMap){}

    public Object  query() throws Exception {
        return  null;
    }

    public Object query(User user) throws ParseException {
        return null;
    }
}
