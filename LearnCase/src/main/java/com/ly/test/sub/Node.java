package com.ly.test.sub;

/**
 * Created by magichill33 on 2015/3/30.
 */
public class Node {
    private int recodr;
    private Node nextNode;

    public Node(int recodr) {
        this.recodr = recodr;
    }

    public int getRecodr() {
        return recodr;
    }

    public void setRecodr(int recodr) {
        this.recodr = recodr;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }
}
