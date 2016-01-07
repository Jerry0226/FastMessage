package com.simple;

import java.io.IOException;

import com.jerry.socket.nio.client.NioConnector;
import com.jerry.socket.nio.client.NioSocketConnector;
import com.jerry.socket.nio.message.Message;
import com.jerry.socket.nio.session.NioSession;

public class Client {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Client cl = new Client();
		cl.start();
		
	}
	
	public void start() {
		for(int i=0;i<5;i++) {
			ClientThread ct = new ClientThread(i * 30);
			new Thread(ct).start();
		}
	}
	
	class ClientThread implements Runnable {

		int count =0;
		public ClientThread(int count) {
			this.count = count;
		}
		
		@Override
		public void run() {
			try {
				NioConnector nio = new NioSocketConnector();
				nio.connect("localhost", 2289);
				NioSession session = nio.getNioSession();
				
				for(int i =0;i<10;i++) {
					System.out.println("发送消息...");
					Message message = (Message)session.syncSendMessage("同步推送消息: " + (count + i), 1);
					System.out.println("服务端返回的消息： " + new String(message.getMessageBody(), "utf-8"));
				}
				System.out.println("destory");
				nio.destory();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
