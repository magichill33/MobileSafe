package com.ly.test;

import com.ly.test.sub.Node;

/**
 * Created by magichill33 on 2015/3/30.
 */
public class ReverseList {
    public static Node reverse(Node head)
    {
        if (null == head||null == head.getNextNode())
        {
            return head;
        }
        Node reversedHead = reverse(head.getNextNode());
        head.getNextNode().setNextNode(head);
        head.setNextNode(null);
        return reversedHead;
    }

    /**
     * 遍历，将当前节点的下一个节点缓存后更改当前节点指针
     *
     */
    public static Node reverse2(Node head) {
        if (null == head) {
            return head;
        }
        Node pre = head;
        Node cur = head.getNextNode();
        Node next;
        while (null != cur) {
            next = cur.getNextNode();
            cur.setNextNode(pre);
            pre = cur;
            cur = next;
        }
        //将原链表的头节点的下一个节点置为null，再将反转后的头节点赋给head
        head.setNextNode(null);
        head = pre;

        return head;
    }

    public static void main(String[] args)
    {
        Node head = new Node(0);
        Node tmp = null;
        Node cur = null;
        for (int i = 1;i<10;i++)
        {
            tmp = new Node(i);
            if (1==i)
            {
                head.setNextNode(tmp);
            }else {
                cur.setNextNode(tmp);
            }
            cur = tmp;
        }

        //打印反转前的链表
        Node h = head;
        while (null != h) {
            System.out.print(h.getRecodr() + " ");
            h = h.getNextNode();
        }
        //调用反转方法
        head = reverse(head);
        System.out.println("\n**************************");
        //打印反转后的结果
        while (null != head) {
            System.out.print(head.getRecodr() + " ");
            head = head.getNextNode();
        }

    }
}
