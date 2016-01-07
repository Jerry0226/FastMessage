package com.jerry.socket.nio.message;

public final class NioMessageFacade {

    private MessageList<Message> messageList = new SimpleMessageList<Message>(); 
    
    public MessageList<Message> getMessageList() {
        return messageList;
    }
    
    public NioMessageFacade() {
    	Thread td = new Thread(new MessageMonitor());
        td.setName("MessageList_Clean_Thread");
        //设置为后台进程，使得在主进程结束后可以自动退出
        td.setDaemon(true);
        td.start();
    }
    
    class MessageMonitor implements Runnable {
    	 /**
         * 接口方法
         */
        public void run() {
            while (true) {
                try {
                    for (Message message : messageList.getMessageList()) {
                        if (message.isInvalid() || message.isComplete()) {
                            messageList.getMessageList().remove(message);
                            System.out.println("Clean Message: " + message);
                        }
                    }
                    Thread.sleep(MONITORSPANTIME);
                }
                catch (InterruptedException e) {
                    System.out.println("监控失效对象删除失败");
                }
            }
        }
    }

    
    /**监控扫描时间间隔*/
    private final int MONITORSPANTIME = 1000;
    
    public synchronized boolean addMessage(Message e) {
        return messageList.addMessageList(e);
    }
    
    public synchronized Message getMessage(long sessionId) {
    	Message message = messageList.getMessage(sessionId);
        return message;
    }
    
    
    /**
     * 获取消息后把消息包同时删除，同时此处返回的消息必须是已经完成的消息，ready = true
     * @param sessionId 会话id
     * @return 消息包Message 对象
     */
    public synchronized Message getAndDelMessage(long sessionId) {
    	
    	Message message = messageList.getMessage(sessionId);
    	if (message != null && message.isReady()) {
    		boolean isexsit = messageList.getMessageList().remove(message);
    		if (isexsit) {
    			return message;
    		}
    		return null;
    	}
    	return null;
    }
    
    public synchronized boolean cleanMessage(Message e) {
        return messageList.cleanMessage(e);
    }
    
    
}
