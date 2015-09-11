package com.jerry.socket.nio.service;

import java.io.IOException;


import com.jerry.socket.nio.session.NioSession;


public interface NioHandler {
    void sessionOpened(NioSession session) throws Exception;

    /**
     * Invoked when a connection is closed.
     */
    void sessionClosed(NioSession session) throws Exception;
    
    
    /**
     * Invoked when any exception is thrown by user {@link IoHandler}
     * implementation or by jerry.  If <code>cause</code> is an instance of
     * {@link IOException}, jerry will close the connection automatically.
     */
    void exceptionCaught(NioSession session, Throwable cause) throws Exception;

}
