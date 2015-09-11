package com.jerry.socket.nio.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: Prodigy
 * </p>
 * <p>
 * Description: 工具类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: ztesoft
 * </p>
 * 
 * @author fan.zhenzhi
 * @version 0.1
 */
public final class Utils {

    /** 构造方法 */
    private Utils() {
    }

    

    /**
     * 判断对象数组是否为空
     * 
     * @param objs Object[]
     * @return boolean
     */
    public static boolean isEmpty(Object[] objs) {
        if (objs == null || objs.length == 0) {
            return true;
        }

        return false;
    }

    
    /**
     * 根据分子和分母得到百分比
     * 
     * @param Molecular 分子
     * @param Denominator 分母
     * @return 百分比
     */
    public static int getPercentage(int Molecular, int Denominator) {
        /** 在ztest的结果区显示ztp的执行通过率 */

        if (Denominator == 0) {
            return 0;
        }
        Double succ_result = Double.valueOf(Molecular);
        Double all_result = Double.valueOf(Denominator);

        // double的方法intValue 是直接截取小数位
        Double result = (succ_result / all_result) * 100;
        return result.intValue();
    }
    
    /**
     * 判断字符串是否为空
     * 
     * @param str String
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }

        return false;
    }

    /**
     * 判断字符串是否为空
     * 
     * @param str Object
     * @return boolean
     */
    public static boolean isEmpty(Object str) {
        if (str == null) {
            return true;
        }

        return false;
    }

    

