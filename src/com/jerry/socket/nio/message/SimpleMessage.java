package com.jerry.socket.nio.message;

import com.jerry.socket.nio.session.NioSession;


public class SimpleMessage implements Message {

    private int operType = -1;
    
    public long sessionId;
    
    
    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long createTime = System.currentTimeMillis();
    
    private long endTime ;
    
    /**消息是否完成，用户已经读取了消息*/
    private boolean isComplete;
    
    
    /**消息的失效时间，现在规定在 30秒，单位毫秒,在这么长的时间内没有被处理就认为失效*/
    private int invalidTime = 30000;

    public int getInvalidTime() {
		return invalidTime;
	}

	public void setInvalidTime(int invalidTime) {
		this.invalidTime = invalidTime;
	}

	private NioSession nioSession;

    /**
     * 消息是否失效
     */
	private boolean isInvalid = false;
	
    
    private byte[] messageBody ;
    
    public byte[] getMessageBody() {
        return messageBody;
    }

    private int offset = 0;

    public int getOffset() {
        return offset;
    }

    /**消息已经接收完成*/
    private boolean isReady;
    
    public boolean isComplete() {
        return isComplete;
    }


    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }


    /**
     * 消息是否失效，false 否，true 失效
     * @return 是否失效
     */
    public boolean isInvalid() {
        if ((System.currentTimeMillis() - createTime) > invalidTime || this.isInvalid) {
            return true;
        }
        return false;
    }
    

//    此处是处理不定长的消息日志
    /**
     * 增加消息片段
     */
    public void addMessageFragment(byte[] fragment) {
        
        if (messageBody == null) {
            messageBody = fragment;
            offset = fragment.length;
        }
        else {
            increaseLength(fragment.length);
            System.arraycopy(fragment, 0, this.messageBody, offset, fragment.length);
            offset = offset + fragment.length;
        }
        
    }
    
    
    /**
     * 当接收的数据超过此最大范围时，需要增加集合的大小
     * @param fragmentLength
     */
    public void increaseLength(int fragmentLength) {
        
        byte[] currentMessageBody = new byte[ offset + fragmentLength];
        System.arraycopy(this.messageBody, 0, currentMessageBody, 0, messageBody.length);
        this.messageBody = currentMessageBody;
        
    }

    public boolean isReady() {
        return this.isReady;
    }

    public void setReady(boolean ready) {
        this.isReady = ready;
        this.endTime = System.currentTimeMillis();
    }
    
    /***
     * 读取本次消息的耗时
     * @return 大于零是正常的消息，小于零为异常消息
     */
    public long durationTime() {
        return this.endTime - this.createTime;
    }

    public int getOperType() {
        return this.operType;
    }

    public void setOperType(int type) {
        this.operType = type;
    }

    @Override
    public String toString() {
        return "sessionId: " + sessionId + ", operType: " + this.operType + ", length: " + this.offset;
    }
    
    @Override
    public NioSession getNioSession() {
        return nioSession;
    }

    @Override
    public void setNioSession(NioSession nioSession) {
        this.nioSession = nioSession;        
    }

	@Override
	public boolean setInvalid(boolean isInvalid) {
		return this.isInvalid  = isInvalid;
	}

}
