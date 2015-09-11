package com.jerry.socket.nio.common;

import java.nio.channels.SocketChannel;

public final class Dustman {
    /**
     * 构造方法
     */
    private Dustman() {
    }
    
    public static void close(SocketChannel channel) {
        try {
            if (channel!= null && channel.isOpen()) {
                channel.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
