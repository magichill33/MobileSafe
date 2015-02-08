package com.ly.lottery.net.protocal;

import com.ly.lottery.ConstantValue;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by magichill33 on 2015/2/8.
 */
public class Header {
    // <agenterid>889931</agenterid>
    private Leaf agenterid = new Leaf("agenterid", ConstantValue.AGENTERID);
    // <source>ivr</source>
    private Leaf source = new Leaf("source",ConstantValue.SOURCE);
    // <compress>DES</compress>
    private Leaf compress = new Leaf("compress",ConstantValue.COMPRESS);
    //
    // <messengerid>20131013101533000001</messengerid>
    private Leaf messengerid = new Leaf("messengerid");
    // <timestamp>20131013101533</timestamp>
    private Leaf timestamp = new Leaf("timestamp");
    // <digest>7ec8582632678032d25866bd4bce114f</digest>
    private Leaf digest = new Leaf("digest");
    //
    // <transactiontype>12002</transactiontype>
    private Leaf transactiontype = new Leaf("transactiontype");
    // <username>13200000000</username>
    private Leaf username = new Leaf("username");

    /**
     * 序列化头
     * @param serializer
     * @param body
     */
    public void serializerHeader(XmlSerializer serializer,String body){
        // 将timestamp、messengerid、digest设置数据
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = format.format(new Date());
        timestamp.setTagValue(time);;
        //messengerid:时间戳+六位的随机数
        Random random = new Random();
        int num = random.nextInt(999999)+1;
        DecimalFormat decimalFormat = new DecimalFormat("0000000");
        messengerid.setTagValue(time+decimalFormat.format(num));

        // digest:时间戳+代理商的密码+完整的body（明文）
        String orgInfo = time + ConstantValue.AGENTER_PASSWORD + body;
        String md5Hex = DigestUtils.md5Hex(orgInfo);
        digest.setTagValue(md5Hex);

        try {
            serializer.startTag(null,"header");
            agenterid.serializerLeaf(serializer);
            source.serializerLeaf(serializer);
            compress.serializerLeaf(serializer);

            messengerid.serializerLeaf(serializer);
            timestamp.serializerLeaf(serializer);
            digest.serializerLeaf(serializer);

            transactiontype.serializerLeaf(serializer);
            username.serializerLeaf(serializer);

            serializer.endTag(null,"header");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Leaf getTransactiontype() {
        return transactiontype;
    }

    public Leaf getUsername() {
        return username;
    }

    /********************************处理服务器回复********************************/
    public Leaf getTimestamp() {
        return timestamp;
    }

    public Leaf getDigest() {
        return digest;
    }
}
