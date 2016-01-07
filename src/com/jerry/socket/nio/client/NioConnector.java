package com.jerry.socket.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.jerry.socket.nio.INioFastSocket;
import com.jerry.socket.nio.session.NioSession;

public interface NioConnector extends INioFastSocket {
    public void connect(String ip, int port)  throws IOException, InterruptedException;
    public void connect(InetSocketAddress address)  throws IOException, InterruptedException;
    public NioSession getNioSession();
    public void setConnectTimeOut(long timeOut);
    public long getConnectTimeOut();
    public void destory();
}
