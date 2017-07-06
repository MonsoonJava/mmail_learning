package com.xfj.mmail.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by asus on 2017/7/6.
 */
public class FileUploadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

    private static final String IP = PropertiesUtil.getProperty("ftp.server.ip");
    private static final String USER = PropertiesUtil.getProperty("ftp.user");
    private static final String PASSWORD = PropertiesUtil.getProperty("ftp.pass");

    private String ip;
    private String user;
    private String password;
    private int prot;
    private FTPClient ftpClient;


    public  static boolean uploadFiles(List<File> files){
        FileUploadUtil fUpload = new FileUploadUtil(IP,USER,PASSWORD,21);
        LOGGER.info("start to upload file");
        boolean isSuccess = fUpload.connectAndUploadToServer("/imgs", files);
        LOGGER.info("upload file finish");
        return isSuccess;
    }

    private boolean connectAndUploadToServer(String remotePath,List<File> files){
        boolean isSuccess = false;
        FileInputStream fis = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(ip);
            boolean loginSuccess = ftpClient.login(user, password);
            if(loginSuccess){
                boolean isChanged = ftpClient.changeWorkingDirectory(remotePath);
                if(!isChanged){
                    ftpClient.makeDirectory(remotePath);
                    ftpClient.changeWorkingDirectory(remotePath);
                }
                ftpClient.enterLocalPassiveMode();
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setBufferSize(1024);
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                for(File fileItem: files){
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fis);
                }
                isSuccess = true;
            }else{
                LOGGER.error("can connect ftp server");
                return isSuccess;
            }
        } catch (IOException e) {
            LOGGER.error("upload file error:class--{}","FileUploadUtil");
            return false;
        }finally {
            if(null != fis){
                try {
                    fis.close();
                    ftpClient.disconnect();
                } catch (IOException e) {
                    LOGGER.error("close fileinputstream error:class--{}","FileUploadUtil");
                }finally {
                    fis = null;
                    ftpClient = null;
                }
            }
        }
        return isSuccess;
    }



    public FileUploadUtil(String ip, String user, String password, int prot) {
        this.ip = ip;
        this.user = user;
        this.password = password;
        this.prot = prot;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getProt() {
        return prot;
    }

    public void setProt(int prot) {
        this.prot = prot;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
