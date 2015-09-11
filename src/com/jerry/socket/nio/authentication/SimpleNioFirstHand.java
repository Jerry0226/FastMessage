package com.jerry.socket.nio.authentication;

public class SimpleNioFirstHand implements NioFirstHand {

    /**
     * 默认的实现方式，如果用户不对实现此方法，得到对方的id，直接连接上
     */
    public long authClientSession(Object message) {
        if (message == null) {
            return System.currentTimeMillis();
        }
        return -1;
    }

}
