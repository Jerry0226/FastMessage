package com.jerry.socket.nio;

import com.jerry.socket.nio.message.wrapper.INioMessageWrapper;
import com.jerry.socket.nio.service.NioHandler;

public interface INioFastSocket {
    
    public void setNioMessageWrapper(INioMessageWrapper message);
    
    /**设置每一个selector消息读取线程池的线程数量，默认为cpu个数*2*/
    public void setReadMessageThreadCount(int threadCount);
    
    /**返回read message thread count 的个数*/
    public int getReadMessageThreadCount();
    
    public void setHandler(NioHandler niohandler);
    
    public NioHandler getHandler();
}
