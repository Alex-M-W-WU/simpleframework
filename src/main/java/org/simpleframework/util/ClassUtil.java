package org.simpleframework.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ClassUtil {
    public static final String FILE_PROTOCOL = "file";

    /**
     * 获取包下类集合
     * @param packageName 包名
     * @return 类集合
     */
    public static Set<Class<?>> extractPackageClass(String packageName){
        //1 获取类的加载器
        ClassLoader classLoader = getClassLoader();
        //2 通过类加载器获取到加载的资源
        URL url = classLoader.getResource(packageName.replace(".","/"));
        if (url == null) {
            log.warn("unable to retrieve anything from package"+packageName);
            return null;
        }

        //3 依据不同的资源类型，采用不同的方式获取资源的集合
        Set<Class<?>> classSet = null;
        if (url.getProtocol().equalsIgnoreCase(FILE_PROTOCOL)) {
            classSet = new HashSet<Class<?>>();
            File packageDirectory = new File(url.getPath());
            extractClassFile(classSet,packageDirectory,packageName);

        }
        // TODO: 2020/11/22 此处可以加入针对其他类型资源的处理
        return classSet;
    }

    /**
     * 递归获取目标package里面的所有class文件(包括子package里的class文件)
     * @param emptyClassSet 装载目标类的集合
     * @param fileSource 文件或者目录
     * @param packageName 包名
     * @return 类集合
     */
    private static void extractClassFile(Set<Class<?>> emptyClassSet, File fileSource, String packageName) {
        if(!fileSource.isDirectory()){
            return;
        }
        //如果是一个文件夹，则调用其listFiles方法获取文件夹下的文件或文件夹
        File[] files = fileSource.listFiles(new FileFilter() {
            @SneakyThrows
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()){
                    return true;
                }else {
                    //获取文件的绝对值路径
                    String absoluteFilePath = file.getAbsolutePath();
                    if(absoluteFilePath.endsWith(".class")){
                        //若是class文件，则直接加载
                        addToClassSet(absoluteFilePath);
                    }
                }
                return false;
            }
            //根据class文件的绝对值路径，获取并生成class对象，并放入classSet中
            private void addToClassSet(String absoluteFilePath) throws ClassNotFoundException {
                //1. 从class文件的绝对值路径里提取出包含了package的类名
                //如alexwu/IdeaProjects/simpleFrameWork/src/main/java/com/imooc/entity/dto
                //需要弄成com.imooc.entity.dto.MainPageInfoDTO
                absoluteFilePath = absoluteFilePath.replace(File.separator,".");
                String className = absoluteFilePath.substring(absoluteFilePath.indexOf(packageName));
                className = className.substring(0,className.lastIndexOf("."));
                //2  通过反射机制获取对应的Class对象并加入到classSet里面
                Class targetClass  = loadClass(className);
                emptyClassSet.add(targetClass);
            }
        });
        if (files != null) {
            for (File f:files) {
                //递归调用
                extractClassFile(emptyClassSet,f,packageName);
            }
        }
    }

    /**
     * 获取Class对象
     * @param className class全名 = package + 类名
     * @return Class
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        try{
            return Class.forName(className);
        }catch (ClassNotFoundException e){
            log.error("loal class error:",e);
            throw new RuntimeException(e);
        }

    }

    /**
     *
     * 获取ClassLoader
     * @return 当前ClassLoader
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    public static void main(String[] args) {
       // extractPackageClass("com.imooc.entity");
        File[] files = null;
        for (File f:files
             ) {
            System.out.println("hhhhhh");
        }
    }

    /**
     * 设置类的属性值
     *
     * @param field
     * @param target
     * @param value
     * @param accessible
     */
    public static void setField(Field field,Object target,Object value,boolean accessible){
        field.setAccessible(accessible);
        try {
            field.set(target,value);
        } catch (IllegalAccessException e) {
            log.error("setField error",e);
            throw new RuntimeException(e);
        }
    }

    /***
     * 实例化class
     * @param clazz
     * @param accessible 是否支持创建出私有class对象的实例
     * @param <T> class的类型
     * @return 类的实例化
     */
    public static <T> T newInstance(Class<?> clazz, boolean accessible) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(accessible);
            return (T)constructor.newInstance();
        } catch (Exception e) {
            log.warn("newInstance error",e);
            throw new RuntimeException(e);
        }
    }
}
