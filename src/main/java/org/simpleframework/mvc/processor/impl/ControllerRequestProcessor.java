package org.simpleframework.mvc.processor.impl;

import lombok.extern.slf4j.Slf4j;
import org.simpleframework.core.BeanContainer;
import org.simpleframework.mvc.RequestProcessorChain;
import org.simpleframework.mvc.annotation.RequestMapping;
import org.simpleframework.mvc.annotation.RequestParam;
import org.simpleframework.mvc.annotation.ResponseBody;
import org.simpleframework.mvc.processor.RequestProcessor;
import org.simpleframework.mvc.render.JsonResultRender;
import org.simpleframework.mvc.render.ResourceNotFoundResultRender;
import org.simpleframework.mvc.render.ResultRender;
import org.simpleframework.mvc.render.ViewResultRender;
import org.simpleframework.mvc.type.ControllerMethod;
import org.simpleframework.mvc.type.RequestPathInfo;
import org.simpleframework.util.ConvertUtil;
import org.simpleframework.util.ValidationUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ControllerRequestProcessor implements RequestProcessor {
    //IOC容器
    private BeanContainer beanContainer;
    //请求和Controller方法的映射集合
    private Map<RequestPathInfo, ControllerMethod> pathInfoControllerMethodMap = new ConcurrentHashMap<>();

    /***
     * 依靠容器的能力，建立起请求路径 请求方法与Controller方法实例的映射
     */
    public ControllerRequestProcessor(){
        this.beanContainer = BeanContainer.getInstance();
        Set<Class<?>> requestMappingSet = beanContainer.getClassByAnnotation(RequestMapping.class);
        initPathControllerMethodMap(requestMappingSet);
    }

    private void initPathControllerMethodMap(Set<Class<?>> requestMappingSet) {
        if(ValidationUtil.isEmpty(requestMappingSet)){return;}
        getValueFromClassAnnotation(requestMappingSet);
    }

    /**
     * 遍历所有被@RequestMapping标记的类，获取类上面该注解的属性值作为一级路径
     * @param requestMappingSet
     */
    private void getValueFromClassAnnotation(Set<Class<?>> requestMappingSet) {
        for (Class<?> requestMappingClass:requestMappingSet) {
            RequestMapping requestMapping = requestMappingClass.getAnnotation(RequestMapping.class);
            String basePath = requestMapping.value();
            if (!basePath.startsWith("/")) {
                basePath = "/" +basePath;
            }
            getValueFromMethod(requestMappingClass, basePath);
        }
    }

    /**
     * 遍历类里所有被@RequestMapping标记的方法，获取方法上面该注解的属性值，作为二级路径
     * @param requestMappingClass
     * @param basePath
     */
    private void getValueFromMethod(Class<?> requestMappingClass, String basePath) {
        Method[] methods = requestMappingClass.getDeclaredMethods();
        if(ValidationUtil.isEmpty(methods)){
            return;
        }
        for (Method method:methods) {
            if(method.isAnnotationPresent(RequestMapping.class)){
                RequestMapping methodRequest = method.getAnnotation(RequestMapping.class);
                String methodPath = methodRequest.value();
                if(!methodPath.startsWith("/")){
                    methodPath = "/" + basePath;
                }
                String url = basePath + methodPath;
                getParamFromMethods(requestMappingClass, method, methodRequest, url);
            }
        }
    }

    /***
     *  解析方法里被@RequestParam标记的参数
     *    获取该注解的属性值，作为参数名
     *    获取被标记的参数的数据类型，建立参数名和参数类型的映射
     * @param requestMappingClass
     * @param method
     * @param methodRequest
     * @param url
     */
    private void getParamFromMethods(Class<?> requestMappingClass, Method method, RequestMapping methodRequest, String url) {
        Map<String,Class<?>> methodParams = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        if(!ValidationUtil.isEmpty(parameters)){
            for (Parameter parameter:parameters) {
                RequestParam param = parameter.getAnnotation(RequestParam.class);
                //目前暂定为Controller方法里面所有的参数都需要@RequestParam注解标记
                if (param == null) {
                    throw new RuntimeException("this parameter must have @RequestParam");
                }
                methodParams.put(param.value(),parameter.getType());
            }
        }
        getPathInfoControllerMethodMap(requestMappingClass, method, methodRequest, url, methodParams);
    }

    /**
     * 将获取到的信息封装成RequestPathInfo实例和ControllerMethod实例，放置到映射表里
     * @param requestMappingClass
     * @param method
     * @param methodRequest
     * @param url
     * @param methodParams
     */
    private void getPathInfoControllerMethodMap(Class<?> requestMappingClass, Method method, RequestMapping methodRequest, String url, Map<String, Class<?>> methodParams) {
        String httpMethod = String.valueOf(methodRequest.method());
        RequestPathInfo requestPathInfo = new RequestPathInfo(httpMethod,url);
        if (this.pathInfoControllerMethodMap.containsKey(requestPathInfo)){
            log.warn("duplicate url registration,current class{} method{} will override the former one",
                    requestMappingClass.getName(),method.getName());
        }
        ControllerMethod controllerMethod = new ControllerMethod(requestMappingClass,method,methodParams);
        this.pathInfoControllerMethodMap.put(requestPathInfo,controllerMethod);
    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        //解析HTTPServletRequest的请求方法，请求路径，获取对应的ControllerMethod实例
        String method = requestProcessorChain.getRequestMethod();
        String path = requestProcessorChain.getRequestPath();
        ControllerMethod controllerMethod = this.pathInfoControllerMethodMap.get(new RequestPathInfo(method,path));
        if (controllerMethod == null) {
            requestProcessorChain.setResultRender(new ResourceNotFoundResultRender(method,path));
            return false;
        }
        //解析请求参数，并传递给获取到ControllerMethod实例去执行
        Object result = invokeControllerMethod(controllerMethod,requestProcessorChain.getRequest());

        setResultRender(result,controllerMethod,requestProcessorChain);

        return true;
    }

    private void setResultRender(Object result, ControllerMethod controllerMethod, RequestProcessorChain requestProcessorChain) {
        if (result == null){
            return;
        }
        ResultRender resultRender;
        boolean isJson = controllerMethod.getInvokeMethod().isAnnotationPresent(ResponseBody.class);
        if (isJson){
            resultRender = new JsonResultRender(result);
        }else {
            resultRender = new ViewResultRender(result);
        }
        requestProcessorChain.setResultRender(resultRender);
    }

    private Object invokeControllerMethod(ControllerMethod controllerMethod, HttpServletRequest request) {
        //从请求里获取GET或者POST的参数名及其对应的值
        Map<String,String> requestParamMap = new HashMap<>();
        //GET POST方法的请求参数获取方式
        Map<String,String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String,String[]> parameter:parameterMap.entrySet()) {
            if(ValidationUtil.isEmpty(parameter.getValue())){
                //只支持一个参数对应一个值的形式
                requestParamMap.put(parameter.getKey(),parameter.getValue()[0]);
            }
        }
        //根据获取到的请求参数名及其对应的值，以及controllerMethod里面的参数和类型的映射关系，去实例化出方法
        List<Object> methodParams = new ArrayList<>();
        Map<String,Class<?>> methodParamMap = controllerMethod.getMethodParameters();
        for (String paramName:methodParamMap.keySet()) {
            Class<?> type = methodParamMap.get(paramName);
            String requestValue = requestParamMap.get(paramName);
            Object value;
            if (requestValue == null) {
                value = ConvertUtil.primitiveNull(type);
            }else{
                value = ConvertUtil.convert(type,requestValue);
            }
            methodParams.add(value);
        }
        //执行Controller里面对应的方法并返回结果
        Object controller =  beanContainer.getBean(controllerMethod.getControllerClass());
        Method invokeMethod = controllerMethod.getInvokeMethod();
        invokeMethod.setAccessible(true);
        Object result = null;
        try {
            if(methodParams.size() == 0){
                result = invokeMethod.invoke(controller);
            }else{
                result = invokeMethod.invoke(controller,methodParams.toArray());
            }
        }catch (InvocationTargetException e){
            throw new RuntimeException(e.getTargetException());
        }catch (IllegalAccessException e){
            throw new RuntimeException(e);
        }
        return result;
    }
}
