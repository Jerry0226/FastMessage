package com.jerry.socket.nio.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;



/**
 * 文件操作帮助类，用来读取拷贝或是其他操作，工具类
 * 
 * @author chm
 */
public final class FileHelper {

    /** 默认编码格式 */
    public static final String DEFAULTCHARTSET = "UTF-8";

    /** 一天的毫秒数 */
    public static final long DAYMILLSECONDS = 1000 * 60 * 60 * 24;

    
    /**
     * 私有化构造方法
     */
    private FileHelper() {

    }

    /**
     * 根据开始和结束行号，读取文件内容
     * 
     * @param fileName 文件名
     * @param startLine 读取开始行号
     * @param endLine 读取结束行号
     * @param charsetName 读取时文件的编码
     * @return 返回读取的内容
     * @throws IOException 异常
     */
    public static String getLine(String fileName, int startLine, int endLine, String charsetName) throws IOException {
        String linesSp = System.getProperty("line.separator");
        StringBuffer sf = new StringBuffer();
        if (StringHelper.isNull(charsetName)) {
            charsetName = DEFAULTCHARTSET;
        }
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(fileName), charsetName));
        String buff = lnr.readLine();
        while (buff != null) {
            if (lnr.getLineNumber() >= startLine && (lnr.getLineNumber() < endLine || endLine < 0)) {
                sf.append(buff);
                sf.append(linesSp);
            }
            buff = lnr.readLine();
        }
        return sf.toString();
    }

    /**
     * 根据开始和结束行号，读取文件内容
     * 
     * @param fileName 文件名
     * @param startLine 读取开始行号
     * @param endLine 读取结束行号
     * @param charsetName 读取时文件的编码
     * @param countNum 文件的行数
     * @return List<String>
     * @throws IOException 异常
     */
    public static List<String> getListFileContent(String fileName, int startLine, int endLine, String charsetName,
        int countNum) throws IOException {
        if (StringHelper.isNull(charsetName)) {
            charsetName = DEFAULTCHARTSET;
        }
        List<String> list = new ArrayList<String>(countNum);
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(fileName), charsetName));
        String buff = lnr.readLine();
        while (buff != null) {
            if (lnr.getLineNumber() >= startLine && (lnr.getLineNumber() < endLine || endLine < 0)) {
                list.add(buff);
            }
            buff = lnr.readLine();
        }
        return list;
    }

    /**
     * 采用BufferedInputStream方式读取文件行数
     * 
     * @param filename 文件名
     * @return 文件的行数
     * @throws IOException 异常
     */
    public static int countLine(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        byte[] c = new byte[1024];
        int count = 0;
        int readChars = 0;
        while ((readChars = is.read(c)) != -1) {
            for (int i = 0; i < readChars; ++i) {
                if (c[i] == '\n') {
                    ++count;
                }
            }
        }
        is.close();
        return count;
    }

    /**
     * 根据文件路径和文件的
     * 
     * @param filePath 文件路径
     * @return 返回文件的内容
     * @throws IOException io异常
     */
    public static String readFile(String filePath) throws IOException {
        return readFile(filePath, DEFAULTCHARTSET);
    }

    /**
     * 根据文件路径和文件的
     * 
     * @param filePath 文件路径
     * @param charsetName 读取编码
     * @return 返回文件的内容
     * @throws IOException io异常
     */
    public static String readFile(String filePath, String charsetName) throws IOException {
        String linesep = System.getProperty("line.separator");
        StringBuffer buffContent = new StringBuffer();
        BufferedReader reader = null;
        try {
            if (StringHelper.isNull(charsetName)) {
                charsetName = DEFAULTCHARTSET;
            }
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charsetName));
            String line;
            while ((line = reader.readLine()) != null) {
                buffContent.append(line).append(linesep);
            }
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
        return buffContent.toString();
    }

    /**
     * 写入文本文件文件内容，根据文件的编码
     * 
     * @param filePath 文件路径
     * @param fileContent 文件内容
     * @param charsetName 文件编码
     * @param append 是否追加写入
     * @return 返回写入是否成功
     * @throws IOException io 异常
     */
    public static boolean writeFile(String filePath, String fileContent, String charsetName, boolean append)
        throws IOException {
        boolean isSucces = false;
        BufferedWriter writer = null;
        try {
            if (StringHelper.isNull(charsetName)) {
                charsetName = DEFAULTCHARTSET;
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, append), charsetName));
            writer.write(fileContent);
            isSucces = true;
        }
        finally {
            if (writer != null) {
                writer.close();
            }
        }
        return isSucces;
    }

    /**
     * 递归获取文件的全部内容
     * 
     * @param filePath 被递归的文件路径
     * @param fileListTemp 文件列表
     * @param fileFilter 文件过滤器
     */
    public static void getFileList(String filePath, List<String> fileListTemp, FileFilter fileFilter) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles(fileFilter);
            if (files == null || files.length <= 0) {
                return;
            }
            else {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        getFileList(files[i].getAbsolutePath(), fileListTemp, fileFilter);
                    }
                    else {
                        fileListTemp.add(files[i].getAbsolutePath());
                    }
                }
            }
        }
        else {
            fileListTemp.add(file.getAbsolutePath());
        }
    }

    /**
     * 得到目录下的所有文件，使用fileListTemp 返回
     * 
     * @param filePath 被变量的目录
     * @param fileListTemp 文件集合
     */
    public static void getFileList(String filePath, List<String> fileListTemp) {
        getFileList(filePath, fileListTemp, null);
    }

    /**
     * 得到目录下的所有文件，使用fileListTemp 返回
     * 
     * @param filePath 被变量的目录
     * @param fileListTemp 文件集合
     */
    public static void getFileList(File filePath, List<File> fileListTemp) {
        getFileList(filePath, fileListTemp, null);
    }

    /**
     * 返回文件列表
     * 
     * @param filePath 文件
     * @param fileListTemp 文件接口
     * @param fileFilter 文件过滤器
     */
    public static void getFileList(File filePath, List<File> fileListTemp, FileFilter fileFilter) {
        List<String> fileStringList = new ArrayList<String>();
        getFileList(filePath.getAbsolutePath(), fileStringList, fileFilter);
        for (String fileTemp : fileStringList) {
            fileListTemp.add(new File(fileTemp));
        }
    }

    /**
     * 拷贝源目录下的文件至目标目录下
     * 
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     * @param isAsci 是否是文本文件
     * @param charsetName 文件编码
     * @return 拷贝是否成功
     * @throws IOException io 异常
     */
    public static boolean copyFile(File sourceFile, File targetFile, boolean isAsci, String charsetName)
        throws IOException {
        String fileContent = readFile(sourceFile.getAbsolutePath(), charsetName);
        return writeFile(targetFile.getAbsolutePath(), fileContent, charsetName, false);
    }

    /**
     * 拷贝源目录下的文件至目标目录下
     * 
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     * @param isAsci 是否是文本文件
     * @return 拷贝是否成功
     * @throws IOException io 异常
     */
    public static boolean copyFile(File sourceFile, File targetFile, boolean isAsci) throws IOException {
        if (isAsci) {
            String fileContent = readFile(sourceFile.getAbsolutePath(), FileHelper.DEFAULTCHARTSET);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            return writeFile(targetFile.getAbsolutePath(), fileContent, FileHelper.DEFAULTCHARTSET, false);
        }
        return false;

    }

    /**
     * 递归删除此目录和目录下的所有子目录及其文件，包含自己
     * 
     * @param file 被递归的文件路径
     * @return 返回是否删除成功
     */
    public static boolean deleteAllFile(File file) {
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    if (!deleteAllFile(files[i])) {
                        return false;
                    }
                }
                else {
                    if (!files[i].delete()) {
                        return false;
                    }
                }
            }
        }
        return file.delete();
    }

    /**
     * 删除文件夹，此操作是否包含删除自己
     * 
     * @param file 被操作的目录
     * @return 删除是否成功
     */
    public static boolean deleteAllFileNotSelf(File file) {
        boolean isSuss = true;
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    isSuss = deleteAllFile(files[i]);
                    if (!isSuss) {
                        break;
                    }
                }
                else {
                    isSuss = files[i].delete();
                    if (!isSuss) {
                        break;
                    }
                }
            }
        }
        return isSuss;
    }

    /**
     * 删除给定文件下几点天前的文件
     * 
     * @param file 待删除的文件目录
     * @param days 多少天以前的文件需要被删除
     */
    public static void deleteFile4Days(File file, int days) {
        long nowDaySec = System.currentTimeMillis();
        File[] files = file.listFiles();
        for (File fileTemp : files) {
            Long lastmodiyLong = fileTemp.lastModified();
            if ((nowDaySec - lastmodiyLong) > DAYMILLSECONDS * days) {
                fileTemp.delete();
            }
        }
    }

}
