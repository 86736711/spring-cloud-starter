package com.npf.cloud.framework.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.framework.context
 * @ClassName: RequestContext
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/11 13:15
 * @Version: 1.0
 */
public class RequestContext {

    private HttpServletRequest request;

    private HttpServletResponse response;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}
