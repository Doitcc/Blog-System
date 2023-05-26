package com.caoxin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoxin.constants.SystemConstants;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.Comment;
import com.caoxin.domain.entity.User;
import com.caoxin.domain.vo.CommentVo;
import com.caoxin.domain.vo.PageVo;
import com.caoxin.enums.CodeEnum;
import com.caoxin.exception.SystemException;
import com.caoxin.mapper.CommentMapper;
import com.caoxin.service.CommentService;
import com.caoxin.service.UserService;
import com.caoxin.utils.BeanCopyUtils;
import com.caoxin.utils.SensitiveWordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //对articleId进行判断
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType), Comment::getArticleId, articleId);
        //根评论 rootId为-1
        queryWrapper.eq(Comment::getRootId, -1);

        //评论类型
        queryWrapper.eq(Comment::getType, commentType);

        queryWrapper.orderByDesc(Comment::getCreateTime);

        //分页查询
        Page<Comment> page = new Page(pageNum, pageSize);
        page(page, queryWrapper);

        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVoList) {
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            //赋值
            commentVo.setChildren(children);
        }

        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(CodeEnum.CONTENT_NOT_NULL);
        }
        
        SensitiveWordUtils.initMap(); //初始化
        String afterFilterComment = SensitiveWordUtils.matchWords(comment.getContent());
        comment.setContent(afterFilterComment);
        save(comment);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addLinkComment(Comment comment) {
        //评论内容不能为空
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(CodeEnum.CONTENT_NOT_NULL);
        }

        SensitiveWordUtils.initMap(); //初始化
        String afterFilterComment = SensitiveWordUtils.matchWords(comment.getContent());
        comment.setContent(afterFilterComment);
        comment.setArticleId(-1L);
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 根据根评论的id查询所对应的子评论的集合
     *
     * @param id 根评论的id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);

        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }

    private List<CommentVo> toCommentVoList(List<Comment> list) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历vo集合
        for (CommentVo commentVo : commentVos) {
            User user = userService.getById(commentVo.getCreateBy());
            //通过creatyBy查询用户的昵称并赋值
            commentVo.setNickName(user.getNickName());
            commentVo.setAvatar(user.getAvatar());
//            commentVo.setToCommentNickName(commentVo.getToCommentNickName());
            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询
            if (commentVo.getToCommentUserId() != -1) {
                String toCommentNickName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentNickName(toCommentNickName);
            }
        }
        return commentVos;
    }
}