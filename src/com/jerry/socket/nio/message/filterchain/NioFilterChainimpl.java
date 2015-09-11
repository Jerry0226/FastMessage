package com.jerry.socket.nio.message.filterchain;

import java.util.LinkedList;

import com.jerry.socket.nio.message.codec.ProtocolCodecFactory;

public class NioFilterChainimpl implements INioFilterChain {

    private LinkedList<ProtocolCodecFactory> filterChainList = new LinkedList<ProtocolCodecFactory>();
    
    public void addAfter(ProtocolCodecFactory protocol) {
        filterChainList.add(protocol);
    }

    public void addFirst(ProtocolCodecFactory protocol) {
        filterChainList.addFirst(protocol);
    }

    public void addLast(ProtocolCodecFactory protocol) {
        filterChainList.addLast(protocol);
    }
    
    public LinkedList<ProtocolCodecFactory> getFilterChain() {
        return filterChainList;
    }

}
