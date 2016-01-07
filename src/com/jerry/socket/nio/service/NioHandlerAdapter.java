package com.jerry.socket.nio.service;

import com.jerry.socket.nio.session.NioSession;

public class NioHandlerAdapter implements NioHandler{

    @Override
    public void exceptionCaught(NioSession session, Throwable cause) throws Exception {
    	
    	//如果客户端主动的关闭连接，服务端需要取消其channel key的监听，否则将服务端的selector
    	//将一直受到此关闭消息
    	
    	/**
    	 * if (skey != null) {
                skey.cancel();
            }
            
            if (channel != null) {
                channel.close();
            }
    	 */
//    		session.destory();
            System.out.println("EXCEPTION, please implement " + getClass().getName()
                    + ".exceptionCaught() for proper handling:" +  cause);
    }

    @Override
    public void sessionClosed(NioSession session) throws Exception {
        System.out.println(session + " is close!");
    }

    @Override
    public void sessionOpened(NioSession session) throws Exception {
        System.out.println(session.getSessionId() + " is accepted");
    }

}
