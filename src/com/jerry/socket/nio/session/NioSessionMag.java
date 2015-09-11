package com.jerry.socket.nio.session;

import java.util.concurrent.ConcurrentLinkedQueue;


public final class NioSessionMag {

    private NioSessionMag(){
    }
    
    private static NioSessionMag instance = new NioSessionMag();
    
    private ConcurrentLinkedQueue<NioSession> agentList = new ConcurrentLinkedQueue<NioSession>();
    
    public static NioSessionMag getInstance() {
        if (instance == null) {
            instance = new NioSessionMag();
        }
        return instance;
    }
    
    public ConcurrentLinkedQueue<NioSession> getAgentList() {
        return agentList;
    }
    
    public boolean addAgent(NioSession agentDto) {
        return agentList.add(agentDto);
    }
    
    public NioSession getNioClient(long sessionId) {
        for(NioSession client : agentList) {
            if (client.getSessionId() == sessionId) {
                return client;
            }
        }
        return null;
    }
    
    public void clear() {
        
        for(NioSession client : agentList) {
            try {
                client.destory();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
//    public NioSession getNioClient(SocketChannel socketChannl) {
//        for(NioSession client : agentList) {
//            if (socketChannl.equals(client.getChannel())) {
//                return client;
//            }
//        }
//        return null;
//    }
    
    
    /**
     * 删除已经关闭的客户端
     * @param nioSession 客户端 
     */
    public void deleteNioSession(NioSession nioSession) {
        agentList.remove(nioSession);
    }
    
}
