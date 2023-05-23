package com.caoxin.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user_overview")
public class UserOverview{
    
    private Integer id;
    
    private String nickName;
    
    private String explanation;
    
    private Integer countArticles;
    
    private Integer countCategorys;
    
    private Integer countTags;
    
    private String avatar;

}

