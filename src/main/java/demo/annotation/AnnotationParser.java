package demo.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AnnotationParser {
    //解析类的注解
    public static void  parseTypeAnnotation() throws ClassNotFoundException {
        Class clazz = Class.forName("demo.annotation.ImoocCourse");
        //这里获取的是class对象的注解，而不是其里面的方法和成员变量的注解
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation:annotations) {
         CourseInfoAnnotation  courseInfoAnnotation = (CourseInfoAnnotation) annotation;
            System.out.println("课程名："+courseInfoAnnotation.courseName()+"\n"+
                    "课程标签："+courseInfoAnnotation.courseIndex());
        }
    }
    //解析成员变量上的标签
    public static void parseFieldAnnotation() throws ClassNotFoundException{
        Class clazz = Class.forName("demo.annotation.ImoocCourse");
        Field[] fields = clazz.getDeclaredFields();
        for (Field f:fields
             ) {
            //判断成员变量中是否有指定注解类型的注解
          boolean hasAnnotation =  f.isAnnotationPresent(PersonInfoAnnotation.class);
          if (hasAnnotation){
              PersonInfoAnnotation personInfoAnnotation = f.getAnnotation(PersonInfoAnnotation.class);
              System.out.println(personInfoAnnotation.name()+personInfoAnnotation.gender());
          }
        }
    }
    //解析方法注解
    public static void parseMethodAnnotation() throws ClassNotFoundException{
        Class clazz = Class.forName("demo.annotation.ImoocCourse");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m:methods
             ) {
           boolean hasAnnotation = m.isAnnotationPresent(CourseInfoAnnotation.class);
           if(hasAnnotation){
               CourseInfoAnnotation courseInfoAnnotation = m.getAnnotation(CourseInfoAnnotation.class);
               System.out.println(courseInfoAnnotation.courseName());
           }

        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
       //parseTypeAnnotation();
        parseFieldAnnotation();
       // parseMethodAnnotation();
    }
}
