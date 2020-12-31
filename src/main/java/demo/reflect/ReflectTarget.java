package demo.reflect;

public class ReflectTarget extends ReflectTargetOrigin{

   // 构造函数
   // 默认的带参数的构造函数
    //仅包可见
    ReflectTarget(String str){
        System.out.println("(默认)的构造方法s =" +str);
    }
    // 无参构造函数
    public ReflectTarget(){
        System.out.println("调用了公有的无参构造方法。。。");
    }
    //有一个参数的构造函数
    public ReflectTarget(char name){
        System.out.println("调用了带有一个参数的构造方法，参数值为"+name);
    }
    //有多个参数的构造函数
    public ReflectTarget(String name,int index){
        System.out.println("调用了带有多个参数的构造方法，参数值为【目标码】："+name+"【序号】"+index);
    }
    ///受保护的构造函数
    protected ReflectTarget(boolean n){
        System.out.println("受保护的构造方法n:"+n);
    }
    //私有的构造函数
    private ReflectTarget(int index){
        System.out.println("私有的构造方法 序号："+index);
    }

    /**********字段*************/
    public String name;
    protected int index;
    char type;
    private String targetInfo;
    @Override
    public String toString(){
        return "ReflectTarget[name="+name+",index="+index+",type="+type+",targetInfo="+targetInfo+"]";
    }

    //***********成员方法***************//


    public void show1(String s) {
        System.out.println("调用了公有的，string参数的show1():s="+s);
    }
    protected void show2() {
        System.out.println("调用了受保护的，无参数的show2()");
    }
    void show3() {
        System.out.println("调用了默认的，无参的show3()");
    }
    private String show4(int index) {
        System.out.println("调用了私有的，并且有返回值的，int参数的show4():index="+index);
        return "show4result";
    }



    public static void main(String[] args) throws ClassNotFoundException {
        //第一种方式获取Class对象
        ReflectTarget reflectTarget = new ReflectTarget();
        Class re1 = reflectTarget.getClass();
        System.out.println("11"+re1.getName());

        //第二种方式获取Class对象
        Class re2 = ReflectTarget.class;
        System.out.println("22"+re2.getName());
        //判断第一种方式获取的class对象和第二种方式获取的是否是同一个  == 表示比较的是对象的内存地址
        System.out.println(re1 == re2);

        //第三种方式获取Class对象
        Class re3 = Class.forName("demo.reflect.ReflectTarget");
        System.out.println("33"+re3.getName());
        System.out.println(re2 == re3);
    }
}
