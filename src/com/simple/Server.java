package com.simple;

import java.io.IOException;

import com.jerry.socket.nio.message.Message;
import com.jerry.socket.nio.message.NioMessageFacade;
import com.jerry.socket.nio.server.NioAcceptor;
import com.jerry.socket.nio.server.NioSocketAcceptor;

public class Server {
	public static void main(String[] args) throws IOException, InterruptedException {
		NioAcceptor nioAcc = new NioSocketAcceptor();
		nioAcc.bind(2289);
		NioMessageFacade nioMessFacade = nioAcc.getNioMessFacade();
		while(true) {
			Thread.sleep(100);
			for(Message message: nioMessFacade.getMessageList().getMessageList()) {
				System.out.println("客户端消息： " + new String(message.getMessageBody(), "utf-8"));
				message.getNioSession().asyncSendMessage("异步推送消息: " + new String(message.getMessageBody(),"utf-8"), 1, message.getSessionId());
				nioMessFacade.cleanMessage(message);
			}
		}
		
	}
}
