package com.caoxin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListVo {

    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;
    
    private Long categoryId;
    //所属分类名
    private List<String> tagName;
    //缩略图
    private String thumbnail;
    
    private String isTop;
    //访问量
    private Long viewCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")  //时间格式化注解
    private Date createTime;
}
