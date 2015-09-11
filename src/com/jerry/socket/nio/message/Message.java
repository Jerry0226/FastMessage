package com.jerry.socket.nio.message;

import com.jerry.socket.nio.session.NioSession;

/**
 * @author chm
 */
public interface Message {
    
    public void setNioSession(NioSession nioSession);
    
    public NioSession getNioSession();
    
    public void setSessionId(long sessionId);
    
    public long getSessionId();
    
    public boolean isTimeOut();
    
    public void setTimeOut(boolean timeOut);
    
    /***
     * 表示消失是否已经处理完成，当客户端表示标示消息已经完成后，消息将被设置为已经完成，此时就可以清除了
     * @return 是否已经完成
     */
    public boolean isComplete();
    
    public void setComplete(boolean isComplete);
    
    public boolean isInvalid();
    
    public void addMessageFragment(byte[] fragment);
    
    public byte[] getMessageBody();
    
    public boolean isReady();
    
    public void setReady(boolean ready);
    
    public void setOperType(int type);
    
    public int getOperType();
    
}
