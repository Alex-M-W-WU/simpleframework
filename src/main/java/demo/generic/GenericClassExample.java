package demo.generic;

import lombok.Data;

@Data
public class GenericClassExample<T> {
    //member这个成员变量的类型为T T的类型由外部指定
    private T member;
    //泛型构造方法形参member的类型为T T的类型也是由外部指定
    public GenericClassExample(T member) {
        this.member = member;
    }

    public T handleSomething(T target){
        return target;
    }

    public String sayHello(String name){
        return "hello" + name;
    }

    public static <E> void printArray(E[] inputArray){
        for (E element:inputArray) {
            System.out.printf("%s",element);
            System.out.printf(" ");
        }
        System.out.println();
    }
}
