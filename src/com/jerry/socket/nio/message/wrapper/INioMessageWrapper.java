package com.jerry.socket.nio.message.wrapper;

import com.jerry.socket.nio.message.Message;

/**
 * 
 * 针对不同的消息类型进行处理，返回给定的消息内容
 * @author chm
 * 
 */
public interface INioMessageWrapper {
    public Object dealWithMessage(Message message);
}
