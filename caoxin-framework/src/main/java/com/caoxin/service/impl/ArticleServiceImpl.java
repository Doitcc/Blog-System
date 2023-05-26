package com.caoxin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoxin.constants.SystemConstants;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.Article;
import com.caoxin.domain.entity.ArticleTag;
import com.caoxin.domain.entity.Comment;
import com.caoxin.domain.vo.*;
import com.caoxin.enums.CodeEnum;
import com.caoxin.mapper.ArticleMapper;
import com.caoxin.service.*;
import com.caoxin.utils.BeanCopyUtils;
import com.caoxin.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ArticleService articleService;
    
    private ArticleMapper articleMapper;

    @Override
    public ResponseResult hotArticleList() {
        //获取redis中的浏览量
        Map<String, Integer> viewCountMap = redisUtils.getCacheMap("article:viewCount");

        List<Article> articleList = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新到数据库中
        articleService.updateBatchById(articleList);

        //查询热门文章，封装成ResponseResult
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        //必须是正式文章
        lqw.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);

        //按照浏览量进行排序
        lqw.orderByDesc(Article::getViewCount);
        //最多只查询10条
        Page<Article> page = new Page<>(1, 3);
        page(page, lqw);

        List<Article> articles = page.getRecords();

        List<Article> articles1 = articles.stream()
                .map(article -> {
                    Integer viewCount = redisUtils.getCacheMapValue("article:viewCount", article.getId().toString());
                    article.setViewCount(viewCount.longValue());
                    return article;
                }).collect(Collectors.toList());

        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles1, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    }

    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId, Integer status) {
        //查询条件
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        //如果有categoryId：要求查询时要和传入的相同
        lqw.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        //状态是正式发布的
        lqw.eq(status != null, Article::getStatus, status);
        //对isTop进行降序
        lqw.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> p = new Page<>(pageNum, pageSize);
        page(p, lqw);

        List<Article> articles = p.getRecords();

        //查询categoryName
        List<Article> list = articles.stream()
                .map(article -> {
                    if (redisUtils.getCacheMapValue("article:viewCount", article.getId().toString()) != null) {
                        article.setViewCount(Long.valueOf(redisUtils.getCacheMapValue
                                ("article:viewCount", article.getId().toString()).toString())); //先变成string再变成long
                    }

                    LambdaQueryWrapper<ArticleTag> lqw2 = new LambdaQueryWrapper();
                    lqw2.eq(ArticleTag::getArticleId, article.getId());
                    List<ArticleTag> articleTags = articleTagService.list(lqw2);
                    List<String> tagName = articleTags.stream().map(articleTag ->
                            tagService.getById(articleTag.getTagId()).getName()).collect(Collectors.toList());

                    article.setTagName(tagName);
                    return article;
                }).collect(Collectors.toList());

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(list, ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos, p.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private TagService tagService;

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = this.getById(id);
        if (article != null) {
            //从redis中获取viewCount
            Integer viewCount = redisUtils.getCacheMapValue("article:viewCount", id.toString());
//            System.out.println(viewCount);
            if (viewCount != null) {
                article.setViewCount(viewCount.longValue());
            }
            //转换成vo
            ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
            LambdaQueryWrapper<ArticleTag> lqw = new LambdaQueryWrapper<>();
            lqw.eq(ArticleTag::getArticleId, id);
            List<ArticleTag> list = articleTagService.list(lqw);
            List<String> tagName = list.stream().map(articleTag ->
                    tagService.getById(articleTag.getTagId()).getName()).collect(Collectors.toList());

            articleDetailVo.setTagName(tagName);

            //封装响应返回
            return ResponseResult.okResult(articleDetailVo);
        }
        return ResponseResult.errorResult(CodeEnum.ARTICLE_NOT_EXIST);
    }


    @Override
    public ResponseResult updateViewCount(Long id) {
        if (redisUtils.getCacheMapValue("article:viewCount", id.toString()) != null) {
            //更新redis中对应 id的浏览量
            redisUtils.incrementCacheMapValue("article:viewCount", id.toString(), 1);
            return ResponseResult.okResult();
        }
//        Article article = getById(id);
//        Long viewCount = article.getViewCount();
//        viewCount++;
//        Article article1=new Article();
//        article1.setViewCount(viewCount);
//        article1.setId(id);
//        this.updateById(article1);
        return ResponseResult.errorResult(CodeEnum.ERROR);
//        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult write(AddArticleVo articleDto) {

        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        boolean flag = articleService.save(article);
        if (flag&&SystemConstants.STATUS_NORMAL.equals(article.getStatus())) {
            Long id = article.getId();
                //更新redis中号主文章数量
                redisUtils.incrementCacheMapValue("user:articleCount", SystemConstants.BLOG_ID, 1); //因为只有一个id（1）
                redisUtils.setCacheMapValue("article:viewCount", id.toString(), 0);
        }
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }
    

    @Override
    public ResponseResult modify(ModifyArticleVo modifyArticleVo) {

        Article article = BeanCopyUtils.copyBean(modifyArticleVo, Article.class);
        article.setUpdateTime(new Date());
        boolean flag = updateById(article);
        Long id = article.getId();

        if(flag&&SystemConstants.ARTICLE_STATUS_NORMAL.equals(modifyArticleVo.getStatus())
                &&SystemConstants.BEFORE_ARTICLE_STATUS_DRAFT.equals(modifyArticleVo.getBeforeStatus())){
            //更新redis中号主文章数量
            redisUtils.incrementCacheMapValue("user:articleCount", SystemConstants.BLOG_ID, 1); //因为只有一个id（1）
            redisUtils.setCacheMapValue("article:viewCount", id.toString(), 0);
        }else if(flag&&SystemConstants.ARTICLE_STATUS_DRAFT.equals(article.getStatus())
                &&SystemConstants.BEFORE_ARTICLE_STATUS_NORMAL.equals(modifyArticleVo.getBeforeStatus())){
            redisUtils.incrementCacheMapValue("user:articleCount", SystemConstants.BLOG_ID, -1); //因为只有一个id（1）
        }
        List<ArticleTag> articleTags = modifyArticleVo.getTags().stream().map(tagId ->
                new ArticleTag(modifyArticleVo.getId(), tagId)).collect(Collectors.toList());
//        articleTagMapper.deleteByArticleId(modifyArticleVo.getId());
        LambdaQueryWrapper<ArticleTag> lqw=new LambdaQueryWrapper<>();
        lqw.eq(ArticleTag::getArticleId,modifyArticleVo.getId());
        articleTagService.remove(lqw);
        articleTagService.saveBatch(articleTags);

        return ResponseResult.okResult();
    }

    @Autowired
    private CommentService commentService;

    @Override
    public ResponseResult delete(Long id) {
        boolean flag = articleService.removeById(id);
//        articleTagMapper.deleteByArticleId(id);
        LambdaQueryWrapper<ArticleTag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        articleTagService.remove(queryWrapper);
        
        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Comment::getArticleId, id);
        commentService.remove(lqw);

        if (flag) {
            redisUtils.delCacheMapValue("article:viewCount", id.toString());
            redisUtils.incrementCacheMapValue("user:articleCount", "1", -1);
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(CodeEnum.DEL_ERROR);
    }

    @Override
    public ResponseResult articleTagList(Long tagId) {

        LambdaQueryWrapper<ArticleTag> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleTag::getTagId, tagId);
        List<ArticleTag> articleTagList = articleTagService.list(lqw);

//        System.out.println(articleTagList);
        if (articleTagList.size() != 0) {
            List<Article> articleList = articleTagList.stream().map(articleTag -> {
                Article article = articleService.getById(articleTag.getArticleId());
                Integer viewCount = redisUtils.getCacheMapValue("article:viewCount", article.getId().toString());
                article.setViewCount(viewCount.longValue());

                LambdaQueryWrapper<ArticleTag> lqw2 = new LambdaQueryWrapper<>();
                lqw2.eq(ArticleTag::getArticleId, article.getId());
                List<ArticleTag> articleTags = articleTagService.list(lqw2);
                List<String> tagName = articleTags.stream().map(articleTagOne ->
                                tagService.getById(articleTagOne.getTagId()).getName())
                        .collect(Collectors.toList());
                article.setTagName(tagName);
                return article;
            }).collect(Collectors.toList());

            List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articleList, ArticleListVo.class);
            return ResponseResult.okResult(articleListVos);
        }
        return ResponseResult.errorResult(CodeEnum.ARTICLE_NOT_EXIST);
    }


}
