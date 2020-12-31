package demo.pattern.factory.method;

import demo.pattern.factory.entity.HpMouse;
import demo.pattern.factory.entity.LenovoMouse;
import demo.pattern.factory.entity.Mouse;

public abstract class LenovoMouceFactory implements MouseFactory{
    @Override
    public Mouse createMouse() {
        return new LenovoMouse();
    }

    public abstract Mouse createMouce();
}
