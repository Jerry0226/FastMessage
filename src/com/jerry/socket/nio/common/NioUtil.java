package com.jerry.socket.nio.common;

public final class NioUtil {

    /**
     * 构造函数私有化
     */
    private NioUtil() {
        
    }
    
    /**
     * byte[]转int
     * 
     * @param bytes 字节
     * @return int 类型
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;
            // 往高位游
        }
        return value;
    }

    /**
     * int到byte[]
     * 
     * @param i 长度
     * @return 字节
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        // 由高位到低位
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }
    
    public static byte[] long2Bytes(long num) {  
        byte[] byteNum = new byte[8];  
        for (int ix = 0; ix < 8; ++ix) {  
            int offset = 64 - (ix + 1) * 8;  
            byteNum[ix] = (byte) ((num >> offset) & 0xff);  
        }  
        return byteNum;  
    }  
      
    public static long bytes2Long(byte[] byteNum) {  
        long num = 0;  
        for (int ix = 0; ix < 8; ++ix) {  
            num <<= 8;  
            num |= (byteNum[ix] & 0xff);  
        }  
        return num;  
    }  
    
    public static byte[] charToByte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }
    
}
