package com.jerry.socket.nio.session;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.jerry.socket.nio.common.FastMessage;
import com.jerry.socket.nio.message.Message;
import com.jerry.socket.nio.message.NioMessageFacade;
import com.jerry.socket.nio.message.SimpleMessage;
import com.jerry.socket.nio.message.filterchain.INioFilterChain;
import com.jerry.socket.nio.message.transfer.PackMessage;
import com.jerry.socket.nio.message.transfer.SimplePackMessage;
import com.jerry.socket.nio.message.wrapper.INioMessageWrapper;
import com.jerry.socket.nio.message.wrapper.NioMessageWrapper;
import com.jerry.socket.nio.service.NioHandler;
import com.jerry.socket.nio.service.NioHandlerAdapter;

/**
 * 每一个客户单连接后需要发送一个握手消息来表名身份，发送一个唯一的id，同时发送一个此客户端的描述字符串，用来表示被服务端监控使用 如在zcip
 * 平台中，因为在一个主机一个用户下只能有一个客户端，那么发送(ip+用户名.toUpper()).hashCode的形式，并发送ip地址和用户名 用来在服务端的监控使用
 * 
 * @author chm
 */
public abstract class NioSession implements INioSession {
    
    
    NioHandler niohandler = null;
    
    @Override
    public NioHandler getHandler() {
        if (niohandler == null) {
            niohandler = new NioHandlerAdapter();
        }
        return niohandler;
    }

    @Override
    public void setHandler(NioHandler niohandler) {
        this.niohandler = niohandler;
    }

    /** 单位毫秒，针对当前session 有效，如果session 不同消息的超时时间不同，那么需要在发送消息是进行重新的设置 */
    private int timeOut = FastMessage.RECEIVEMESSAGETIMEOUT;

    private PackMessage packMeg = new SimplePackMessage();
    
    private String clientIp;

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    private Selector selector;
    
    /**当前的NioSession 是否有效*/
    private boolean isVaild = true;

    private SelectionKey skey;

    public SelectionKey getSkey() {
        return skey;
    }

