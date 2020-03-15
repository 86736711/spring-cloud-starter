package com.npf.cloud.framework.util;

import java.util.Random;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.framework.util
 * @ClassName: StringUtils
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/13 11:10
 * @Version: 1.0
 */
public class StringUtils {


    /**
     * 功能描述：过滤掉$符
     *
     * @param str 替换前的字符串
     * @return 替换后的字符串
     */
    public static String filterDollarStr(String str) {
        String filtered = "";
        if (!org.apache.commons.lang3.StringUtils.trim(str).equals("")) {
            if (str.indexOf('$', 0) > -1) {
                while (str.length() > 0) {
                    if (str.indexOf('$', 0) > -1) {
                        filtered += str.subSequence(0, str.indexOf('$', 0));
                        filtered += "\\$";
                        str = str.substring(str.indexOf('$', 0) + 1, str.length());
                    } else {
                        filtered += str;
                        str = "";
                    }
                }
            } else {
                filtered = str;
            }
        }
        return filtered;
    }


    /**
     * 生成指定长度的字符串
     *
     * @param length 长度
     */
    public static String genRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
