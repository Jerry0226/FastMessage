package com.jerry.socket.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.jerry.socket.nio.authentication.NioFirstHand;
import com.jerry.socket.nio.authentication.SimpleNioFirstHand;
import com.jerry.socket.nio.common.FastMessage;
import com.jerry.socket.nio.message.wrapper.INioMessageWrapper;
import com.jerry.socket.nio.message.wrapper.NioMessageWrapper;
import com.jerry.socket.nio.read.SingleMessageRead;
import com.jerry.socket.nio.service.NioHandler;
import com.jerry.socket.nio.service.NioHandlerAdapter;
import com.jerry.socket.nio.session.NioSession;
import com.jerry.socket.nio.session.NioSessionMag;
import com.jerry.socket.nio.session.NioSessionSimple;

public class NioSocketAcceptor implements NioAcceptor {
    
    NioHandler niohandler = null;

    private AtomicLong sessionGet = new AtomicLong(1l);

    /** 单个selector上的消息内容读取的线程数 */
    private int readMessageThreadCount = FastMessage.DEFAULTREADTHREADCOUNT;


    /** 首次会话，用来做握手验证，默认直接返回成功，用户需要根据自己的需要进行验证操作 */
    private NioFirstHand firstSession = new SimpleNioFirstHand();

    public NioFirstHand getFirstSession() {
        return firstSession;
    }

    public void setFirstSession(NioFirstHand firstSession) {
        this.firstSession = firstSession;
    }

    public static AtomicBoolean isWakeUped = new AtomicBoolean(false);

    /**
     * 消息的处理方式，默认不做任何处理直接返回，用户需要对此进行有效的扩展
     */
    private INioMessageWrapper nioMegWrapper = new NioMessageWrapper();

    public INioMessageWrapper getNioMegAction() {
        return nioMegWrapper;
    }

    public void setNioMessageWrapper(INioMessageWrapper messageWrapper) {
        this.nioMegWrapper = messageWrapper;

    }

//    Selector readSelector;

    Selector serverSelector;

    public void bind(SocketAddress localAddress) throws IOException {
        serverSelector = Selector.open();

        // 打开服务端的套接字通道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 打开通道选择器

        // 将服务端套接字通道绑定到本机服务端端口
        ssc.socket().bind(localAddress);

        // 将服务端套接字通道连接方式调整为非阻塞模式
        ssc.configureBlocking(false);

        // 将服务端套接字通道OP_ACCEP事件注册到通道选择器上
        ssc.register(serverSelector, SelectionKey.OP_ACCEPT);

        new Thread(new ServerMonitor(ssc)).start();
//        System.out.println("ssc: " + ssc.socket().getLocalPort() + ",ssc.validOps(): " + ssc.validOps());

    }

    class ServerMonitor implements Runnable {

        // 设置读取消息的线程大小，根据外面的定义来实现
        private ThreadPoolExecutor poolStartRead = (ThreadPoolExecutor) Executors
            .newFixedThreadPool(getReadMessageThreadCount());

        // 设置读取消息的线程大小，根据外面的定义来实现
//        private ThreadPoolExecutor poolStartAccept = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        private ServerSocketChannel ssc;

