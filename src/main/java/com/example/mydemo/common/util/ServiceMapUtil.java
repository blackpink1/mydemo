package com.example.mydemo.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * map工具类
 */
public class ServiceMapUtil {

    /**
     * 可添加共同属性
     * @return
     */
    public static Map<String,Object> getMap(){
        return new HashMap<String,Object>();
    }

    /**
     * 可添加共同属性
     * @param params
     * @return
     */
    public static Map<String,Object> getMap(Map<String,Object> params){
        return params;
    }
}
