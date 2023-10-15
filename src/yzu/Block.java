package yzu;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Block {
    public int height;      // 区块高度
    public BigInteger pre;  // 前一个区块的hash值
    public BigInteger merkletree;   // 所有交易的默克尔树
    public BigInteger oldmerkletree;    // （针对已修改的数据）旧交易的默克尔树
    public List<Transaction> transactions;  // 交易数据
    public int redactedNum;

    public Block(int height, BigInteger pre){
        this.height = height;
        this.pre = pre;
        this.transactions = new ArrayList<Transaction>();
        this.redactedNum = 0;
    }

    // 计算本区块的默克尔树:所有交易的tid的累加并计算
    public void generateMerkletree(){
        this.merkletree = Util.getMerkleTree(this.transactions);
    }

    public void setOldMerkletree(){
        this.oldmerkletree = this.merkletree;
    }
    // 区块验证的时候，用补偿交易附带的id计算原默克尔树
    // 计算前需要对List进行排序
}
