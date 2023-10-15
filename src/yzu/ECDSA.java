package yzu;
import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.math.BigInteger;
import java.util.Random;
import java.security.MessageDigest;

public class ECDSA {
    private Pairing pairing;
    private Field<Element> G1;
    private Element g;          // 生成元
    private BigInteger x;       // 私钥
    private BigInteger q;       // Fq域
    private MessageDigest md;

    public ECDSA(){
        try{
            md = MessageDigest.getInstance("SHA-256");
        }catch (Exception e){
            e.printStackTrace();
        }
        pairing = PairingFactory.getPairing("a.properties");
        G1 = pairing.getG1();
        q = G1.getOrder();
        //q = new BigInteger("8780710799663312522437781984754049815806883199414208211028653399266475630880222957078625179422662221423155858769582317459277713367317481324925129998224791");
        g = G1.newRandomElement().getImmutable();
        x = getRandomBigInteger();
    }

    public BigInteger generateNewUser(){
        x = getRandomBigInteger();
        return x;
    }

    public Element getPublicKey(){
        return g.mul(x).getImmutable();
    }

    public BigInteger getSecretKey() {
        return x;
    }

    public Element getG(){
        return g;
    }

    public void setG(Element g){
        this.g = g;
    }

    public void setSecretKey(BigInteger x){
        this.x = x;
    }


    public ECDSASig sign(String message){

        // 生成临时私钥k
        BigInteger k = getRandomBigInteger().mod(q);
        // 计算kG,在乘法群中是G^k
        Element K = g.mul(k).getImmutable();
        Point point = (Point)K.duplicate();
        BigInteger r = point.getX().toBigInteger().mod(q);
        BigInteger e = hash(message);
        BigInteger s = k.modInverse(q).multiply(e.add(r.multiply(x))).mod(q);
        BigInteger k2 = s.modInverse(q).multiply(e.add(r.multiply(x))).mod(q);
        return new ECDSASig(r,s);
    }

    public boolean verify(String message, ECDSASig sig){
        BigInteger e = hash(message);
        BigInteger r = sig.r;
        BigInteger s = sig.s;
        BigInteger u1 = s.modInverse(q).multiply(e).mod(q);
        BigInteger u2 = s.modInverse(q).multiply(r).mod(q);

        Element u1g = g.mul(u1).getImmutable();
        Element u2pk = getPublicKey().mul(u2).getImmutable();
        Element P = u1g.add(u2pk).getImmutable();
        Point point = (Point)P.duplicate();
        BigInteger xp = point.getX().toBigInteger().mod(q);
        return xp.equals(r);
    }

    public static boolean verifyTransaction(String message, ECDSASig sig, Element g, Element pk, BigInteger q){
        BigInteger e = Util.Hash(message);
        BigInteger r = sig.r;
        BigInteger s = sig.s;
        BigInteger u1 = s.modInverse(q).multiply(e).mod(q);
        BigInteger u2 = s.modInverse(q).multiply(r).mod(q);

        Element u1g = g.duplicate().mul(u1).getImmutable();
        Element u2pk = pk.duplicate().mul(u2).getImmutable();
        Element P = u1g.add(u2pk).getImmutable();
        Point point = (Point)P.duplicate();
        BigInteger xp = point.getX().toBigInteger().mod(q);
        return xp.equals(r);
    }

    public BigInteger getRandomBigInteger(){
        BigInteger bi = q.subtract(BigInteger.ONE);
        Random randNum = new Random();
        int len = q.bitLength();
        BigInteger res = new BigInteger(len,randNum);
        if(res.compareTo(BigInteger.ONE) < 0) res = res.add(BigInteger.ONE);
        if(res.compareTo(bi) >= 0) res = res.mod(bi).add(BigInteger.ONE);
        return res;
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
        return hash.mod(q);
    }

}
