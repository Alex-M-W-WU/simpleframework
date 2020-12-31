package demo.generic;

import java.util.LinkedList;
import java.util.List;

public class GenericDemo {
    //? 表示支持 在泛型里面使用具备继承关系的类
    // ? extends E 泛型上边界 E表示父类 比较大的 ？表示子类
    // ? super E 泛型下边界 E表示子类 ?是父类
    public static void handleMember(GenericClassExample<? super Integer> integerGenericClassExample){
        Integer result = 111 + (Integer) integerGenericClassExample.getMember();
        System.out.println("result is " + result);
    }

    public static void main(String[] args) {
//        List<String> linkedList = new LinkedList(); 定义具体的String类型  对于int就会在编译时报错 提前发现问题
//        linkedList.add("words");
//        //linkedList.add(1);
//        for (int i = 0; i < linkedList.size() ; i++) {
//          String item = linkedList.get(i);
//            System.out.println(item);
//        }
        GenericClassExample<String> stringExample = new GenericClassExample<>("abc");
        GenericClassExample<Number> integerGenericClassExample = new GenericClassExample<Number>(123);
        /*System.out.println(stringExample.getMember().getClass());
        System.out.println(integerGenericClassExample.getMember().getClass());*/
        System.out.println(stringExample.getClass());
        System.out.println(integerGenericClassExample.getClass());
        handleMember(integerGenericClassExample);
        Integer[] integers = {1,2,3,4,5,6};
        Double [] doubles ={1.1,1.2,1.3};
        Character[] characters = {'A','B','C'};
        stringExample.printArray(integers);
        stringExample.printArray(doubles);
        stringExample.printArray(characters);
    }
}
