package org.simpleframework.mvc.render;

import org.simpleframework.mvc.RequestProcessorChain;
import org.simpleframework.mvc.type.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ViewResultRender implements ResultRender{
    private ModelAndView modelAndView;

    /***
     * 处理入参，并赋值给ModelAndView成员变量
     * @param mv
     */
    public ViewResultRender(Object mv) {
        if (mv instanceof ModelAndView){
            this.modelAndView = (ModelAndView) mv;
        }else if (mv instanceof String){
            this.modelAndView = new ModelAndView().setView((String) mv);
        }else{
            throw new RuntimeException("illegal request result type");
        }
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        HttpServletRequest httpServletRequest = requestProcessorChain.getRequest();
        HttpServletResponse httpServletResponse = requestProcessorChain.getResponse();
        String path = modelAndView.getView();
        Map<String,Object> model = modelAndView.getModel();
        for (Map.Entry<String,Object> entry:model.entrySet()) {
            httpServletRequest.setAttribute(entry.getKey(),entry.getValue());
        }
        //JSP
        httpServletRequest.getRequestDispatcher("/templates/" +path).forward(httpServletRequest,httpServletResponse);
    }
}
