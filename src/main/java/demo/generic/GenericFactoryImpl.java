package demo.generic;

//T N 不能修改成其他类型
public class GenericFactoryImpl<T,N> implements GenericIFactory<T,N> {
    @Override
    public T nextObject() {
        return null;
    }

    @Override
    public N nextNumber() {
        return null;
    }
}
