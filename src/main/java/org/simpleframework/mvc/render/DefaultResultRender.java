package org.simpleframework.mvc.render;

import org.simpleframework.mvc.RequestProcessorChain;

public class DefaultResultRender implements ResultRender{
    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        requestProcessorChain.getResponse().setStatus(requestProcessorChain.getResponseCode());
    }
}
