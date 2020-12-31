package org.simpleframework.mvc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.mvc.processor.RequestProcessor;
import org.simpleframework.mvc.render.DefaultResultRender;
import org.simpleframework.mvc.render.InternalErrorResultRender;
import org.simpleframework.mvc.render.ResultRender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

@Data
@Slf4j
public class RequestProcessorChain {
    //请求处理器迭代器
    private Iterator<RequestProcessor> requestProcessorIterator;
    //请求request
    private HttpServletRequest request;
    //请求response
    private HttpServletResponse response;
    //http请求方法
    private String requestMethod;

    private String requestPath;

    private int responseCode;

    private ResultRender resultRender;

    public RequestProcessorChain(Iterator<RequestProcessor> iterator, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.requestProcessorIterator = iterator;
        this.request = httpServletRequest;
        this.response = httpServletResponse;
        this.requestMethod = httpServletRequest.getMethod();
        this.requestPath = httpServletRequest.getServletPath();
        this.responseCode = httpServletResponse.SC_OK;
    }

    /**
     * 以责任链模式执行请求链
     */
    public void doRequestProcessorChain() {
        //通过迭代器遍历注册的请求处理器实现类列表
        while (requestProcessorIterator.hasNext()){
            try {//直到某个请求处理器执行后返回为false为止
                if (!requestProcessorIterator.next().process(this)) {
                    break;
                }
            } catch (Exception e) {  //期间如果出现异常，则交由内部异常渲染器处理
                this.resultRender = new InternalErrorResultRender(e.getMessage());
                log.error("e",e);
            }
        }
    }

    public void doRender() {
        if (this.resultRender == null) {
            this.resultRender = new DefaultResultRender();
        }
        try {
            this.resultRender.render(this);
        } catch (Exception e) {
            log.error("error",e);
            throw new RuntimeException(e);
        }
    }
}
