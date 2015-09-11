package com.jerry.socket.nio.message.codec;

import com.jerry.socket.nio.message.Message;

public interface ProtocolEncoder {
    void encode(Message message) throws Exception;
}
