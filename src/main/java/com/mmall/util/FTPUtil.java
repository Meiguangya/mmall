package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.ftp.FtpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {
    private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPassword = PropertiesUtil.getProperty("ftp.pass");

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FTPUtil(String ip, int port, String user, String ftpPassword) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = ftpPassword;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPassword);
        logger.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile("img", fileList);
        logger.info("开始连接ftp服务器,结束上传,上传结果:{}");
        return result;
    }

    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        if (connectServer(this.ip, this.port, this.user, this.pwd)) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for (File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }
            } catch (IOException e) {
                logger.error("上传文件异常", e);
                uploaded = false;
                e.printStackTrace();
            } finally {
                fis.close();
                ftpClient.disconnect();
            }

        }
        return uploaded;
    }

    private boolean connectServer(String ip, int port, String user, String pwd) {
        ftpClient = new FTPClient();
        boolean isSuccess = false;
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.error("登录ftp服务器失败", e);
        }
        return isSuccess;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
