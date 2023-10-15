package yzu;

import java.math.BigInteger;
import java.util.*;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class Main {

    public static void main(String[] args) {
        //TimeOverheadExperiment();
        //SecretNumberTExperiment();
        RedactedBlockDelayExperiment(1000);
        /*
        for(int i = 100; i <= 3000; i = i+100){
            RedactedBlockDelayExperiment(i);
        }
        */
        //RedactedBlockDelayExperiment();
        /*
        // 生成pairing
        Pairing pairing = PairingFactory.getPairing("a.properties");
        // 生成Field
        Field<Element> G1;
        G1 = pairing.getG1();
        // 测试秘密分享
        ECDSA ecdsa = new ECDSA();
        BigInteger secret = ecdsa.getSecretKey();   // 获取私钥
        Shamir shamir = new Shamir(G1.getOrder(),3);   // 将私钥分成20份
        shamir.setG(ecdsa.getG());                  // 设置可验证的生成元
        List<SecretPiece> sps = shamir.splite(10,secret);
        List<Element> varr = shamir.generateCommitments();
        List<BigInteger> arr = shamir.getArr();
        for(int i = 0; i < arr.size(); i++) System.out.println(arr.get(i));
        if(Shamir.verifyCommitment(sps.get(0),varr,ecdsa.getG(),G1.getOrder())) System.out.println("PASS!");
        else System.out.println("FAIL!");
        //
        System.out.println("secret:"+secret);
        sps.remove(0);
        System.out.println("recovery:"+Shamir.recover(sps,3,G1.getOrder()));
        System.out.println("vrr0:"+varr.get(0));
        System.out.println("ECDSA pk:"+ecdsa.getPublicKey());
        //
        BigInteger two = BigInteger.TWO;
        BigInteger ten = BigInteger.TEN;
        BigInteger twenty = new BigInteger("12");
        Element g = G1.newRandomElement().getImmutable();
        Element twoe = g.mul(two).getImmutable();
        Element tene = g.mul(ten).getImmutable();
        //Element right = twoe.add(tene).getImmutable();
        //Element left = g.mul(twenty).getImmutable();
        //if(left.isEqual(right)) System.out.println("No Problem!");
        //else System.out.println("Here is a problem!");
        BigInteger tt = new BigInteger("20");
        Element right2 = twoe.mul(BigInteger.TEN).getImmutable();
        Element left2 = g.mul(tt).getImmutable();
        if(left2.isEqual(right2)) System.out.println("No Problem!");
        else System.out.println("Here is a problem!");
        //testVRF();
        //System.out.println(test);
        //System.out.println(g);
        //testECDSA();
        //registration();
        // Shamir
        */
        /*
        Shamir shamir = new Shamir(BigInteger.valueOf(827),3);
        List<SecretPiece> sp = shamir.splite(4,BigInteger.valueOf(233));
        for(int i = 0; i < sp.size(); i++) {
            System.out.println("x:"+sp.get(i).x+",y:"+sp.get(i).y);
        }
        BigInteger secret = shamir.recover(sp);
        System.out.println(secret);
         */

    }

    public static void registration(){
        // 生成pairing
        Pairing pairing = PairingFactory.getPairing("a.properties");
        // 生成Field
        Field<Element> G1;
        G1 = pairing.getG1();
        // 一些参数
        int n = 20;
        ECDSA ecdsa = new ECDSA();
        Shamir shamir = new Shamir(G1.getOrder(),n);

        // 生成n个参数
        BigInteger secret = ecdsa.getSecretKey();
        List<Element> vrr = new ArrayList<>();
        for(int i = 0; i < 20; i++){}
        // arr.get(0)
        // 随机生成一对秘密碎片
        /*
        Element x = Zq.newRandomElement().getImmutable();
        Element fx = arr.get(0).duplicate();
        for(int i = 1; i < n; i++){
            fx = fx.add(arr.get(i).mulZn(x.powZn(Zq.newElement(i))));
        }
        fx = fx.getImmutable();
        // 生成验证用的承诺
        List<Element> vrr = new ArrayList<>();
        Element g = G1.newRandomElement().getImmutable();
        for(int i = 0; i < n; i++) vrr.add(g.powZn(arr.get(i)));
        // 验证承诺
        Element gv = g.powZn(fx).getImmutable();
        Element gv2 = vrr.get(0).duplicate();
        for(int i = 1; i < n; i++){
            gv2 = gv2.mul(vrr.get(i).powZn(x.powZn(Zq.newElement(i))));
        }
        gv2 = gv2.getImmutable();
        if(gv.isEqual(gv2)) System.out.println("PASS");
        else System.out.println("FAIL");
        // arr[0]的椭圆曲线签名
         */

    }

    public static void testVRF(){
        String message = "HelloWorld";
        VRF vrf = new VRF();
        Element r1 = vrf.getRandomNumber(message);
        Element r2 = vrf.getRandomNumber(message);
        if(r1.isEqual(r2)) System.out.println("Pass r1 = r2!");
        else System.out.println("Fail r1 != r2!");
        Element proof = vrf.getProof(message);
        Element pk = vrf.getPublicKey();
        if(vrf.verify(message,r1,proof,pk)) System.out.println("Pass Verification!");
        else System.out.println("Fail Verification!");
    }

    public static void testPoint(){
        // 生成pairing
        Pairing pairing = PairingFactory.getPairing("a.properties");
        // 生成Field
        Field<Element> G1,Zq;
        G1 = pairing.getG1();
        Element e = G1.newRandomElement().getImmutable();
        Point t = (Point)e.duplicate();
        System.out.println(t.getX());
        System.out.println(e);

    }

    public static void testECDSA(){
        ECDSA ecdsa = new ECDSA();
        String message = "Hello World!";
        ECDSASig sig = ecdsa.sign(message);
        if(ecdsa.verify(message,sig)) System.out.println("Pass!");
        else System.out.println("Fail!");
    }

    public static void TimeOverheadExperiment(){
        int n = 500;       // 重复n次
        int t = 20;
        // 生成pairing
        Pairing pairing = PairingFactory.getPairing("a.properties");
        // 生成Field
        Field<Element> G1;
        G1 = pairing.getG1();
        // ********************************* Initialization *********************************
        // Initialization 用户进入的初始化，需要初始化ECDSA,Shamir,VRF
        // 平均1点几
        long init_begin, init_end;
        ECDSA ecdsa = null;
        Shamir shamir = null;
        VRF vrf = null;
        for(int i = 0; i < n; i++){
            init_begin = System.nanoTime();
            ecdsa = new ECDSA();
            shamir = new Shamir(G1.getOrder(),t);
            shamir.setG(ecdsa.getG());
            vrf = new VRF();
            init_end = System.nanoTime();
            System.out.println("init:"+(init_end-init_begin)*1.0/1000000);
        }
        // ********************************* Registeration *********************************
        Util.g = ecdsa.getG();
        Util.q = G1.getOrder();
        BigInteger Alice_sk = ecdsa.generateNewUser();
        Element Alice_addr = ecdsa.getPublicKey();
        BigInteger sd_sk = ecdsa.generateNewUser();
        Element sd_addr = ecdsa.getPublicKey();
        BigInteger Bob_sk = ecdsa.generateNewUser();
        Element Bob_addr = ecdsa.getPublicKey();
        // 平均500出头
        // Registeration 注册请求，需要生成秘密分享和承诺
        long reg_begin, reg_end;
        long mod_begin, mod_end;
        List<SecretPiece> sps = null;
        List<Element> varr = null;
        for(int i = 0; i < n; i++){
            reg_begin = System.nanoTime();
            sps = shamir.splite(2*t, sd_sk);
            varr = shamir.generateCommitments();
            reg_end = System.nanoTime();
            System.out.println("reg:"+(reg_end-reg_begin)*1.0/1000000);
            mod_begin = System.nanoTime();
            // 验证密钥
            for(int j = 0; j < t; j++){
                if(!Shamir.verifyCommitment(sps.get(j),varr,ecdsa.getG(),G1.getOrder())) System.out.println("wrong secret pieces!");
            }
            // 恢复密钥
            BigInteger skrecovery = Shamir.recover(sps,t,Util.q);
            if(!skrecovery.equals(sd_sk)) System.out.println("error!");
            // 签名
            String tmp = sd_addr.toString()+Bob_addr.toString()+"10";
            ecdsa.setSecretKey(sd_sk);
            ECDSASig sig0 = ecdsa.sign(tmp);
            // 生成补偿交易
            Transaction remedyTran = new Transaction(sd_addr,Bob_addr,10,sig0);
            remedyTran.isRemedyTransaction = true;
            ecdsa.setSecretKey(Alice_sk);
            ECDSASig remedysig = ecdsa.sign(tmp);
            remedyTran.remedysig = remedysig;
            mod_end = System.nanoTime();
            System.out.println("modify:"+(mod_end-mod_begin)*1.0/1000000);
        }
        // ********************************* Vote Distribution *********************************
        // Vote Distribution 选票分发， 按照算法来
        long vd_begin, vd_end;
        BigInteger id_reg = Util.Hash("Test Vote Distribution!");
        int num = 2000;         // 2000人申请成为监管用户
        for(int i = 0; i < n; i++){
            vd_begin = System.nanoTime();
            List<Element> proofs = new ArrayList<Element>();
            int nidx = Util.Hash(vrf.getRandomNumber(id_reg.toString()).toString()).mod(BigInteger.valueOf(num)).intValue();
            proofs.add(vrf.getProof(id_reg.toString()));
            List<Integer> idx = new ArrayList<Integer>();
            while(idx.size() < 2*t){
                nidx = nidx % num;
                while(idx.contains(nidx)) nidx = (nidx + 1) % num;
                idx.add(Integer.valueOf(nidx));
                nidx = Util.Hash(vrf.getRandomNumber(""+nidx).toString()).mod(BigInteger.valueOf(num)).intValue();
                proofs.add(vrf.getProof(""+nidx));
            }
            vd_end = System.nanoTime();
            System.out.println("votedistribution:"+(vd_end-vd_begin)*1.0/1000000);
        }
        // Modify 恢复sk_SD,对补偿交易签名
        // Modify在上面
        // Validation of Block
    }

    public static void SecretNumberTExperiment(){
        // 测试碎片阈值t对区块链的影响
        int n = 100;        // 重复n次
        List<Integer> ts = new ArrayList<Integer>();
        // 生成pairing
        Pairing pairing = PairingFactory.getPairing("a.properties");
        // 生成Field
        Field<Element> G1;
        G1 = pairing.getG1();
        // 每个t都尝试一次
        for(int t = 2; t < 129; t++){
            System.out.print("t:"+t+" ");
            // 初始化
            long sum = 0;
            long init_begin, init_end;
            ECDSA ecdsa = null;
            Shamir shamir = null;
            VRF vrf = null;
            sum = 0;
            for(int i = 0; i < n; i++){
                init_begin = System.nanoTime();
                ecdsa = new ECDSA();
                shamir = new Shamir(G1.getOrder(),t);
                shamir.setG(ecdsa.getG());
                vrf = new VRF();
                init_end = System.nanoTime();
                sum += (init_end-init_begin);
            }
            System.out.print((sum*1.0/1000000)/n+" ");
            // 注册请求，生成秘密分享和承诺
            Util.g = ecdsa.getG();
            Util.q = G1.getOrder();
            BigInteger Alice_sk = ecdsa.generateNewUser();
            Element Alice_addr = ecdsa.getPublicKey();
            BigInteger sd_sk = ecdsa.generateNewUser();
            Element sd_addr = ecdsa.getPublicKey();
            BigInteger Bob_sk = ecdsa.generateNewUser();
            Element Bob_addr = ecdsa.getPublicKey();
            long reg_begin, reg_end;
            long mod_begin, mod_end;
            long sum2;
            sum = sum2 = 0;
            List<SecretPiece> sps = null;
            List<Element> varr = null;
            for(int i = 0; i < n; i++){
                reg_begin = System.nanoTime();
                sps = shamir.splite(2*t, sd_sk);
                varr = shamir.generateCommitments();
                reg_end = System.nanoTime();
                sum += (reg_end-reg_begin);
                mod_begin = System.nanoTime();
                // 验证密钥
                for(int j = 0; j < t; j++){
                    if(!Shamir.verifyCommitment(sps.get(j),varr,ecdsa.getG(),G1.getOrder())) System.out.println("wrong secret pieces!");
                }
                // 恢复密钥
                BigInteger skrecovery = Shamir.recover(sps,t,Util.q);
                if(!skrecovery.equals(sd_sk)) System.out.println("error!");
                // 签名
                String tmp = sd_addr.toString()+Bob_addr.toString()+"10";
                ecdsa.setSecretKey(sd_sk);
                ECDSASig sig0 = ecdsa.sign(tmp);
                // 生成补偿交易
                Transaction remedyTran = new Transaction(sd_addr,Bob_addr,10,sig0);
                remedyTran.isRemedyTransaction = true;
                ecdsa.setSecretKey(Alice_sk);
                ECDSASig remedysig = ecdsa.sign(tmp);
                remedyTran.remedysig = remedysig;
                mod_end = System.nanoTime();
                sum2 += (mod_end-mod_begin);
            }
            System.out.print((sum*1.0/1000000)/n+" ");
            System.out.println((sum2*1.0/1000000)/n);
            // Modify
        }
        // 初始化
        // 注册请求时间
        // Modify时间

    }

    public static void RedactedBlockDelayExperiment(int redactnum){
        int blocknum = 100;      // 区块链数量
        // 被修改的区块数量占比对区块链验证速度的影响
        int usernum = 50;     // 用户数量
        int transactionnum = 200; // 一个区块的交易数量
        //System.out.print(""+redactnum*1.0/(blocknum*transactionnum)+" ");    // 输出比例
        // 生成随机数
        Random r = new Random();
        // 生成pairing
        Pairing pairing = PairingFactory.getPairing("a.properties");
        // 生成Field
        Field<Element> G1;
        G1 = pairing.getG1();
        ECDSA ecdsa = new ECDSA();
        // 系统参数
        Util.g = ecdsa.getG();
        Util.q = G1.getOrder();
        Blockchain chain = new Blockchain();
        // 生成用户 map: pk -> sk
        HashMap<Element,BigInteger> map = new HashMap<Element,BigInteger>();
        List<Element> userlist = new ArrayList<Element>();
        for(int i = 0; i < usernum; i++){
            BigInteger newsk = ecdsa.generateNewUser();
            Element newpk = ecdsa.getPublicKey();
            map.put(newpk,newsk);
            userlist.add(newpk);
        }
        // 生成创世哈希值
        String tmp = "Hello Blockchain!";
        BigInteger inithash = Util.Hash(tmp);
        BigInteger prehash = inithash;
        // 生成区块
        for(int i = 0; i < blocknum; i++){
            Block b = new Block(i,prehash);
            // 随机生成transactionnum个交易
            for(int j = 0; j < transactionnum; j++){
                // 随机选择一个作为sender
                Element senderAddr = userlist.get(r.nextInt(usernum));
                BigInteger sendersk = map.get(senderAddr);
                // 随机选择一个作为receiver
                Element receiverAddr = userlist.get(r.nextInt(usernum));
                // 随机选取交易数量
                int num = r.nextInt(100);
                // 生成签名
                ecdsa.setSecretKey(sendersk);
                ECDSASig sig0 = ecdsa.sign(senderAddr.toString()+receiverAddr.toString()+num);
                Transaction newTransaction = new Transaction(senderAddr,receiverAddr,num,sig0);
                b.transactions.add(newTransaction);
            }
            // 计算该区块的默尔克树
            b.generateMerkletree();
            b.setOldMerkletree();
            // 计算该区块的哈希
            prehash = Util.Hash(""+b.height+b.pre+b.merkletree+b.oldmerkletree);
            // 区块链增加一个区块
            chain.chain.add(b);
        }
        // 验证区块链
        long time_begin,time_end;
        time_begin = System.nanoTime();
        if(!Util.validateBlockChain(chain,inithash)) System.out.println("something error!");
        time_end = System.nanoTime();
        //System.out.print((time_end-time_begin)*1.0/1000000000+" ");
        // 生成保证金账户
        BigInteger sdsk = ecdsa.generateNewUser();
        Element sdAddr = ecdsa.getPublicKey();
        // 修改区块链，需要生成新的交易，并移除旧交易，除此意外需要将旧交易的id记录在remede交易中
        HashSet<Integer> set = new HashSet<Integer>();
        // 随机修改redactnum数量个任意交易
        for(int i = 0; i < redactnum; i++){
            // 获取随机一个区块
            int idx = r.nextInt(blocknum);
            Block b = chain.getBlock(idx);
            while(b.redactedNum >= transactionnum){
                idx = r.nextInt(blocknum);
                b = chain.getBlock(idx);
            }

            // 获取随机一个交易
            int tidx = r.nextInt(b.transactions.size());
            Transaction t = b.transactions.get(tidx);
            while(t.isRedacted || t.isRemedyTransaction){
                tidx = r.nextInt(b.transactions.size());
                t = b.transactions.get(tidx);
            }
            // 构造补偿交易
            String message = sdAddr.toString()+t.receiverAddr.toString()+"10";
            ecdsa.setSecretKey(sdsk);
            ECDSASig sig0 = ecdsa.sign(message);
            Transaction remedyTran = new Transaction(sdAddr,t.receiverAddr,10,sig0);
            remedyTran.isRemedyTransaction = true;
            // 找到sender并签名
            BigInteger sendersk = map.get(t.senderAddr);
            ecdsa.setSecretKey(sendersk);
            ECDSASig remedysig = ecdsa.sign(message);
            remedyTran.remedysig = remedysig;
            remedyTran.originaltid = t.tid;
            remedyTran.userAddr = t.senderAddr;
            // 修改原交易
            t.num = 0;
            t.isRedacted = true;
            String message2 = t.senderAddr.toString()+t.receiverAddr.toString()+t.num;
            t.tid = Util.Hash(message2);
            ecdsa.setSecretKey(sendersk);
            ECDSASig sig1 = ecdsa.sign(message2);
            t.sig0 = sig1;
            // 将补偿交易放入区块中
            b.transactions.add(remedyTran);
            b.redactedNum++;
            b.generateMerkletree();
        }
        // 验证修改后的区块链
        System.out.println("Validate Redactable Blockchain!");
        time_begin = System.nanoTime();
        if(!Util.validateBlockChain(chain,inithash)) System.out.println("redact error!");
        time_end = System.nanoTime();
        //System.out.println((time_end-time_begin)*1.0/1000000000);
    }

}

// 区块链验证方面，只需要验证签名和对应区块里面的哈希