package com.cn.red.point.common;

import com.alibaba.fastjson.JSONObject;
import com.cn.red.point.common.enity.Result;
import com.cn.red.point.common.enity.User;
import com.cn.red.point.redis.RedisUtilsEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class Action {

    public static Logger LOGGER = LoggerFactory.getLogger(Action.class);

    protected String query(String path){
        return path;
    }

    public boolean _checkSecret(HttpServletRequest request) {
        Enumeration<String> enumeration = request.getParameterNames();
        List<String> list = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            if (key.equals("ppa"))
                continue;
            list.add(request.getParameter(key));
        }
        if (list.isEmpty())
            return true;
        String ppa = request.getParameter("ppa");
        if (StringUtils.isEmpty(ppa))
            return false;
        String[] val = new String[list.size()];
        list.toArray(val);
        if (ppa.equals(Code._getToken(val)))
            return true;
        return false;
    }

    private boolean checkSecret(HttpServletRequest request) {
        Enumeration<String> enumeration = request.getParameterNames();
        List<String> list = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            if (key.equals("ppa"))
                continue;
            list.add(request.getParameter(key));
        }
        if (list.isEmpty())
            return true;
        String ppa = request.getParameter("ppa");
        if (StringUtils.isEmpty(ppa))
            return false;
        String[] val = new String[list.size()];
        list.toArray(val);
        if (ppa.equals(Code.getToken(val)))
            return true;
        return false;
    }

    protected String ajaxQuery(QueryCmd cmd,String token,HttpServletRequest request){
        if (!checkSecret(request))
            return JSONObject.toJSONString(Result.ERROR.setMsg("请求错误").setData(null));
        String object = RedisUtilsEx.get(token);
        if (StringUtils.isEmpty(object))
            return JSONObject.toJSONString(Result.NO_SIGN);
        try {
            User user = JSONObject.parseObject(object, User.class);
            return JSONObject.toJSONString(cmd.query(user));
        } catch (Exception e){
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return JSONObject.toJSONString(Result.ERROR.setMsg(e.getMessage()));
        }
    }

    protected String ajaxSimpleQuery(QueryCmd cmd,HttpServletRequest request){
        if (!checkSecret(request))
            return JSONObject.toJSONString(Result.ERROR.setMsg("请求错误").setData(null));
        try {
            return JSONObject.toJSONString(cmd.query());
        } catch (Exception e){
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return JSONObject.toJSONString(Result.ERROR.setMsg("system error").setData(null));
        }
    }

    protected String ajaxGameSimpleQuery(QueryCmd cmd,HttpServletRequest request){
        if (!_checkSecret(request))
            return JSONObject.toJSONString(Result.ERROR.setMsg("请求错误").setData(null));
        try {
            return JSONObject.toJSONString(cmd.query());
        } catch (Exception e){
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return JSONObject.toJSONString(Result.ERROR.setMsg("system error").setData(null));
        }
    }


    protected Map<String,String> getParams(HttpServletRequest request){
        Map<String,String> map = new HashMap<>();
        Enumeration<String> keys = request.getParameterNames();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            map.put(key,request.getParameter(key));
        }
        return map;
    }
}
