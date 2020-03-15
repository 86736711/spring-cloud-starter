package com.npf.cloud.framework.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.framework.util
 * @ClassName: JSONObj
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/15 16:38
 * @Version: 1.0
 */
public class JSONObj extends JSONObject {

    private static final long serialVersionUID = 1L;

    private JSONObject jsonObject;

    public JSONObj(String json) {
        this.jsonObject = JSON.parseObject(json);
    }
}