    public void setSkey(SelectionKey skey) {
        this.skey = skey;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public void destory() throws IOException {
    	try {
    		isVaild = false;
            if (skey != null) {
                skey.cancel();
            }
            
            if (channel != null) {
                channel.close();
            }
    	}
    	finally {
    		NioSessionMag.getInstance().deleteNioSession(this);
    	}
    	
        
       
        
    }
    
    @Override
    public boolean isVaild() {
        return isVaild;
    }

    /**
     * 消息的处理方式，默认不做任何处理直接返回，用户需要对此进行有效的扩展
     */
    private INioMessageWrapper nioMegWrapper = new NioMessageWrapper();

    public INioMessageWrapper getNioMegWrapper() {
        return nioMegWrapper;
    }

    public void setNioMegAction(INioMessageWrapper nioMegWrapper) {
        this.nioMegWrapper = nioMegWrapper;
    }

    // private final String nioClientToStringMessage;

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    /**被初始化时设置，后续也可以被改变*/
    private long sessionId;

    public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	private INioFilterChain nioFilterChain = null;

    public long getSessionId() {
        return sessionId;
    }

    private SocketChannel channel;

    public NioSession(SocketChannel channel, long sessionId, Selector selector) {
        this.channel = channel;
        this.sessionId = sessionId;
        this.selector = selector;
        // 每一个channel的端口都是不一样的
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }

    public INioFilterChain getFilterChain() {
        return nioFilterChain;
    }

    public void setFilterChain(INioFilterChain nioFilterChain) {
        this.nioFilterChain = nioFilterChain;
    }

    public void closeSession() {

        try {
        	//失效
            isVaild = false;
            if (skey != null) {
                skey.cancel();
            }
            
            if (channel != null && channel.isConnected()) {
                channel.close();
            }

            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            NioSessionMag.getInstance().deleteNioSession(this);
        }
    }

    public Object syncSendMessage(Object message, int operType, long sessionId) throws IOException,
        InterruptedException {
        return syncSendMessage(message, FastMessage.MESSAGEEND, operType, sessionId);
    }

    public Object syncSendMessage(Object message, char state, int operType, long sessionId) throws IOException,
        InterruptedException {
        return syncSendMessage(message, state, operType, sessionId, this.timeOut);
    }
    
    
    public Object syncSendMessage(Object message, int operType, long sessionId, int timeOut) throws IOException,
    InterruptedException {
        return syncSendMessage(message, FastMessage.MESSAGEEND, operType, sessionId, timeOut);
    }
    
    
    public Object syncSendMessage(Object message, char state, int operType, long sessionId, int timeOut) throws IOException,
    InterruptedException {

    Message messageResult = null;
    byte[] messageTemp = packMessage(message, state, operType, sessionId);
    int writeSize = this.getChannel().write(ByteBuffer.wrap(messageTemp));
    while (writeSize <= 0) {
        writeSize = this.getChannel().write(ByteBuffer.wrap(messageTemp));
        Thread.sleep(FastMessage.SENDMESSAGEWAITTIME);
    }

    long nowMin = System.currentTimeMillis();
    boolean isContinue = true;
    while (isContinue) {

        messageResult = NioMessageFacade.getInstance().getAndDelMessage(sessionId);
        // 消息必须isReady 才能被返回使用
        if (null != messageResult) {
            return messageResult;
        }
        if ((System.currentTimeMillis() - nowMin) > timeOut) {
            isContinue = false;
            break;
        }
        Thread.sleep(FastMessage.RECEIVEMESSAGEWAITTIME);
    }
    if (!isContinue) {

        messageResult = new SimpleMessage();
        messageResult.setSessionId(sessionId);
        messageResult.setReady(true);
        messageResult.setTimeOut(true);
    }
    return this.getNioMegWrapper().dealWithMessage(messageResult);
}
    

    public Object syncSendMessage(Object message, char state, int operType) throws IOException, InterruptedException {

        long sessionId = System.nanoTime();
        return syncSendMessage(message, state, operType, sessionId);
    }
    
    public Object syncSendMessage(Object message, int operType, int timeOut) throws IOException, InterruptedException {

        long sessionId = System.nanoTime();
        return syncSendMessage(message, FastMessage.MESSAGEEND, operType, sessionId, timeOut);
    }

    public Object syncSendMessage(Object message, int operType) throws IOException, InterruptedException {

        long sessionId = System.nanoTime();
        return syncSendMessage(message, FastMessage.MESSAGEEND, operType, sessionId);
    }

    public void asyncSendMessage(Object message, char state, int operType, long sessionId) throws IOException,
        InterruptedException {
        byte[] messageTemp = packMessage(message, state, operType, sessionId);

        int writeSize = this.getChannel().write(ByteBuffer.wrap(messageTemp));
        while (writeSize <= 0) {
            Thread.sleep(FastMessage.SENDMESSAGEWAITTIME);
            writeSize = this.getChannel().write(ByteBuffer.wrap(messageTemp));
        }
    }

    /**
     * 异步发送消息，不需要同步等待结果
     * 
     * @param message 发送的消息，消息只能有两种类型，一个是字符串String类型，一个是字节码数据
     * @param state 状态，'1' 表示完成，'0' 表示为完成
     * @param operType 操作类型
     * @throws IOException 操作异常
     * @throws InterruptedException 线程中断异常
     */
    public void asyncSendMessage(Object message, char state, int operType) throws IOException, InterruptedException {
        long sessionId = System.nanoTime();
        asyncSendMessage(message, state, operType, sessionId);
    }

    
    /**
     * 异步发送消息，不需要同步等待结果
     * 
     * @param message 发送的消息，消息只能有两种类型，一个是字符串String类型，一个是字节码数据
     * @param operType 操作类型
     * @throws IOException 操作异常
     * @throws InterruptedException 线程中断异常
     */
    public void asyncSendMessage(Object message, int operType) throws IOException, InterruptedException {
        long sessionId = System.nanoTime();
        asyncSendMessage(message, FastMessage.MESSAGEEND, operType, sessionId);
    }
    
    /**
     * 异步发送消息，不需要同步等待结果
     * 
     * @param message 发送的消息，消息只能有两种类型，一个是字符串String类型，一个是字节码数据
     * @param operType 操作类型
     * @param sessionId 会话id
     * @throws IOException 操作异常
     * @throws InterruptedException 线程中断异常
     */
    public void asyncSendMessage(Object message, int operType, long sessionId) throws IOException, InterruptedException {
        asyncSendMessage(message, FastMessage.MESSAGEEND, operType, sessionId);
    }

    private byte[] packMessage(Object message, char state, int operType, long sessionId)
        throws UnsupportedEncodingException {

        byte[] messageBody = null;
        if (message instanceof String) {
            String str = (String) message;
            messageBody = str.getBytes(FastMessage.CODECCHARSET);
        }
        else if (message instanceof byte[]) {
            messageBody = (byte[]) message;
        }
        else {
            throw new ClassCastException("No support type, except for String and byte[]");
        }
        return packMeg.packMessage(messageBody.length, state, sessionId, operType, messageBody);
    }

    

}
