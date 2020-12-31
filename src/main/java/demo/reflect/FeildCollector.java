package demo.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 获取成员变量并使用
 *
 * 1.批量的
 *  1）Field[] getFields() 获取所有的"公有字段"
 *  2）Field[] getDeclaredFields() 获取所有的字段，包括：私有、受保护、默认、公有
 * 2.获取单个的
 *  1）public Field getField(String fieldName) 获取某个"公有的"字段
 *  2）public Field getDeclaredField(String fieldName) 获取某个字段（可以是私有的）
 *
 *  设置字段的值
 *   Filed---->public  void set(Object obj,Object value)
 *        参数说明
 *         1.obj 要设置的字段所在的对象
 *         2.value 要为字段设置的值
 */

public class FeildCollector {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //获取CLass对象
        Class reflectTargetClass = Class.forName("demo.reflect.ReflectTarget");
        //1 获取所有的公有字段
        System.out.println("******获取所有公有的字段********");
        Field[] fields =reflectTargetClass.getFields();
        for (Field f:fields) {
            System.out.println(f);
        }
        //2 获取所有的字段
        System.out.println("=====获取所有的字段======");
        fields = reflectTargetClass.getDeclaredFields();
        for (Field f:fields
             ) {
            System.out.println(f);
        }
        //3获取单个特定公有的field
        System.out.println("====获取公有字段并使用=====");
        Field f = reflectTargetClass.getField("name");
        System.out.println("公有的field name:" +f);
        ReflectTarget reflectTarget = (ReflectTarget) reflectTargetClass.getConstructor().newInstance();
        //4 给获取到的field赋值
        f.set(reflectTarget,"待反射一号");
        //5 验证对应的值name
        System.out.println("验证name："+reflectTarget.name);
        //获取单个私有的field
        System.out.println("********获取私有字段targetInfo并调用**********");
        f = reflectTargetClass.getDeclaredField("targetInfo");
        System.out.println(f);
        f.setAccessible(true);
        f.set(reflectTarget,"89898989898");
        System.out.println("验证信息"+reflectTarget);
    }
}
