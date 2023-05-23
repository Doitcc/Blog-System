package com.caoxin.service.impl;

import ch.qos.logback.core.util.FileSize;
import com.caoxin.domain.ResponseResult;
import com.caoxin.enums.CodeEnum;
import com.caoxin.service.UploadService;
import com.caoxin.utils.FileSizeUtils;
import com.caoxin.utils.PathUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
//@Data
//@ConfigurationProperties(prefix = "oss")
public class OssUploadService implements UploadService {
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        //判断文件类型
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        //对原始文件名进行判断
//        if(!originalFilename.endsWith(".jpg")){
//            throw new SystemException(CodeEnum.FILE_TYPE_ERROR);
//        }
        boolean flag = FileSizeUtils.checkFileSize(img.getSize(), 2, "M");
        if(!flag){
            return ResponseResult.errorResult(CodeEnum.IMG_MAX_ERROR);
        }

        //如果判断通过上传文件到OSS
        String filePath = PathUtils.generateFilePath(originalFilename);
//        System.out.println(filePath);
        String name="head/";
        String url = uploadOss(img,filePath,name);//  2099/2/3/wqeqeqe.png
//        System.out.println(url);
        return ResponseResult.okResult(url);
    }

    @Override
    public ResponseResult uploadArticleImg(MultipartFile img) {
        //判断文件类型
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        //对原始文件名进行判断
//        if(!originalFilename.endsWith(".jpg")){
//            throw new SystemException(CodeEnum.FILE_TYPE_ERROR);
//        }
        System.out.println(img.getSize());
        boolean flag = FileSizeUtils.checkFileSize(img.getSize(), 2, "M");
        if(!flag){
            return ResponseResult.errorResult(CodeEnum.IMG_MAX_ERROR);
        }

        //如果判断通过上传文件到OSS
        String filePath = PathUtils.generateFilePath(originalFilename);
//        System.out.println(filePath);
        String name="article/";
        String url = uploadOss(img,filePath,name);//  2099/2/3/wqeqeqe.png
//        System.out.println(url);
        return ResponseResult.okResult(url);
    }

    @Value("${oss.accessKey}")
    private String accessKey;
    @Value("${oss.secretKey}")
    private String secretKey;
    @Value("${oss.bucket}")
    private String bucket;


    private String uploadOss(MultipartFile imgFile, String filePath,String categoryName){
        System.out.println(imgFile);
        System.out.println(filePath);
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = categoryName+filePath;
        try {
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//                System.out.println(putRet.key);
//                System.out.println(putRet.hash);
                return "http://rulf5wkef.hd-bkt.clouddn.com/"+key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "www";
    }
    

    
}
