package org.simpleframework.mvc.render;

import org.simpleframework.mvc.RequestProcessorChain;

public interface ResultRender {
    //执行渲染
    void render(RequestProcessorChain requestProcessorChain) throws Exception;
}
