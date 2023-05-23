package com.caoxin.controller;

import com.caoxin.domain.ResponseResult;
import com.caoxin.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img) {
        return uploadService.uploadImg(img);
    }

    @PostMapping("/uploadArticleImg")
    public ResponseResult uploadArticleImg(MultipartFile img) {
        return uploadService.uploadArticleImg(img);
    }
}