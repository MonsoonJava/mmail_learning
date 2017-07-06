package com.xfj.mmail.service.impl;

import com.google.common.collect.Lists;
import com.xfj.mmail.common.ServerResponse;
import com.xfj.mmail.service.IUploadService;
import com.xfj.mmail.utils.FileUploadUtil;
import com.xfj.mmail.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by asus on 2017/7/6.
 */
@Service("iUploadService")
public class IUploadServiceImpl implements IUploadService{

    private static final Logger LOGGER = LoggerFactory.getLogger(IUploadServiceImpl.class);
    @Override
    public ServerResponse<String> uploadFile(String path, MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String fileExten = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String newFileName = UUID.randomUUID().toString() + "." + fileExten;
        File filePath = new File(path);
        if(!filePath.exists()){
            filePath.setWritable(true);
            filePath.mkdirs();
        }
        File targetFile = new File(path,newFileName);
        try {
            multipartFile.transferTo(targetFile);
            List<File> files = Lists.newArrayList(targetFile);
            boolean isUploadSuccess = FileUploadUtil.uploadFiles(files);
            targetFile.delete();
            if(!isUploadSuccess){
                return ServerResponse.getServerResponseErrorMessage("uplaod file error");
            }
        } catch (IOException e) {
            LOGGER.error("uplaod file error",e);
            return ServerResponse.getServerResponseErrorMessage("uplaod file error");
        }
        return ServerResponse.getServerResponseSuccess(PropertiesUtil.getProperty("ftp.server.http.prefix")+ newFileName);
    }
}
