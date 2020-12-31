package org.simpleframework.mvc;

import com.imooc.controller.frontend.MainPageController;
import com.imooc.controller.superadmin.HeadLineOperationController;
import org.simpleframework.aop.AspectWeaver;
import org.simpleframework.core.BeanContainer;
import org.simpleframework.inject.DependencyInjector;
import org.simpleframework.mvc.processor.RequestProcessor;
import org.simpleframework.mvc.processor.impl.ControllerRequestProcessor;
import org.simpleframework.mvc.processor.impl.JspRequestProcessor;
import org.simpleframework.mvc.processor.impl.PreRequestProcessor;
import org.simpleframework.mvc.processor.impl.StaticResourceRequestProcessor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    List<RequestProcessor> PROCESSOR = new ArrayList<>();
    @Override
    public void init(){
        //初始化容器
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("com.imooc");
        new AspectWeaver().doAop();
        new DependencyInjector().doIoC();
        //初始化请求处理器责任链
        PROCESSOR.add(new PreRequestProcessor());
        PROCESSOR.add(new StaticResourceRequestProcessor(getServletContext()));
        PROCESSOR.add(new JspRequestProcessor(getServletContext()));
        PROCESSOR.add(new ControllerRequestProcessor());
        //System.out.println("首次请求被处理前执行的，后续不会再执行");
    }

    @Override
    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        //创建责任链对象实例
        RequestProcessorChain requestProcessorChain = new RequestProcessorChain(PROCESSOR.iterator(),httpServletRequest,httpServletResponse);
        //通过责任链模式来依次调用请求处理器对请求进行处理
        requestProcessorChain.doRequestProcessorChain();
        //对处理结果进行渲染
        requestProcessorChain.doRender();
       /* System.out.println("request path is:"+httpServletRequest.getServletPath());
        System.out.println("request method is:"+httpServletRequest.getMethod());
        if(httpServletRequest.getServletPath() =="/frontend/getmainpageinfo" && httpServletRequest.getMethod() =="GET"){
            new MainPageController().getMainPageInfo(httpServletRequest,httpServletResponse);
        }else if(httpServletRequest.getServletPath() =="/superadmin/addheadline" && httpServletRequest.getMethod() =="POST"){
            new HeadLineOperationController().addHeadLine(httpServletRequest,httpServletResponse);
        }*/
    }
}
