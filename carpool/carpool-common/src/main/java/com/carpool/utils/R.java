package com.carpool.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2016年10月27日 下午9:59:27
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
    	put("code", 0);
    }

    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(500, msg);
    }
    
    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }
    
    public static R error(Object data) {
    	R r = new R();
        r.put("code", 500);
        r.put("msg", "未知异常，请联系管理员");
        r.put("data", data);
        return r;
    }

    public static R ok() {
    	R r = new R();
    	r.put("code", 0);
        r.put("msg", "success");
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("code", 0);
        r.put("msg", msg);
        return r;
    }
    
    public static R ok(Object data) {
        R r = new R();
        r.put("code", 0);
        r.put("msg", "success");
        r.put("data", data);
        return r;
    }
    
    public static R ok(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }
    
    public static R ok(int code, String msg, Object data) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        r.put("data", data);
        return r;
    }
    

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.put("code", 0);
        r.put("msg", "success");
        r.putAll(map);
        return r;
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
