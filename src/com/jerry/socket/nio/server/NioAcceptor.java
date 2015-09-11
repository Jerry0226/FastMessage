package com.jerry.socket.nio.server;

import java.io.IOException;
import java.net.SocketAddress;

import com.jerry.socket.nio.INioFastSocket;


/**
 * fastMessage的服务端，因为消息量的问题，当前所有的连接都只注册在一个selector上，
 * 后面可以考虑注册多个selector，对每一个selector进行监听，接口层面先给出定义
 * @author chm
 *
 */
public interface NioAcceptor extends INioFastSocket {

    /**服务端绑定地址*/
    public void bind(SocketAddress localAddress) throws IOException;
    
    /**服务端绑定地址*/
    public void bind(int port) throws IOException;
    
    
    
    
    
}
