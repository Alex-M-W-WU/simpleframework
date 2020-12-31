package com.imooc.controller.superadmin;

import com.imooc.entity.bo.HeadLine;
import com.imooc.entity.dto.Result;
import com.imooc.service.solo.HeadLineService;
import org.simpleframework.core.annotation.Controller;
import org.simpleframework.inject.annotation.Autowired;
import org.simpleframework.mvc.annotation.RequestMapping;
import org.simpleframework.mvc.annotation.RequestParam;
import org.simpleframework.mvc.annotation.ResponseBody;
import org.simpleframework.mvc.type.ModelAndView;
import org.simpleframework.mvc.type.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/headline")
public class HeadLineOperationController {
    @Autowired(value = "HeadLineServiceImpl")
    private HeadLineService headLineService;
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ModelAndView addHeadLine(@RequestParam("lineName") String lineName){
        //todo 参数检验以及请求参数转化
       HeadLine headLine = new HeadLine();
       headLine.setLineName(lineName);
       Result<Boolean> result = headLineService.addHeadLine(headLine);
       ModelAndView modelAndView = new ModelAndView();
       modelAndView.setView("addheadline.jsp").addViewData("result",result);
       return modelAndView;
    }

    @RequestMapping(value = "/remove",method = RequestMethod.GET)
    public /*Result<Boolean>*/ void removeHeadLine(){
        //todo 参数检验以及请求参数转化
        System.out.println("llalalallall");
        //return headLineService.removeHeadLine(1);
    }
    public Result<Boolean> modifyHeadLine(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        //todo 参数检验以及请求参数转化
        return headLineService.modifyHeadLine(new HeadLine());
    }
    public Result<HeadLine> queryHeadLineById(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        //todo 参数检验以及请求参数转化
        return headLineService.queryHeadLineById(1);
    }

    @RequestMapping(value = "/query",method = RequestMethod.GET)
    @ResponseBody
    public Result<List<HeadLine>> queryHeadLine(){
        //todo 参数检验以及请求参数转化
        return headLineService.queryHeadLine(null, 1, 100);
    }

}
