package com.jerry.socket.nio.message.codec;

import com.jerry.socket.nio.message.Message;

public interface ProtocolDecoder {
    void decode(Message message)  throws Exception;
}
