package com.npf.cloud.framework.common;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.framework.common
 * @ClassName: AbleSort
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/11 13:05
 * @Version: 1.0
 */
public interface AbleSort extends Comparable<AbleSort> {

    /**
     * 这里定义的返回数值代表了执行的顺序，数越小越靠前执行
     * @return serialNumber
     */
    int getExeSerialNumber();

    //继承Comparable接口，这个接口就是可由比较的能力，其实和自己能以api的行为是一样的
    default int compareTo(AbleSort o){

        if(getExeSerialNumber() >= o.getExeSerialNumber()){

            return 1;
        }else{
            return -1;
        }

    }
}
