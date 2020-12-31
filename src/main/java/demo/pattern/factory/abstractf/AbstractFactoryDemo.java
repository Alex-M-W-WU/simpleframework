package demo.pattern.factory.abstractf;

import demo.pattern.factory.entity.KeyBoard;
import demo.pattern.factory.entity.Mouse;

import java.security.Key;

public class AbstractFactoryDemo {
    public static void main(String[] args) {
        ComputerFactory computerFactory = new HpComputerFactory();
        Mouse mouse = computerFactory.createMouse();
        KeyBoard keyBoard = computerFactory.creatKeyboard();
        mouse.sayHi();
        keyBoard.sayHello();
    }
}
