package com.jerry.socket.nio.read;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.jerry.socket.nio.common.FastMessage;
import com.jerry.socket.nio.message.Message;
import com.jerry.socket.nio.message.NioMessageFacade;
import com.jerry.socket.nio.message.SimpleMessage;
import com.jerry.socket.nio.session.NioSession;
import com.jerry.socket.nio.session.NioSessionMag;

/**
 * 消息读取的抽象方法
 * @author chm
 *
 */
public final class SingleMessageRead implements MessageRead, Runnable {

    /**连接会话*/
    private NioSession nioSession;
    
    /**长度，状态('0' 未完成，'1'，完成)，sessionId, messageType*/
    private final int buffSizeHeader = 4 + 2 + 8 + 4;
    
    
    
    public SingleMessageRead(NioSession nioSession) {
        this.nioSession = nioSession;
    }
    
    public void start() {
        System.out.println("Begin read " + nioSession.toString() + " message ....");
    }
    
    public void finish() {
        try {
          //增加对read 事件的监听
            nioSession.getSelector().wakeup();
            nioSession.getChannel().register(nioSession.getSelector(), SelectionKey.OP_READ, nioSession);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("End read " + nioSession.toString() + " message ....");
    }
    
    /**
     * 读取管道的数据内容
     */
    public void run() {
        try {
            readMessage();
            finish();
        }
        catch (Exception e) {
            //增加错误处理方法
            try {
                nioSession.getHandler().exceptionCaught(nioSession, e);  
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
            closeSession();
            NioSessionMag.getInstance().deleteNioSession(nioSession);
        }
        
    }
    
    public Message readMessage() throws Exception {
        
        ByteBuffer buffReadHeader = ByteBuffer.allocate(buffSizeHeader);
        boolean isFinish = false;
        
        //一条消息不能读取时间太长，影响别的消息的读取
        long nowMin = System.currentTimeMillis();
        SocketChannel channel = nioSession.getChannel();
        Message message = null;
        while(!isFinish) {
            try {
                
                //防止读取的报文头不完整
                int size = channel.read(buffReadHeader);
                if (buffReadHeader.position() < buffSizeHeader) {
                    //此处打印当前读取到的数据大小,针对的是关闭主动关闭close 的情况
                    System.out.println("buffReadHeader.position(): " + buffReadHeader.position() + " size: " + size);
                    if (size < 0) {
                    	nioSession.getHandler().exceptionCaught(nioSession, new Exception("remote channel close"));
                    }
                    continue;
                }
                
                //开始读取消息，start方法待后面的具体实现去做实例化
                buffReadHeader.rewind();
                int length = buffReadHeader.getInt();
                char state = buffReadHeader.getChar();
                long sessionId = buffReadHeader.getLong();
                int messageType = buffReadHeader.getInt();
                
                //当获取的消息长度大于最大值时，一般情况下是因为消息格式不完整等错误导致的，此时需要把管道中的数据全部读出并返回错误
                //消息读取异常
                if (length > FastMessage.MAXMESSAGELEN) {
                	ByteBuffer bufException = ByteBuffer.allocate(1024); 
                	while(channel.read(bufException) > 0) {
                		bufException.clear();
                	}
                	System.out.println("Read Exception NioSession: " + this.nioSession + " hostIp: " + channel.socket().getInetAddress().getHostAddress());
                	break;
                }
                
                message = NioMessageFacade.getInstance().getMessage(sessionId);
                if (message == null) {
                    message = new SimpleMessage();
                    message.setSessionId(sessionId);
                    message.setNioSession(nioSession);
                    message.setOperType(messageType);
                    NioMessageFacade.getInstance().addMessage(message);
                }
                
                //此处后面版本考虑如何复用ByteBuffer 出来的内存池
                ByteBuffer messageBody = ByteBuffer.allocate(length);
                
                while (channel.read(messageBody) > 0) {
                    
//                	System.out.println("messageBody.capacity(): " + messageBody.capacity() + " -- messageBody.remaining(): " + messageBody.remaining());
                    
                	if (messageBody.remaining() > 0) {
                        
                        //超过线程读取的持续时间就释放读取线程，防止出现别的客户端长时间等待的情况发生
                        if ((System.currentTimeMillis() - nowMin) > FastMessage.RECEIVEMESSAGETIMEOUT) {
                            //超时时需要中断客户端，防止出现读取消息的异常
                        	System.out.println("messageBody.remaining() TimeOut");
                        	nioSession.getHandler().exceptionCaught(nioSession, new Exception("TimeOutException"));
                            break;
                        }
                        Thread.sleep(FastMessage.RECEIVEMESSAGEWAITTIME);
                        continue;
                    }
                    
                   
                    
                    byte[] byteBody = new byte[length];
                    messageBody.rewind();
                    messageBody.get(byteBody);
                    messageBody.clear();
                    
                    message.addMessageFragment(byteBody);
                    //一次消息体读取完成后，跳出，重新读取下一个消息体
                    break;
                }
                
                
                
                
                
                //消息的最后设置消息的状态为可以读取的状态
                if (FastMessage.MESSAGEEND == state) {
                    message.setReady(true);
                    break;
                }
                
                //超过线程读取的持续时间就释放读取线程，防止出现别的客户端长时间等待的情况发生
                if ((System.currentTimeMillis() - nowMin) > FastMessage.RECEIVEMESSAGETIMEOUT) {
                    //超时时需要中断客户端，防止出现读取消息的异常
                	nioSession.getHandler().exceptionCaught(nioSession, new Exception("TimeOutException"));
                    break;
                }
                Thread.sleep(20);
                
            }
            catch (Exception e) {
              //消息读取异常时，需要退出循环，并且需要做特殊的处理
                isFinish = true;
                throw e;
            }
            
            //在一条信息被多个消息中断读取等待时有效
            buffReadHeader.rewind();
            buffReadHeader.clear();
            
        }
        return message;
    }
    
    
    public void closeSession() {
            try {
            	nioSession.getHandler().exceptionCaught(nioSession, new Exception("Force destory"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
    }

}
