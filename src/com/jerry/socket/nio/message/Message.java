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
    
    /***
     * 表示消失是否已经处理完成，当客户端表示标示消息已经完成后，消息将被设置为已经完成，此时就可以清除了
     * @return 是否已经完成
     */
    public boolean isComplete();
    
    public void setComplete(boolean isComplete);
    
    
    public int getInvalidTime();
    
    /**
     * 设置失效时间，默认是30000毫秒
     * @param invalidTime
     */
    public void setInvalidTime(int invalidTime);
    
    /**
     * 消息是否失效，false 否，true 失效,有两种状态下会被设置为失效状态，1 用户前置设置，2 消息存活时间超过规定的超时时间
     * @return 是否失效
     */
    public boolean isInvalid();
    
    public boolean setInvalid(boolean isInvalid);
    
    public void addMessageFragment(byte[] fragment);
    
    public byte[] getMessageBody();
    
    public boolean isReady();
    
    public void setReady(boolean ready);
    
    public void setOperType(int type);
    
    public int getOperType();
    
}
