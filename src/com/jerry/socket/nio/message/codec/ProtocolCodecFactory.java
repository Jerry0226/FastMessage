package com.jerry.socket.nio.message.codec;

import com.jerry.socket.nio.session.NioSession;

/**
 * 编解码器，一般是配对出现一个编码器一个解码器
 * @author chm
 * 
 */
public interface ProtocolCodecFactory {
    
    /**
     * return encoder
     * */
    ProtocolEncoder getEncoder(NioSession session) throws Exception;
    
    /**
     * return decoder
     * */
    ProtocolDecoder getDecoder(NioSession session) throws Exception;
}
