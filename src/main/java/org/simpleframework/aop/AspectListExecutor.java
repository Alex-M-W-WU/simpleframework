package org.simpleframework.aop;

import lombok.Getter;
import lombok.SneakyThrows;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.simpleframework.aop.annotation.Aspect;
import org.simpleframework.aop.aspect.AspectInfo;
import org.simpleframework.util.ValidationUtil;

import java.lang.reflect.Method;
import java.util.*;

public class AspectListExecutor implements MethodInterceptor {
    //被代理的类
    private Class<?> targetClass;
    //排好序的Aspect列表
    @Getter
    private List<AspectInfo>  sortedAspectInfoList;
    public AspectListExecutor(Class<?> targetClass,List<AspectInfo> aspectInfoList){
        this.targetClass =targetClass;
        this.sortedAspectInfoList = sortAspectInfoList(aspectInfoList);
    }

    /**
     * 按照order的值进行升序排序，确保order值比较小的aspect优先被织入
     * @param aspectInfoList
     * @return
     */
    private List<AspectInfo> sortAspectInfoList(List<AspectInfo> aspectInfoList){
        Collections.sort(aspectInfoList, new Comparator<AspectInfo>() {
            @Override
            public int compare(AspectInfo o1, AspectInfo o2) {
                //升序排序
                return o1.getOrderIndex() - o2.getOrderIndex();
            }
        });
        return aspectInfoList;
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object returnValue = null;
        collectAccurateMatchedAspectList(method);//精确筛选
        if (ValidationUtil.isEmpty(sortedAspectInfoList)) {
            returnValue = methodProxy.invokeSuper(proxy, args);
            return returnValue;
        }
        //按order的顺序升序执行完所有Aspect的before方法
        invokeBeforAdvices(method,args);
        try {
            //执行被代理类的方法
            returnValue = methodProxy.invokeSuper(proxy, args);
            //如果被代理方法正常返回，则按照order的顺序降序执行完所有的Aspect的afterReturning方法
            returnValue = invokeAfterReturningAdvices(method, args, returnValue);
        }catch (Exception e){
            //如果被代理方法抛出异常，则按照order的顺序降序执行完所有Aspect的afterThrowing方法
            invokeAfterThrowingAdvices(method,args,e);
        }

        return returnValue;
    }

    private void collectAccurateMatchedAspectList(Method method) {
        if(ValidationUtil.isEmpty(sortedAspectInfoList)){
            return;
        }
        Iterator<AspectInfo> it= sortedAspectInfoList.iterator();
        while (it.hasNext()){
            AspectInfo aspectInfo = it.next();
            if(!aspectInfo.getPointcutLocator().accurateMatches(method)){
                it.remove();
            }
        }
    }

    //如果被代理方法抛出异常，则按照order的顺序降序执行完所有Aspect的afterThrowing方法
    private void invokeAfterThrowingAdvices(Method method, Object[] args, Exception e) throws Throwable {
        for (int i=sortedAspectInfoList.size()-1;i>=0;i--){
            sortedAspectInfoList.get(i).getAspectObject().afterThrowing(targetClass,method,args,e);
        }
    }

    //如果被代理方法正常返回，则按照order的顺序降序执行完所有的Aspect的afterReturning方法
    private Object invokeAfterReturningAdvices(Method method, Object[] args, Object returnValue) throws Throwable {
        Object result = null;
        for (int i=sortedAspectInfoList.size()-1;i>=0;i--){
           result = sortedAspectInfoList.get(i).getAspectObject().afterReturning(targetClass,method,args,returnValue);
        }
        return result;
    }

    //按order的顺序升序执行完所有Aspect的before方法
    @SneakyThrows
    private void invokeBeforAdvices(Method method, Object[] args) {
        for (AspectInfo aspectInfo:sortedAspectInfoList) {
            aspectInfo.getAspectObject().before(targetClass,method,args);
        }
    }
}
