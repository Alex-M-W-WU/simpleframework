package demo.pattern.factory.method;

import demo.pattern.factory.entity.HpMouse;
import demo.pattern.factory.entity.Mouse;

public class factoryMethodDemo {
    public static void main(String[] args) {
        MouseFactory mouseFactory = new HpMouseFactory();
        Mouse mouse = mouseFactory.createMouse();
        mouse.sayHi();
    }
}
