package com.npf.cloud.framework.exception;

import com.npf.cloud.framework.common.model.MessageSourceBO;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.framework.exception
 * @ClassName: ApplicationMessageException
 * @Author: ningpf
 * @Description: 这个是业务的作为阻断线程的执行返回的异常，不包含异常信息。只包含业务中返回的业务异常，用于国际化的异常处理
 * @Date: 2020/3/11 14:57
 * @Version: 1.0
 */
public class ApplicationMessageException extends RuntimeException {

    private static final long serialVersionUID = -4832597104421398005L;

    private MessageSourceBO messageSourceBO;

    public ApplicationMessageException(String message) {
        super(message);
    }

    public ApplicationMessageException(MessageSourceBO messageSourceBO, Throwable cause) {
        super(messageSourceBO.getMessCode(), cause);
        this.messageSourceBO = messageSourceBO;
    }

    public ApplicationMessageException(MessageSourceBO messageSourceBO) {
        super(messageSourceBO.getMessCode());
        this.messageSourceBO = messageSourceBO;
    }

    public MessageSourceBO getMessageSourceBO() {
        return messageSourceBO;
    }

    public void setMessageSourceBO(MessageSourceBO messageSourceBO) {
        this.messageSourceBO = messageSourceBO;
    }
}
