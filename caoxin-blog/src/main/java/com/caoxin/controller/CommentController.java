package com.caoxin.controller;

import com.caoxin.constants.SystemConstants;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.Comment;
import com.caoxin.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }

    @PostMapping("/add")
    public ResponseResult addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }



    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.LINK_COMMENT, -1L, pageNum, pageSize);
    }

    @PostMapping("/addLinkComment")
    public ResponseResult addLinkComment(@RequestBody Comment comment) {
        return commentService.addLinkComment(comment);
    }
}