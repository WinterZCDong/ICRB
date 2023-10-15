package yzu;
import it.unisa.dia.gas.jpbc.*;

import java.math.BigInteger;

public class Transaction implements Comparable<Transaction>{
    public Element senderAddr;      // 发送者地址
    public Element receiverAddr;    // 接收者地址
    public int num;                 // 交易数量
    public ECDSASig sig0;           // 正常签名
    public boolean isRedacted;      // 是否被修改过
    public boolean isRemedyTransaction; // 是否是补偿交易
    public BigInteger tid;              // 交易编号
    public ECDSASig remedysig;          // 如果是补偿交易，这一项需要非空
    // 补偿交易附带的信息
    public BigInteger originaltid;           // 原被修改交易的id
    public Element userAddr;                 // 如果是补偿交易需要有用户的地址
    public Transaction(){
    }

    public Transaction(Element senderAddr, Element receiverAddr, int num, ECDSASig sig0){
        this.senderAddr = senderAddr;
        this.receiverAddr = receiverAddr;
        this.num = num;
        this.sig0 = sig0;
        this.tid = Util.Hash(senderAddr.toString()+receiverAddr.toString()+num);
        this.isRedacted = false;
        this.isRemedyTransaction = false;
    }

    @Override
    public int compareTo(Transaction t){
        return t.tid.subtract(this.tid).compareTo(BigInteger.ZERO);
    }
}
