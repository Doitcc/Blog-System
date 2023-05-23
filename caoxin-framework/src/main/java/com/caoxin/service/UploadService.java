package com.caoxin.service;

import com.caoxin.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
    ResponseResult uploadArticleImg(MultipartFile img);
}