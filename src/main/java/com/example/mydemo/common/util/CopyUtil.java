package com.example.mydemo.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestUtils;
import com.example.mydemo.common.util.StrUtils;




public class CopyUtil {

    private static final Logger logger = LoggerFactory.getLogger(CopyUtil.class);
    private static ConvertUtilsBean convert = new ConvertUtilsBean();
    private static BeanUtilsBean beanUtils;

    static {
        beanUtils = new BeanUtilsBean(convert, new PropertyUtilsBean());
        convert.register(new Converter() {
            public Object convert(Class type, Object value) {
                if(value == null) {
                    return null;
                } else {
                    if(value instanceof String) {
                        if(StringUtils.isBlank((String)value)) {
                            return null;
                        }

                        try {
                            return DateUtils.parseDate(String.valueOf(value), new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm"});
                        } catch (IllegalArgumentException var4) {
                            throw new ConversionException("ill date patten:" + value);
                        } catch (ParseException var5) {
                            var5.printStackTrace();
                        }
                    }

                    return value;
                }
            }
        }, Date.class);
    }

    public CopyUtil() {
    }

    public static void copyProperties(Object dest, Object orig) {
        try {
            beanUtils.copyProperties(dest, orig);
        } catch (IllegalAccessException var3) {
            var3.printStackTrace();
        } catch (InvocationTargetException var4) {
            var4.printStackTrace();
        }

    }

    public static <T> T getBean(Class<T> clz, HttpServletRequest request, String var) {
        Object t = null;

        try {
            t = clz.newInstance();
            copyToObject(t, request, var);
        } catch (InstantiationException var5) {
            var5.printStackTrace();
        } catch (IllegalAccessException var6) {
            var6.printStackTrace();
        }

        return (T)t;
    }

    public static void copyToObject(Object obj, HttpServletRequest request, String var) {
        try {
            Map e = convertString(request, var);
            beanUtils.copyProperties(obj, e);
        } catch (IllegalAccessException var4) {
            logger.debug("Map中的数据Copy到对象出错误！");
            throw new ContextedRuntimeException("Map中的数据Copy到对象出错误！", var4);
        } catch (InvocationTargetException var5) {
            logger.debug("Map中的数据Copy到对象出错误！");
            throw new ContextedRuntimeException("Map中的数据Copy到对象出错误！", var5);
        }
    }

    public static <T> T getBean(Class<T> clz, HttpServletRequest request) {
        Object t = null;

        try {
            t = clz.newInstance();
            copyToObject(t, request);
        } catch (InstantiationException var4) {
            var4.printStackTrace();
        } catch (IllegalAccessException var5) {
            var5.printStackTrace();
        }

        return (T)t;
    }

    public static void copyToObject(Object obj, HttpServletRequest request) {
        try {
            Map e = convertString(request);
            beanUtils.copyProperties(obj, e);
        } catch (IllegalAccessException var3) {
            logger.debug("Map中的数据Copy到对象出错误！");
            throw new ContextedRuntimeException("Map中的数据Copy到对象出错误！", var3);
        } catch (InvocationTargetException var4) {
            logger.debug("Map中的数据Copy到对象出错误！");
            throw new ContextedRuntimeException("Map中的数据Copy到对象出错误！", var4);
        }
    }


    public static void copyToObject(Object obj, Map<String, Object> map) {
        try {
            beanUtils.copyProperties(obj, map);
        } catch (IllegalAccessException var3) {
            logger.debug("Map中的数据Copy到对象出错误！");
            throw new ContextedRuntimeException("Map中的数据Copy到对象出错误！", var3);
        } catch (InvocationTargetException var4) {
            logger.debug("Map中的数据Copy到对象出错误！");
            throw new ContextedRuntimeException("Map中的数据Copy到对象出错误！", var4);
        }
    }

    public static Map<String, Object> copyMap(Map<String, String> qm) {
        HashMap params = new HashMap();
        Iterator iter = qm.entrySet().iterator();

        while(iter.hasNext()) {
            Entry e = (Entry)iter.next();
            params.put((String)e.getKey(), e.getValue());
        }

        return params;
    }

    public static Map copyToNewMap(Map qm) {
        HashMap params = new HashMap();
        Iterator iter = qm.entrySet().iterator();

        while(iter.hasNext()) {
            Entry e = (Entry)iter.next();
            params.put(e.getKey(), e.getValue());
        }

        return params;
    }

    public static Map<String, Object> copyMap(HttpServletRequest request) {
        return copyMap(convertString(request));
    }

    public static Map<String, Object> copyMap(HttpServletRequest request, boolean isFixListStr) {
        Map par = copyMap(convertString(request));
        if(isFixListStr) {
            Set keySet = par.keySet();
            Iterator var5 = keySet.iterator();

            while(var5.hasNext()) {
                String key = (String)var5.next();
                if(key.endsWith("List")) {
                    Object obj = par.get(key);
                    par.put(key, Arrays.asList(String.valueOf(obj).split(",")));
                }
            }
        }

        return par;
    }

    public static Map<String, Object> copyMap(HttpServletRequest request, String var) {
        return copyMap(convertString(request, var));
    }

    public static Map<String, String> convertString(HttpServletRequest request, String var) {
        HashMap map = new HashMap();
        var = var.trim() + ".";
        Iterator iter = request.getParameterMap().entrySet().iterator();

        while(iter.hasNext()) {
            Entry e = (Entry)iter.next();
            String key = String.valueOf(e.getKey());
            String value = ServletRequestUtils.getStringParameter(request, key, "");
            if(StringUtils.isNotBlank(key) && StringUtils.startsWith(key, var) && !StringUtils.isBlank(value)) {
                key = StringUtils.substringAfter(key, var);
                map.put(key, value);
            }
        }

        return map;
    }

    public static Map<String,String> convertToMap(Object obj) {
        try {
            return beanUtils.describe(obj);
        } catch (IllegalAccessException var2) {
            var2.printStackTrace();
        } catch (InvocationTargetException var3) {
            var3.printStackTrace();
        } catch (NoSuchMethodException var4) {
            var4.printStackTrace();
        }

        return null;
    }

    public static Map<String, String> convertString(HttpServletRequest request) {
        HashMap map = new HashMap();
        Iterator iter = request.getParameterMap().entrySet().iterator();

        while(iter.hasNext()) {
            Entry e = (Entry)iter.next();
            String key = String.valueOf(e.getKey());
            String value = ServletRequestUtils.getStringParameter(request, key, "");
            if(!StringUtils.isBlank(value)&&!"_".equals(key)) {
                map.put(key, value);
            }
        }

        return map;
    }


    public static void copyBeanNotNull2Bean(Object databean,Object tobean,boolean isNullToStr)throws Exception
    {
        PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(databean);
        for (int i = 0; i < origDescriptors.length; i++) {
            String name = origDescriptors[i].getName();
            String type = origDescriptors[i].getPropertyType().toString() ;
            if ("class".equals(name)) {
                continue; // No point in trying to set an object's class
            }
            if (PropertyUtils.isReadable(databean, name) && PropertyUtils.isWriteable(tobean, name)) {
                try {

                    Object value = PropertyUtils.getSimpleProperty(databean, name);
                    if(value!=null){
                        beanUtils.getPropertyUtils().setSimpleProperty(tobean, name, value);
                    }else{
                        if(isNullToStr){
                            beanUtils.getPropertyUtils().setSimpleProperty(tobean, name, returnObj(type));
                        }
                    }
                }
                catch (IllegalArgumentException ie) {
                    ; // Should not happen
                }
                catch (Exception e) {
                    ; // Should not happen
                }

            }
        }
    }

    public static void copyBeanNotNull2Bean(Object databean,Object tobean)throws Exception
    {
        copyBeanNotNull2Bean(databean,tobean,false);
    }




    public static List  copyListBeanNotNull2ListBean(List databeans,String  className)throws Exception
    {
        List<Object>  reslist = new ArrayList<Object>();
        if(databeans != null){
            for(int i=0;i<databeans.size();i++){
                Class cls=Class.forName(className);
                Object obj=cls.newInstance();//创建对象
                copyBeanNotNull2Bean(databeans.get(i),  obj);
                reslist.add(obj);
            }
        }
        return reslist;
    }

    public static Object returnObj(String type){
        if (type.contains("String")) {
            return "";
        } else if (type.contains("Double")) {
            return 0;
        }  else if (type.contains("Integer")) {
            return 0;
        }
        return null;
    }

    // Map --> Bean 1: 利用Introspector,PropertyDescriptor实现 Map --> Bean
    public static void copyMapToBean(Map<String, Object> map, Object obj) {

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                logger.debug("key : {} ",key);
                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    logger.debug("value : {} ",value);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    setter.invoke(obj, value);
                }
            }
        } catch (Exception e) {
            logger.error("copyMap2Bean Error " + e);
        }

        return;
    }

    public static Map<String, Object> copyBeanNotNullToMap(Object obj) {
        return copyBean2Map(obj,true);
    }

    // Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
    public static Map<String, Object> copyBean2Map(Object obj,Boolean isFilterNull) {

        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if(isFilterNull && (null == value || StrUtils.isBlank(value.toString()))){
                        continue;
                    }
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            logger.error("copyBean2Map Error " + e);
        }
        return map;
    }
}

