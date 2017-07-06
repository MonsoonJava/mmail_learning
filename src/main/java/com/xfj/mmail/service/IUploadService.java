package com.xfj.mmail.service;

import com.xfj.mmail.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by asus on 2017/7/6.
 */
public interface IUploadService {

    public ServerResponse<String> uploadFile(String path, MultipartFile multipartFile);
}
