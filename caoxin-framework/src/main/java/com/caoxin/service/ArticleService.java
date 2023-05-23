package com.caoxin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.Article;
import com.caoxin.domain.vo.AddArticleVo;
import com.caoxin.domain.vo.ModifyArticleVo;


public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId,Integer status);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult write(AddArticleVo article);

    ResponseResult modify(ModifyArticleVo modifyArticleVo);

    ResponseResult delete(Long id);

    ResponseResult articleTagList(Long tagId);
}
