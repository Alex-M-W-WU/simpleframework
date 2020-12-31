package demo.pattern.factory.method;

import demo.pattern.factory.entity.IMBMouse;
import demo.pattern.factory.entity.Mouse;

public class IBMMouseFactory extends LenovoMouceFactory {
    @Override
    public Mouse createMouce(){
        return new IMBMouse();
    }
}
