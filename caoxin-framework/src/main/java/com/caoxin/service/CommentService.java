package com.caoxin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.Comment;


public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);

    ResponseResult addLinkComment(Comment comment);
}

