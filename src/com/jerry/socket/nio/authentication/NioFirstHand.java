package com.jerry.socket.nio.authentication;
/**
 * 首次握手的接口，用来对客户端做验证操作，如果验证通过那么返回true,
 * 返回false 时，服务端将直接删除客户端
 * @author chm
 *
 */
public interface NioFirstHand {

    public long authClientSession(Object message);
}
