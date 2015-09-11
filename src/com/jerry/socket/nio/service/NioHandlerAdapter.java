package com.jerry.socket.nio.service;

import com.jerry.socket.nio.session.NioSession;

public class NioHandlerAdapter implements NioHandler{

    @Override
    public void exceptionCaught(NioSession session, Throwable cause) throws Exception {
            System.out.println("EXCEPTION, please implement " + getClass().getName()
                    + ".exceptionCaught() for proper handling:" +  cause);
    }

    @Override
    public void sessionClosed(NioSession session) throws Exception {
        
    }

    @Override
    public void sessionOpened(NioSession session) throws Exception {
        System.out.println(session.getSessionId() + " is accepted");
    }

}
