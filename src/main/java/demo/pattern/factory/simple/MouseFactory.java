package demo.pattern.factory.simple;

import demo.pattern.factory.entity.*;

public class MouseFactory {
    public static Mouse createMouse(int type){
        switch (type){
            case 0:return new DellMouse();
            case 1:return new HpMouse();
            case 2:return new LenovoMouse();
            default:return new HpMouse();
        }
    }

    public static void main(String[] args) {
      Mouse mouse = MouseFactory.createMouse(1);
      mouse.sayHi();
    }
}
