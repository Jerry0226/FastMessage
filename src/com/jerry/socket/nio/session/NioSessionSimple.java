package com.jerry.socket.nio.session;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class NioSessionSimple extends NioSession {
    
    public NioSessionSimple(SocketChannel channel, long sessionId,Selector selector) {
        super(channel, sessionId,selector);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof NioSession) {
            NioSession dailyObj = (NioSession) obj;
            return dailyObj.getSessionId() == this.getSessionId();
        }
        else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    public String toString() {
        return "SessionId: " + this.getSessionId() + "--" + getChannel().socket().getInetAddress() + "--"
            + getChannel().socket().getPort();
    }
    
}
