package com.jerry.socket.nio;

public class FastSocketConstants {
	
	/**长度，状态('0' 未完成，'1'，完成)，sessionId, messageType*/
	public static final int HEADSIZE = 4 + 2 + 8 + 4;
}
