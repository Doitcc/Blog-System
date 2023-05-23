package com.caoxin.constants;

public class SystemConstants {

    /**
     * 文章是草稿
     */
    public static final String ARTICLE_STATUS_DRAFT = "1";

    /**
     * 文章是正常分布状态
     */
    public static final String ARTICLE_STATUS_NORMAL = "0";

    /**
     * 修改文章传入的之前的状态
     */
    public static final String BEFORE_ARTICLE_STATUS_DRAFT = "1";
    public static final String BEFORE_ARTICLE_STATUS_NORMAL = "0";

    /**
     * 文章状态为审核中
     */
    public static final String STATUS_NORMAL = "0";

    /**
     * 友链状态为
     */
    public static final String LINK_STATUS_NORMAL = "1";
    public static final String LINK_STATUS = "0";
    public static final String LINK_STATUS_ERROR = "-1";

    /**
     * 没被删除
     */
    public static final String NOT_DELETED = "0";

    /**
     * 评论类型为：文章评论
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     * 评论类型为：友联评论
     */
    public static final String LINK_COMMENT = "1";

    /**
     * 博客主人的id
     */
    public static final String BLOG_ID = "1";
    public static final String ADMIN_ID = "1";

    /**
     * 游客的id
     */
    public static final Long VISITOR_ID = -1L;
}