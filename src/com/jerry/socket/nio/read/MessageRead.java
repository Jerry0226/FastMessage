package com.jerry.socket.nio.read;


/***
 * 此处是内部消息传输的读取，外部应用不需要操作此方法
 * @author chm
 *
 */
public interface MessageRead {
    void finish();
    void start();
}
