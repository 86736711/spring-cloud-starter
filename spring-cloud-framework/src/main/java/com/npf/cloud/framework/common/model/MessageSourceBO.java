package com.npf.cloud.framework.common.model;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.framework.common.model
 * @ClassName: MessageSourceBO
 * @Author: ningpf
 * @Description: 国际化中所要转换的key和参数
 * @Date: 2020/3/11 14:25
 * @Version: 1.0
 */
public class MessageSourceBO {

    public String messCode;

    public Object[] args;

    public MessageSourceBO(){}

    public MessageSourceBO(String messCode,Object... args){

        this.messCode = messCode;

        this.args = args;
    }

    public String getMessCode() {
        return messCode;
    }

    public void setMessCode(String messCode) {
        this.messCode = messCode;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
