package org.simpleframework.inject;

import com.imooc.controller.frontend.MainPageController;
import com.imooc.service.combine.HeadLineShopCategoryCombineService;
import com.imooc.service.combine.impl.HeadLineShopCategoryCombineServiceImpl;
import com.imooc.service.combine.impl.HeadLineShopCategoryCombineServiceImpl2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.simpleframework.core.BeanContainer;

public class DependencyInjectorTest {
    @DisplayName("依赖注入doIoC")
    @Test
    public void doIoCTest(){
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("com.imooc");
        Assertions.assertEquals(true,beanContainer.isLoaded());
        MainPageController controller = (MainPageController) beanContainer.getBean(MainPageController.class);
        Assertions.assertEquals(true,controller instanceof MainPageController);
        Assertions.assertEquals(null,controller.getHeadLineShopCategoryCombineService());
        new DependencyInjector().doIoC();
        Assertions.assertNotEquals(null,controller.getHeadLineShopCategoryCombineService());
        Assertions.assertEquals(true,controller.getHeadLineShopCategoryCombineService() instanceof HeadLineShopCategoryCombineServiceImpl);
        Assertions.assertEquals(false,controller.getHeadLineShopCategoryCombineService() instanceof HeadLineShopCategoryCombineServiceImpl2);
    }
}
