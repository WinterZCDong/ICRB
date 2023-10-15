package yzu;
import it.unisa.dia.gas.jpbc.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Util {
    public static Element g;            // ECDSA的生成元
    public static BigInteger q;         // G1的order
    public static BigInteger Hash(String message){
        BigInteger hash = BigInteger.ZERO;
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashbyte = md.digest(message.getBytes());
            hash = new BigInteger(1,hashbyte);
        }catch (Exception e){
            e.printStackTrace();
        }
        return hash;
    }

    // 交易的验证，先验证hash,再验证签名，最后验证补偿签名
    public boolean validateTransaction(Transaction t){
        // 验证hash
        BigInteger hash = Util.Hash(t.senderAddr.toString()+ t.receiverAddr.toString()+t.num);
        if(!hash.equals(t.tid)) return false;
        // 验证签名
        if(!ECDSA.verifyTransaction(hash.toString(),t.sig0,Util.g,t.senderAddr,Util.q)){
            return false;
        }
        // 如果是补偿交易，还要验证第二个签名
        if(t.isRemedyTransaction){
            if(!ECDSA.verifyTransaction(hash.toString(),t.remedysig,Util.g,t.senderAddr,Util.q)){
                return false;
            }
        }
        return true;
    }

    // 计算默克尔树
    public static BigInteger getMerkleTree(List<Transaction> arr){
        Collections.sort(arr);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < arr.size(); i++) sb.append(arr.get(i).tid.toString());
        BigInteger tmp = Util.Hash(sb.toString());
        return tmp;
    }

    // 验证区块,需要加上验证交易的tid
    public static boolean validateBlock(Block b){
        long tbegin, tend;
        tbegin = System.nanoTime();
        if(!Util.getMerkleTree(b.transactions).equals(b.oldmerkletree)) {
            return false;
        }
        ECDSA ecdsa = new ECDSA();
        ecdsa.setG(g);
        for(int i = 0; i < b.transactions.size(); i++){
            Transaction t = b.transactions.get(i);
            String message = t.senderAddr.toString()+ t.receiverAddr.toString()+t.num;
            if(!ecdsa.verifyTransaction(message,t.sig0,g,t.senderAddr,q)) return false;
        }
        tend = System.nanoTime();
        System.out.println((tend-tbegin)*1.0/1000000);
        return true;
    }
    // 验证被修改的区块
    public static boolean validateRedactedBlock(Block b, List<BigInteger> oldhashs){
        long tbegin, tend;
        tbegin = System.nanoTime();
        ECDSA ecdsa = new ECDSA();
        ecdsa.setG(g);
        List<Transaction> list = new ArrayList<Transaction>();
        for(int i = 0; i < b.transactions.size(); i++){
            Transaction t = b.transactions.get(i);
            String message = t.senderAddr.toString()+ t.receiverAddr.toString()+t.num;
            if(!ecdsa.verifyTransaction(message,t.sig0,g,t.senderAddr,q)) {
                System.out.println("A");
                return false;
            }
            if(t.isRemedyTransaction){
                Transaction tmp = new Transaction();
                tmp.tid = t.originaltid;
                list.add(tmp);
                if(!ecdsa.verifyTransaction(message,t.remedysig,g,t.userAddr,q)) {
                    System.out.println("B");
                    return false;
                }
            }
            else if(!t.isRedacted) list.add(t);
        }
        // 模拟遍历200个区块
        for(int i = 0; i < 200; i++){
            for(int j = 0; j < b.transactions.size(); j++){
                Transaction t = b.transactions.get(j);
                if(t.isRedacted){
                    // 什么也不做
                    int a = 10;
                }
            }
        }
        /*
        for(int i = 0; i < oldhashs.size(); i++){
            Transaction tmp = new Transaction();
            tmp.tid = oldhashs.get(i);
            list.add(tmp);
        }
         */
        if(!Util.getMerkleTree(list).equals(b.oldmerkletree)) {
            //System.out.println(list.size());
            //System.out.println("C");
            tend = System.nanoTime();
            System.out.println((tend-tbegin)*1.0/1000000);
            return false;
        }
        tend = System.nanoTime();
        System.out.println((tend-tbegin)*1.0/1000000);
        return true;
    }
    // 验证区块链
    public static boolean validateBlockChain(Blockchain c, BigInteger inithash){
        BigInteger prehash = inithash;
        Stack<Block> stack = new Stack<Block>();
        for(int i = 0; i < c.chain.size(); i++){
            if(!validateBlock(c.getBlock(i))) stack.push(c.getBlock(i));
        }
        while(!stack.empty()){
            Block tmp = stack.pop();
            if(!validateRedactedBlock(tmp,new ArrayList<BigInteger>())) return false;
        }
        return true;
    }
}
