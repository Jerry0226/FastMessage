package com.jerry.socket.nio.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.jerry.socket.nio.message.wrapper.INioMessageWrapper;
import com.jerry.socket.nio.read.SingleMessageRead;
import com.jerry.socket.nio.service.NioHandler;
import com.jerry.socket.nio.service.NioHandlerAdapter;
import com.jerry.socket.nio.session.NioSession;
import com.jerry.socket.nio.session.NioSessionSimple;

public class NioSocketConnector implements NioConnector {
    
    NioHandler niohandler = null;
    
    @Override
    public NioHandler getHandler() {
        if (niohandler == null) {
            niohandler = new NioHandlerAdapter();
        }
        return niohandler;
    }

    @Override
    public void setHandler(NioHandler niohandler) {
        this.niohandler = niohandler;
    }


    static final int HEARTTIME = 1000;

    SocketChannel channel;

    Selector sel;

    Selector selRead;

    NioSession nioSession;
    
    private long timeOut = 0l;

    public void setConnectTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }
    
    public long getConnectTimeOut() {
        return this.timeOut;
    }

    int poolThreadCount = 0;

    public void connect(InetSocketAddress address) throws IOException, InterruptedException {

        channel = SocketChannel.open();
        sel = Selector.open();
        selRead = Selector.open();
        channel.configureBlocking(false);
        channel.register(sel, SelectionKey.OP_CONNECT);
        channel.connect(address);
        awaitUninterruptibly();
        ReadMonitor sm = new ReadMonitor();
        new Thread(sm).start();

    }

    private void awaitUninterruptibly() throws IOException {
        boolean done = false;
        while (!done) {
            sel.select();
            Iterator<?> it = sel.selectedKeys().iterator();
            while (it.hasNext()) {

                SelectionKey key = (SelectionKey) it.next();
                it.remove();
                // 获取创建通道选择器事件键的套接字通道
                channel = (SocketChannel) key.channel();

                if ((key.readyOps() & SelectionKey.OP_CONNECT) == SelectionKey.OP_CONNECT) {
                    
                    waitConnection(this.timeOut, channel);

                    while (true) {
                        if (channel.isConnected()) {
                            channel.configureBlocking(false);// 设置成非阻塞
                            
                            //创建nioSession 对象
                            long sessionId = System.currentTimeMillis();
                            nioSession = new NioSessionSimple(channel, sessionId, selRead);
                            nioSession.setHandler(getHandler());
                            try{
                            	nioSession.getHandler().sessionOpened(nioSession);
                            }catch (Exception e) {
                            	e.printStackTrace();
							}
                            nioSession.setClientIp(channel.socket().getInetAddress().getHostAddress());
                            // 连接成功后，退出等待，并把连接注册到读取的渠道中
                            done = true;
                            selRead.wakeup();
                            channel.register(selRead, SelectionKey.OP_READ, nioSession);
                            break;
                        }
                    }

                }

            }

        }
    }
    
    /**
     * 等待消息连接的完成，防止异步监听
     * @param timeOut 超时时间
     * @param channel 渠道
     * @throws IOException 异常消息
     */
    private void waitConnection(long timeOut, SocketChannel channel) throws IOException {
        long beginTime = System.currentTimeMillis();
        boolean isWait = true;
        long waitTime = timeOut/10;
        while(isWait) {
            try {
                if (channel.isConnectionPending()){
                    channel.finishConnect();
                }
                isWait = false;
            }
            catch (IOException e) {
            	//连接异常时，需要关闭相关的文件操作句柄，如channel，selector等
                if (timeOut == 0l||(System.currentTimeMillis() - beginTime) > (timeOut)) {
                	close();
                    throw e;
                }
            }
            try {
                Thread.sleep(waitTime);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }

    /**
     * 内容读取
     * @author chm
     *
     */
    class ReadMonitor implements Runnable {
        
        boolean isContinue = true;

        public void run() {
            try {
            while (isContinue) {
                
                    // 如果选择器在指定时间内没有任何返回那么说明主控已经关闭
                    selRead.select();
                    Iterator<?> it = selRead.selectedKeys().iterator();
                    while (it.hasNext()) {
                        
                        // 获取通道选择器事件键
                        SelectionKey skey = (SelectionKey) it.next();
                        NioSession nioSession = (NioSession)skey.attachment();
                        it.remove();
                        if (skey.isReadable()) {
                            // 每次发生消息read事件时，需要把选择器中此管道的读取时间取消掉，当本次读取完成后，重新进行注册read 时间监听
                            nioSession.setSkey(skey);
                            nioSession.getChannel().register(selRead, skey.interestOps() & (~SelectionKey.OP_READ), nioSession);
                            
                            //为了跳出监控，不直接调用线程执行
                            SingleMessageRead single = new SingleMessageRead(nioSession);
                            single.readMessage();
                            
                            //增加对read 事件的监听
                            nioSession.getSelector().wakeup();
                            nioSession.getChannel().register(nioSession.getSelector(), nioSession.getSkey().interestOps()|SelectionKey.OP_READ, nioSession);
                            
                        }
                    }
                }
            }
            catch (Exception e) {
                //此处增加消息异常的对外接口
//                e.printStackTrace();
                isContinue = false;
                try {
                	e.printStackTrace();
                    nioSession.getHandler().exceptionCaught(nioSession, e);
                    nioSession.destory();
                }
                catch (Exception e1) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void connect(String ip, int port) throws IOException, InterruptedException {
        InetAddress address = InetAddress.getByName(ip);
        connect(new InetSocketAddress(address, port));
    }


    public int getReadMessageThreadCount() {
        if (poolThreadCount <= 0) {
            return 1;
        }
        return poolThreadCount;
    }

    public void setNioMessageWrapper(INioMessageWrapper message) {

    }

    public void setReadMessageThreadCount(int threadCount) {
        this.poolThreadCount = threadCount;
    }


    public NioSession getNioSession() {
        return nioSession;
    }

	public void close() {
		try {
			if (channel != null) {
				this.channel.close();
				this.channel = null;
			}
			if(sel != null) {
				sel.close();
				sel = null;
			}
			if(selRead != null) {
				selRead.close();
				selRead = null;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}


}
