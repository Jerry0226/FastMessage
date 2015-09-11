package com.jerry.socket.nio.message.wrapper;

import com.jerry.socket.nio.message.Message;

/**
 * 默认直接返回消息内容本身
 * @author chm
 *
 */
public class NioMessageWrapper implements INioMessageWrapper {

    public Object dealWithMessage(Message message) {
        return message;
    }
    
}
