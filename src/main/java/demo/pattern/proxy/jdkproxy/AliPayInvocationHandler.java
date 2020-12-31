package demo.pattern.proxy.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AliPayInvocationHandler implements InvocationHandler {
    private Object targetObject;//被代理的对象 目标类
    public AliPayInvocationHandler(Object o){
        this.targetObject = o;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        beforePay();
        Object result = method.invoke(targetObject,args);
        afterPay();
        return result;
    }
    private void beforePay(){
        System.out.println("从招行取款");
    }

    private void afterPay(){
        System.out.println("支付给慕课网");
    }
}
