package com.jerry.socket.nio.message.filterchain;

import java.util.LinkedList;

import com.jerry.socket.nio.message.codec.ProtocolCodecFactory;

/**
 * 消息过滤链
 * @author chm
 *
 */
public interface INioFilterChain {

    public void addFirst(ProtocolCodecFactory protocol);
    
    public void addLast(ProtocolCodecFactory protocol);
    
    public void addAfter(ProtocolCodecFactory protocol);
    
    public LinkedList<ProtocolCodecFactory> getFilterChain();
}
