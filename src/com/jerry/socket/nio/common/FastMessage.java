package com.jerry.socket.nio.common;
/**
 * fast消息通讯的目标
 * @author chm
 *
 */
public class FastMessage {
    
    /**消息状态，接收完成*/
    public static final char MESSAGEEND = '1';
    
    /**消息状态，正在接收中*/
    public static final char MESSAGRUNING = '0';
    
    /**消息的默认编码方式*/
    public static final String CODECCHARSET = "UTF-8";
    
    /**在消息发送时，如果发送的消息没有完全发送完成循环等待，每次循环等待时间，单位毫秒*/
    public static final int SENDMESSAGEWAITTIME = 1;
    
    /**在消息接收时，如果消息没有接收完成，进行循环等待，每次循环等待时间，单位毫秒*/
    public static final int RECEIVEMESSAGEWAITTIME = 3;
    
    
    /**接收消息时的默认等待时间，用户每次操作可以设置不同的超时时间，单位毫秒*/
    public static final int  RECEIVEMESSAGETIMEOUT= 20000;
    
    /**默认的一个selector监听器读取小的线程数*/
    public static final int DEFAULTREADTHREADCOUNT = Runtime.getRuntime().availableProcessors()*2;
    
    /**握手消息的操作类型*/
    public static final int FIRSTHANDOPERTYPE = 101;
    
    /**消息的最大长度, 4M*/
    public static final int MAXMESSAGELEN = 1024 * 1024 * 4;
    
    
}
