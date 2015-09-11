package com.jerry.socket.nio.message;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SimpleMessageList<E extends Message> implements MessageList<E> {
    
    ConcurrentLinkedQueue<E> messageListCurrent = new ConcurrentLinkedQueue<E>();
    
    public boolean addMessageList(E e) {
        return messageListCurrent.add(e);
    }

    public boolean cleanMessage(E e) {
        return messageListCurrent.remove(e);
    }

    public E getMessage(long sessionId) {
        for (E message : messageListCurrent) {
            if (message.getSessionId() == sessionId) {
                return (E) message;
            }
        }
        return null;
    }

    public ConcurrentLinkedQueue<E> getMessageList() {
        return messageListCurrent;
    }

}
