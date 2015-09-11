package com.jerry.socket.nio.session;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

//import com.test.socket.nio.message.Message;
import com.jerry.socket.nio.message.filterchain.INioFilterChain;
import com.jerry.socket.nio.service.NioHandler;

/**
 * @author chm
 *
 */
public interface INioSession {

    public void asyncSendMessage(Object obj,char state,int operType) throws IOException, InterruptedException;
    
    public void asyncSendMessage(Object obj,int operType) throws IOException, InterruptedException;
    
    public void asyncSendMessage(Object obj,char state,int operType, long sessionId) throws IOException, InterruptedException;
    
    public Object syncSendMessage(Object obj,int operType) throws IOException, InterruptedException;
    
    public Object syncSendMessage(Object obj,char state,int operType) throws IOException, InterruptedException;
    
    public Object syncSendMessage(Object message,int operType, long sessionId) throws IOException, InterruptedException;
    
    public Object syncSendMessage(Object message,char state,int operType, long sessionId) throws IOException, InterruptedException;
    
    public INioFilterChain getFilterChain();
    
    public void setFilterChain(INioFilterChain nioFilterChain);
    
    public SelectionKey getSkey() ;

    public void setSkey(SelectionKey skey) ;
    
    public void setSelector(Selector selector);
    
    public Selector getSelector();
    
    public void destory()throws IOException;
    
    public boolean isVaild();
    
    public void setHandler(NioHandler niohandler);
    
    public NioHandler getHandler();
}
