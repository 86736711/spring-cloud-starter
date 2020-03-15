package com.npf.cloud.framework.util;

import com.alibaba.fastjson.JSON;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.framework.util
 * @ClassName: JSONUtil
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/15 16:36
 * @Version: 1.0
 */
public class JSONUtil {

    public static String toStr(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * 将JSON字符串转为对象
     *
     * @param json
     *            json字符串
     * @param clazz
     *            要造型成的Class对象T
     *
     * @return T 对象
     */
    public static <T> T toObj(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 将JSON字符串转化为JSON对象
     *
     * @param json
     * @return JSONObj
     */
    public static JSONObj toJSONObj(String json) {
        return new JSONObj(json);
    }
}
