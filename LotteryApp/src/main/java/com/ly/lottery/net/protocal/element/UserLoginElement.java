package com.ly.lottery.net.protocal.element;

import com.ly.lottery.net.protocal.Element;
import com.ly.lottery.net.protocal.Leaf;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;


/**
 * Created by Administrator on 2015/2/9.
 */
public class UserLoginElement extends Element{
    private Leaf actpassword = new Leaf("actpassword");


    @Override
    public void serializerElement(XmlSerializer serializer) {
        try {
            serializer.startTag(null,"element");
            actpassword.serializerLeaf(serializer);
            serializer.endTag(null,"element");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTransactionType() {
        return "14001";
    }

    public Leaf getActpassword() {
        return actpassword;
    }
}
