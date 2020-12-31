package demo.pattern.factory.abstractf;

import demo.pattern.factory.entity.*;

public class HpComputerFactory implements ComputerFactory{
    @Override
    public Mouse createMouse() {
        return new HpMouse();
    }

    @Override
    public KeyBoard creatKeyboard() {
        return new HpKeyboard();
    }
}

