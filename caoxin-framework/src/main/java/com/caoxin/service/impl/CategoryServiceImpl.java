package com.caoxin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoxin.constants.SystemConstants;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.Article;
import com.caoxin.domain.entity.Category;
import com.caoxin.enums.CodeEnum;
import com.caoxin.mapper.CategoryMapper;
import com.caoxin.service.ArticleService;
import com.caoxin.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //查询文章表 状态为已发布的文章
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articles = articleService.list(lqw);

//        System.out.println(articles.size()!=0);
        if(articles.size()!=0){
            //查询文章的分类id，并且去重
            Set<Long> categoryIds = articles.stream()
                    .map(o -> o.getCategoryId())
                    .collect(Collectors.toSet());
            //查询分类表
            List<Category> categories = listByIds(categoryIds);
//            List<Category> categoryList = categories.stream()
//                    .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
//                    .collect(Collectors.toList());
            //封装vo
//            List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
            return ResponseResult.okResult(categories);
        }
        return ResponseResult.errorResult(CodeEnum.CATEGORY_NOT_EXIST);
    }
}
