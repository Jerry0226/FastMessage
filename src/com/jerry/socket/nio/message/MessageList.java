package com.jerry.socket.nio.message;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface MessageList <E extends Message> {

    public boolean addMessageList(E e);
    
    public E getMessage(long sessionId);
    
    public boolean cleanMessage(E e);
    
    public ConcurrentLinkedQueue<E> getMessageList();
    
}
