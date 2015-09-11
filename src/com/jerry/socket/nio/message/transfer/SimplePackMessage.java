package com.jerry.socket.nio.message.transfer;

import com.jerry.socket.nio.common.NioUtil;

public class SimplePackMessage implements PackMessage {

    /**
     * 打包消息，把消息体上增加长度，状态，id
     * @param length 消息体的长度
     * @param state 状态
     * @param session 消息的id，消息的唯一标示
     * @param messageType 消息类型
     * @return byte[] 返回打包后的消息
     */
    public byte[] packMessage(int length, char state, long session,int messageType, byte[] body) {
        byte[] messageByte = new byte[4 + 2 + 8 + 4 + body.length];
        System.arraycopy(NioUtil.intToByteArray(length), 0, messageByte, 0, 4);
        System.arraycopy(NioUtil.charToByte(state), 0, messageByte, 4, 2);
        System.arraycopy(NioUtil.long2Bytes(session), 0, messageByte, 6, 8);
        System.arraycopy(NioUtil.intToByteArray(messageType), 0, messageByte, 14, 4);
        System.arraycopy(body, 0, messageByte, 18, body.length);
        return messageByte;
    }
    
}
