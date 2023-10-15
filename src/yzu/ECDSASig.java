package yzu;

import java.math.BigInteger;
import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
// 椭圆曲线签名对
public class ECDSASig {
    public BigInteger r;    // 随机数
    public BigInteger s;    // 签名数据
    public ECDSASig(BigInteger r, BigInteger s){
        this.r = r;
        this.s = s;
    }

    @Override
    public String toString() {
        return "ECDSASig{" +
                "r=" + r +
                ", s=" + s +
                '}';
    }
}
