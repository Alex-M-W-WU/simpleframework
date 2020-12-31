package org.simpleframework.mvc.type;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    @Getter
    private String view;
    @Getter
    private Map<String,Object> model = new HashMap<>();

    public ModelAndView setView(String view){
        this.view = view;
        return this;
    }

    //modelAndView.setView("addheadline.jsp").addViewData("aaa","bbb");
    public ModelAndView addViewData(String attributeName,Object attributeValue){
        model.put(attributeName,attributeValue);
        return this;
    }
}
