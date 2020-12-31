package com.imooc.controller.superadmin;

import com.imooc.entity.bo.ShopCategory;
import com.imooc.entity.dto.Result;
import com.imooc.service.solo.ShopCategoryService;
import org.simpleframework.core.annotation.Controller;
import org.simpleframework.inject.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
@Controller
public class ShopCategoryOperationController {
    @Autowired
    private ShopCategoryService shopCategoryService;
    public Result<Boolean> addShopCategory(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        //todo 参数检验以及请求参数转化
        return shopCategoryService.addShopCategory(new ShopCategory());
    }
    public Result<Boolean> removeShopCategory(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        //todo 参数检验以及请求参数转化
        return shopCategoryService.removeShopCategory(1);
    }
    public Result<Boolean> modifyShopCategory(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        //todo 参数检验以及请求参数转化
        return shopCategoryService.modifyShopCategory(new ShopCategory());
    }
    public Result<ShopCategory> queryShopCategoryById(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        //todo 参数检验以及请求参数转化
        return shopCategoryService.queryShopCategoryById(1);
    }
    public Result<List<ShopCategory>> queryShopCategory(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        //todo 参数检验以及请求参数转化
        return shopCategoryService.queryShopCategory(null, 1, 100);
    }

}
