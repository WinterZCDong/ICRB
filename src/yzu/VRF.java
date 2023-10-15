package yzu;
import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class VRF {
    private Pairing pairing;
    private Field<Element> G1;
    private Field<Element> G2;
    private Field<Element> Zq;
    private Element g;
    private Element x;          // sk
    // https://blog.csdn.net/u011211976/article/details/89498317

    public VRF() {
        pairing = PairingFactory.getPairing("a.properties");
        G1 = pairing.getG1();
        G2 = pairing.getG2();
        Zq = pairing.getZr();
        g = G1.newRandomElement().getImmutable();
        x = Zq.newRandomElement().getImmutable();
    }

    // return the public key
    public Element getPublicKey(){
        return g.powZn(x).getImmutable();
    }

    // return the random Number of message
    public Element getRandomNumber(String message){
        Element r = G2.newElementFromBytes(message.getBytes()).getImmutable();
        return r;
    }

    // return the proof of message
    public Element getProof(String message){
        Element proof = getRandomNumber(message).powZn(x).getImmutable();
        return proof;
    }

    public boolean verify(String message, Element r, Element proof, Element pk){
        Element h = getRandomNumber(message);
        Element e_proof_g = pairing.pairing(proof,g).getImmutable();
        Element e_message_pk = pairing.pairing(h,pk).getImmutable();
        return e_proof_g.isEqual(e_message_pk);
    }
}

