package com.jerry.socket.nio.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 字符串操作类
 * 
 * @author chm1 字符串帮助类
 */
public final class StringHelper {

    /**
     * 待过滤的字符串，次字符串不打印到日志中
     */
    private static String[] filterString = new String[] {
        "java.util.jar.Attributes read", "Duplicate name in Manifest:",
        "manifest and in the META-INF/MANIFEST.MF entry in the jar file",
        "that blank lines separate individual sections in both your",
        "Ensure that the manifest does not have duplicate entries, and"
    };

    /**
     * 默认构造方法
     */
    private StringHelper() {
    }

    /**
     * 判断字符串是否为空，字符串为null，或是为""，或是只有空格符的字符串也算为null
     * 
     * @param str 被检测字符串
     * @return 是否为null 或是空
     */
    public static boolean isNull(String str) {
        boolean result = false;
        if (str == null) {
            result = true;
        }
        else {
            if (("").equals(str.trim())) {
                result = true;
            }
        }

        return result;
    }

    /**
     * 将字符串从一种指定的编码集转换到另一种编码集
     * 
     * @param val String 要转换的字符串
     * @param formEncoding String 要转换的编码
     * @param toEncoding String 被转换的编码
     * @return String 转换后的字符串
     * @throws UnsupportedEncodingException 异常
     */
    public static String encode(String val, String formEncoding, String toEncoding) throws UnsupportedEncodingException {
        if (val == null) {
            return null;
        }
        return new String(val.getBytes(formEncoding), toEncoding);
    }
    
    /**
     * 把集合转换为字符串中间用逗号分割
     * 
     * @param list 集合变量
     * @return 字符串
     */
    public static String formartList2String(List<Integer> list) {
        if (!Utils.isEmpty(list)) {
            StringBuffer strbff = new StringBuffer();
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    strbff.append(list.get(i).toString());
                }
                else {
                    strbff.append(list.get(i).toString()).append(",");
                }
            }
            return strbff.toString();
        }
        else {
            return null;
        }
    }

    /**
     * 把集合转换为字符串中间用逗号分割
     * 
     * @param list 集合变量
     * @return 字符串
     */
    public static String formartListLong2String(List<Long> list) {
        if (!list.isEmpty()) {
            StringBuffer strbff = new StringBuffer();
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    strbff.append(list.get(i).toString());
                }
                else {
                    strbff.append(list.get(i).toString()).append(",");
                }
            }
            return strbff.toString();
        }
        else {
            return null;
        }
    }

    /**
     * 把集合转换为字符串中间用逗号分割
     * 
     * @param list_str 字符串
     * @param split_char 分隔符
     * @return list<Integer>
     */
    public static List<Integer> formartString2List(String list_str, String split_char) {
        if (!Utils.isEmpty(list_str)) {
            List<Integer> list = new ArrayList<Integer>();
            String[] str_list = list_str.split(split_char);
            for (int i = 0; i < str_list.length; i++) {
                list.add(Integer.valueOf(str_list[i]));
            }
            return list;
        }
        else {
            return null;
        }
    }

    /**
     * 去除字符串两端的特殊字符
     * 
     * @param count 字符串长度
     * @param offset 起始位置
     * @param str 字符串
     * @param toDeleteChar 待删除的字符
     * @return 被处理后的字符串
     */
    public static String trimComma(int count, int offset, String str, char toDeleteChar) {
        int len = count;
        int st = 0;
        int off = offset; /* avoid getfield opcode */
        char[] val = str.toCharArray(); /* avoid getfield opcode */

        while ((st < len) && (val[off + st] == toDeleteChar)) {
            st++;
        }
        while ((st < len) && (val[off + len - 1] == toDeleteChar)) {
            len--;
        }
        if (((st > 0) || (len < count))) {
            return str.substring(st, len);
        }
        else {
            return str;
        }

    }

    /**
     * 判断str 字符串是否包含在filterString 中
     * 
     * @param str 待判断的字符串
     * @return 返回此字符串是否包含在filterString
     */
    public static boolean containFilterString(String str) {
        boolean iscontain = false;
        if (!Utils.isEmpty(str)) {
            for (String strTemp : filterString) {
                if (str.contains(strTemp)) {
                    iscontain = true;
                    break;
                }
            }
        }
        return iscontain;
    }

}
