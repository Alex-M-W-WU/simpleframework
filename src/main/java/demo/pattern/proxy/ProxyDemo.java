package demo.pattern.proxy;

import demo.pattern.proxy.cglib.AlipayMethodInterceptor;
import demo.pattern.proxy.cglib.CglibUtil;
import demo.pattern.proxy.impl.*;
import demo.pattern.proxy.jdkproxy.AliPayInvocationHandler;
import demo.pattern.proxy.jdkproxy.JdkDynamicProxyUtil;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationHandler;

public class ProxyDemo {
    public static void main(String[] args) {
       /* ToCPayment toCProxy = new AliPayToC(new ToCPaymentImpl());
        toCProxy.pay();
        ToBPayment toBProxy = new AliPayToB(new ToBPaymentImpl());
        toBProxy.pay();*/
       /* ToCPayment toCPayment = new ToCPaymentImpl();
        InvocationHandler handler = new AliPayInvocationHandler(toCPayment);//切面类实例
        ToCPayment toCProxy = JdkDynamicProxyUtil.newProxyInstance(toCPayment,handler);//动态代理对象
        toCProxy.pay();*/
        /*ToBPayment toBPayment = new ToBPaymentImpl();
        InvocationHandler handler1 = new AliPayInvocationHandler(toBPayment);
        ToBPayment toBProxy = JdkDynamicProxyUtil.newProxyInstance(toBPayment,handler1);
        toBProxy.pay();*/

        CommonPayment commonPayment = new CommonPayment();
       // AliPayInvocationHandler aliPayInvocationHandler = new AliPayInvocationHandler(commonPayment);
        //CommonPayment commonPaymentProxy = JdkDynamicProxyUtil.newProxyInstance(commonPayment,aliPayInvocationHandler);
        MethodInterceptor methodInterceptor = new AlipayMethodInterceptor();
        CommonPayment commonPaymentProxy = CglibUtil.createProxy(commonPayment,methodInterceptor);
        commonPaymentProxy.pay();

        ToCPayment toCPayment = new ToCPaymentImpl();
        ToCPayment toCProxy = CglibUtil.createProxy(toCPayment,methodInterceptor);
        toCProxy.pay();
    }
}