        public ServerMonitor(ServerSocketChannel ssc) {
            this.ssc = ssc;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // 通道选择器开始轮询通道事件
                    int read = serverSelector.select(100);
                    if (read <= 0) {
                        continue;
                    }
                    Iterator<?> it = serverSelector.selectedKeys().iterator();
                    while (it.hasNext()) {
                        SelectionKey skey = (SelectionKey) it.next();
                        // 获取通道选择器事件键
                        it.remove();
                        if (skey.isAcceptable()) {
                            SocketChannel ch = null;
                            ch = ssc.accept();
                            ch.configureBlocking(false);
                            ch.socket().setReuseAddress(true);

                            // 注册得到客户端连接的session
                            long sessionId = sessionGet.getAndIncrement();
                            NioSession nioSession = new NioSessionSimple(ch, sessionId, serverSelector);
                            nioSession.setHandler(getHandler());
                            nioSession.setClientIp(ch.socket().getInetAddress().getHostAddress());
                            NioSessionMag.getInstance().addAgent(nioSession);
                            
                            //当连接已经连接后的回调函数
                            nioSession.getHandler().sessionOpened(nioSession);
                            
                            System.out.println("nioSession : " + nioSession.getSessionId() + " hostIp: " + ch.socket().getInetAddress().getHostAddress() + " accept....");

                            ch.register(serverSelector, SelectionKey.OP_READ, nioSession);
//                            poolStartAccept.execute(new StartRegRead(ch, nioSession));

                        }
                        if (skey.isReadable()) {

                            NioSession nioSession = (NioSession) skey.attachment();
                            if (skey.isReadable()) {
                                try {
                                    System.out.println("read server message......");
                                    nioSession.setSkey(skey);
                                    nioSession.getChannel().register(serverSelector,
                                        skey.interestOps() & (~SelectionKey.OP_READ), nioSession);
                                    SingleMessageRead single = new SingleMessageRead(nioSession);
                                    poolStartRead.execute(single);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                    nioSession.destory();
                                }

                            }
                        }
                    }
                }
            }
            catch (Exception e) {
                System.out.println("服务端监听异常");
                //出现异常时，清空所有的连接
                NioSessionMag.getInstance().clear();
                e.printStackTrace();
            }

        }

    }
/**
    class StartRegRead implements Runnable {

        private SocketChannel ch;

        private NioSession nioSession;

        public StartRegRead(SocketChannel ch, NioSession nioSession) {
            this.ch = ch;
            this.nioSession = nioSession;
        }

        @Override
        public void run() {

            try {
//                System.out.println("register read begin niosession  " + nioSession.getSessionId());
                ch.register(serverSelector, SelectionKey.OP_READ, nioSession);
//                System.out.println("register read end niosession " + nioSession.getSessionId());
            }
            catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        }

    }

*/
    public void bind(int port) throws IOException {
        bind(new InetSocketAddress(port));
    }

    public int getReadMessageThreadCount() {
        if (readMessageThreadCount <= 0) {
            return Runtime.getRuntime().availableProcessors() * 2 + 1;
        }
        return readMessageThreadCount;
    }


    public void setReadMessageThreadCount(int threadCount) {
        this.readMessageThreadCount = threadCount;
    }


    /***
     * 服务端的监听操作
     * 
     * @author chm
     */
    /**
    class ReadMonitor implements Runnable {

        // 设置读取消息的线程大小，根据外面的定义来实现
        private ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors
            .newFixedThreadPool(getReadMessageThreadCount());

        public void run() {
            try {
                while (true) {
                    readSelector.select();
                    Iterator<?> it = readSelector.selectedKeys().iterator();
                    while (it.hasNext()) {

                        // 获取通道选择器事件键
                        SelectionKey skey = (SelectionKey) it.next();
                        it.remove();
                        System.out.println("read server message ddddddddddddddddddddddddddddd");
                        switch (skey.interestOps()) {
                            case SelectionKey.OP_READ:
                                NioSession nioSession = (NioSession) skey.attachment();
                                if (skey.isReadable()) {
                                    try {
                                        System.out.println("read server message......");
                                        nioSession.setSkey(skey);
                                        readSelector.wakeup();
                                        nioSession.getChannel().register(readSelector,
                                            skey.interestOps() & (~SelectionKey.OP_READ), nioSession);
                                        SingleMessageRead single = new SingleMessageRead(nioSession);
                                        pool.execute(single);
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                        nioSession.destory();
                                    }

                                }
                        }
                    }
                }
            }
            catch (Exception e) {
                // 存在异常时退出整个服务端，并提醒
                // TODO: handle exception
            }

        }

    }
*/

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

}
