package yzu;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    public List<Block> chain;
    public Blockchain(){
        chain = new ArrayList<Block>();
    }

    public Block getBlock(int height){
        return chain.get(height);
    }

    public void setBlock(int height, Block b){
        chain.set(height,b);
    }
}
