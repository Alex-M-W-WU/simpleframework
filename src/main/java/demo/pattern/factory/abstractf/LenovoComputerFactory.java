package demo.pattern.factory.abstractf;

import demo.pattern.factory.entity.*;

public class LenovoComputerFactory implements ComputerFactory{
    @Override
    public Mouse createMouse() {
        return new LenovoMouse();
    }

    @Override
    public KeyBoard creatKeyboard() {
        return new LevonoKeyboard();
    }
}

