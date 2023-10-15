package yzu;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import it.unisa.dia.gas.jpbc.*;
import java.security.MessageDigest;

public class Shamir {
    public BigInteger p;        // 素数p
    public int t;               // 最少份额
    private Element g;          // 可验证的生成元
    private List<BigInteger> arr;   // 参数
    private MessageDigest md;

    public Shamir(BigInteger p, int t){
        this.p = p;
        this.t = t;
        try{
            md = MessageDigest.getInstance("SHA-256");
        } catch (Exception e){
            e.printStackTrace();
        }
        // 生成t-1个整数
        Random rand = new Random();
        arr = new ArrayList<BigInteger>();
        arr.add(BigInteger.ONE);            // 默认1是秘密
        for(int i = 0; i < t-1; i++) {
            // 随机生成t-1个整数
            BigInteger r;
            r = new BigInteger(p.bitLength()-1,rand);
            arr.add(r);
        }
    }

    public List<BigInteger> getArr() {
        return arr;
    }

    public void setG(Element g){
        this.g = g;
    }

    public List<SecretPiece> splite(int n, BigInteger s){
        List<SecretPiece> ret = new ArrayList<SecretPiece>();
        arr.set(0,s);
        Random rand = new Random();
        // 随机生成n个不同的x以及n个不同的y
        HashSet<BigInteger> xset = new HashSet<BigInteger>();
        int cnt = 0;
        while(cnt < n){
            BigInteger x = new BigInteger(p.bitLength()/2,rand);
            if(!xset.contains(x)){
                xset.add(x);
                cnt++;
                // 计算y
                BigInteger y = s;
                for(int i = 1; i < t; i++){
                    y = y.add(arr.get(i).multiply(x.modPow(BigInteger.valueOf(i),p))).mod(p);
                }
                ret.add(new SecretPiece(x,y));
            }
        }
        return ret;
    }

    public static BigInteger recover(List<SecretPiece> sp, int t, BigInteger p){
        BigInteger secret = BigInteger.ZERO;
        BigInteger tmp;
        BigInteger tmp2;
        for(int i = 0; i < t; i++){
            tmp = sp.get(i).y;
            for(int j = 0; j < t; j++){
                if(i == j) continue;
                tmp2 = sp.get(j).x.subtract(sp.get(i).x);
                tmp = tmp.multiply(sp.get(j).x).multiply(tmp2.modInverse(p)).mod(p);
            }
            secret = secret.add(tmp).mod(p);
        }
        return secret;
    }

    public BigInteger hash(String message){
        BigInteger hash = BigInteger.ZERO;
        try{
            byte[] hashbyte = md.digest(message.getBytes());
            // convert byte array into biginteger
            hash = new BigInteger(1,hashbyte);
        }catch (Exception e){
            e.printStackTrace();
        }
        return hash.mod(p);
    }

    public List<Element> generateCommitments(){
        List<Element> varr = new ArrayList<Element>();
        for(int i = 0; i < t; i++){
            varr.add(g.mul(arr.get(i)).getImmutable());
        }
        return varr;
    }

    public static boolean verifyCommitment(SecretPiece sp, List<Element> varr, Element g, BigInteger p){
        BigInteger x = sp.x;
        BigInteger fx = sp.y;
        Element left = g.duplicate().mul(fx).getImmutable();
        Element right = varr.get(0).getImmutable();
        for(int i = 1; i < varr.size(); i++){
            // x^i
            BigInteger xi = x.modPow(BigInteger.valueOf(i),p);
            Element tmp = varr.get(i).duplicate().mul(xi).getImmutable();
            right = right.add(tmp).getImmutable();
        }
        return left.isEqual(right);
    }
}
