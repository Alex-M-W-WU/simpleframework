package demo.pattern.proxy.impl;

import demo.pattern.proxy.ToBPayment;

public class AliPayToB implements ToBPayment {
    ToBPayment toBPayment;
    public AliPayToB(ToBPayment toBPayment){
        this.toBPayment = toBPayment;
    }

    @Override
    public void pay() {
        beforePay();
        toBPayment.pay();
        afterPay();

    }
    private void beforePay(){
        System.out.println("从招行取款");
    }

    private void afterPay(){
        System.out.println("支付给慕课网");
    }
}
