package com.jerry.socket.nio.message.transfer;
/**
 * 消息的格式，长度、状态、ID、BODY,长度是四个字节表示的整型，
 * ID 是此消息的消息信息,此处的消息长度是指body的长度,状态是针对分包的情况
 * @author chm
 *
 */
public interface PackMessage {

    /**
     * 打包消息，把消息体上增加长度，状态，id
     * @param length 消息体的长度
     * @param state 状态
     * @param session 消息的id，消息的唯一标示
     * @param messageType 消息类型
     * @return byte[] 返回打包后的消息
     */
    public byte[] packMessage(int length, char state, long session,int messageType, byte[] body);
}