    /**
     * 判断一个字符串是否数字
     * 
     * @param val String
     * @return boolean
     */
    public static boolean isNumber(String val) {
        try {
            Double.parseDouble(val);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 除数 DIV
     */
    public static final long DIV = 10000;

    /**
     * 得到float数据
     * 
     * @param value long
     * @return float
     */
    public static float divLongToFloat(long value) {
        float ret = ((float) value) / DIV;
        return ret;
    }

    /**
     * long修改为double
     * 
     * @param value 值
     * @return double double 值
     */
    public static double divLongToDouble(long value) {
        double ret = ((double) value) / DIV;
        return ret;
    }

    /**
     * 换成字符串插入数据库
     * 
     * @param value long
     * @return String
     */
    public static String divLongToString(long value) {
        String ret = Double.toString(((double) value) / DIV);
        return ret;
    }
    
    /**
     * 判断集合是否为空
     * 
     * @param coll Collection
     * @return boolean
     */
    public static boolean isEmpty(Collection<?> coll) {

        if (coll.isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * 判断int数组是否为空
     * 
     * @param intArr int[]
     * @return boolean
     */
    public static boolean isEmpty(int[] intArr) {
        if (intArr == null || intArr.length == 0) {
            return true;
        }

        return false;
    }

    /**
     * 判断long数组是否为空
     * 
     * @param longArr long[]
     * @return boolean
     */
    public static boolean isEmpty(long[] longArr) {
        if (longArr == null || longArr.length == 0) {
            return true;
        }

        return false;
    }

    /**
     * 判断Map是否为空
     * 
     * @param map Map
     * @return boolean
     */
    public static boolean isEmpty(Map<?, ?> map) {
        if (map.isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * 小数点处理
     * 
     * @param ll long
     * @param deciLen int
     * @return String
     */
    public static String fomartLong(long ll, int deciLen) {
        // 小数点后位数
        String strMoney = Long.toString(ll);
        StringBuffer retSb = new StringBuffer();
        // 如果为负数
        if (("-").equals(strMoney.substring(0, 1))) {
            // 第一个为负号
            retSb.append("-");
            // 实际数字长度，去掉负号的
            strMoney = strMoney.substring(1);
        }
        // 字符串长度
        int len = strMoney.length();
        // 验证长度，不满或者4位数前补零
        if (len <= deciLen) {
            // 补零的位数
            int iRex = deciLen - len;
            retSb.append("0").append(".");
            for (int i = 0; i < iRex; i++) {
                retSb.append("0");
            }
            retSb.append(strMoney.substring(0));
        }
        else {
            // 超过4位的，移小数点
            int offset = len - deciLen;
            retSb.append(strMoney.substring(0, offset)).append(".").append(strMoney.substring(offset));
        }
        // 去掉最后几位
        String ret = retSb.toString();
        for (int i = 0; i < 4; i++) {
            if (!("0").equals(ret.substring(ret.length() - 1))) {
                break;
            }
            ret = ret.substring(0, ret.length() - 1);
            if (i == 3) {
                ret = ret.substring(0, ret.length() - 1);
            }
        }
        return ret;
    }

   

    /**
     * 字符串是否在列表中被找到
     * 
     * @param str String 要查找的字符串
     * @param list List 字符串列表, 将调用list.get(index).toString()方法和str比较
     * @return boolean
     */
    public static boolean exists(String str, List<String> list) {
        if (isEmpty(str) || isEmpty(list)) {
            return false;
        }

        for (int i = 0; i < list.size(); i++) {
            if (str.equals(list.get(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * <把id标识数组转换为以','分割的字符串>
     * 
     * @param idArr idArr
     * @return String String
     */
    public static String convertIdArr2StringByComma(int[] idArr) {
        StringBuffer retStr = new StringBuffer();
        if (isEmpty(idArr)) {
            return retStr.toString();
        }

        for (int i = 0; i < idArr.length; i++) {
            retStr.append(String.valueOf(idArr[i])).append(",");
        }

        return retStr.substring(0, retStr.length() - 1);
    }

    /**
     * <把id标识数组转换为以','分割的字符串>
     * 
     * @param idArr idArr
     * @return String String
     */
    public static String convertIdArr2StringByComma(long[] idArr) {
        StringBuffer retStr = new StringBuffer();
        if (isEmpty(idArr)) {
            return retStr.toString();
        }

        for (int i = 0; i < idArr.length; i++) {
            retStr.append(String.valueOf(idArr[i])).append(",");
        }

        return retStr.substring(0, retStr.length() - 1);
    }

    /**
     * <把id标识数组转换为以','分割的字符串>
     * 
     * @param idArr idArr
     * @return String String
     */
    public static String convertIdArr2StringByComma(String[] idArr) {
        StringBuffer retStr = new StringBuffer();
        if (isEmpty(idArr)) {
            return retStr.toString();
        }

        for (int i = 0; i < idArr.length; i++) {
            retStr.append(String.valueOf(idArr[i])).append(",");
        }

        return retStr.substring(0, retStr.length() - 1);
    }
    
    
    /**
     * 得到long数据
     * 
     * @param value long
     * @return float
     */
    public static long mulFloatToLong(float value) {
        return (long) (value * 10000);
    }

    /**
     * 去掉字符串的空格
     * 
     * @param str String
     * @return String
     */
    public static String trim(String str) {
        if (str == null) {
            return str;
        }
        return str.trim();
    }

    /**
     * 字符串是否在字符传集合中被找到
     * 
     * @param str String
     * @param collArr String[]
     * @return boolean true-找到, false-未找到
     */
    public static boolean exists(String str, String[] collArr) {
        if (isEmpty(str) || isEmpty(collArr)) {
            return false;
        }

        for (int i = 0; i < collArr.length; i++) {
            if (str.equals(collArr[i])) {
                return true;
            }
        }

        return false;
    }

   

    /**
     * 获取异常的全部消息
     * 
     * @param e 异常
     * @return 异常的全部消息
     */
    public static String getThrowableMessage(Throwable e) {
        StringBuffer resultMessage = new StringBuffer();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
        try {
            String line = reader.readLine();
            while (line != null) {
                resultMessage.append(line).append("\n");
                line = reader.readLine();
            }
        }
        catch (IOException ex) {
            resultMessage.append(ex.toString()).append("\n");
        }
        return resultMessage.toString();

    }
    
    
    /***
     * 根据CLOB 字段返回字符串
     * 
     * @param clob clob字段
     * @return 字符串
     * @throws SQLException 返回异常
     * @throws IOException 返回异常
     */
    public static String clobToString(Clob clob) throws SQLException, IOException {

        String reString = "";
        Reader is = clob.getCharacterStream();
        // 得到流
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {
            // 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
            sb.append(s);
            s = br.readLine();
        }
        reString = sb.toString();
        br.close();
        is.close();
        return reString;
    }

    /**
     * 根据文件的全路径获取文件名称
     * 
     * @param filePath 文件全路径
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        if (filePath == null || filePath.trim().length() == 0) {
            return "";
        }
        else {
            return new File(filePath).getName();
        }
    }

    /**
     * 把字符串根据分隔符返回分割后的字符串数组
     * 
     * @param strSource 被分割的字符串
     * @param splitStr 分隔符
     * @return String[]
     */
    public static String[] string2Array(String strSource, String splitStr) {
        if (Utils.isEmpty(strSource)) {
            return new String[0];
        }
        if (Utils.isEmpty(splitStr)) {
            return new String[] {
                strSource
            };
        }
        return strSource.split(splitStr);
    }


}
