package com.caoxin.scheduled;

import com.caoxin.constants.SystemConstants;
import com.caoxin.domain.entity.UserOverview;
import com.caoxin.service.UserOverviewService;
import com.caoxin.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpdateUserArticlesAndTagsCount {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserOverviewService userOverviewService;

    //每过一分钟将redis的数据同步到数据库
    @Scheduled(cron = "0 0/1 * * * ?")
    public void updateViewCount() {
        //获取redis中的浏览量
        Map<String, Integer> articleCountMap = redisUtils.getCacheMap("user:articleCount");
        Map<String, Integer> tagCountMap = redisUtils.getCacheMap("user:tagCount");
        Map<String, Integer> categoryCountMap = redisUtils.getCacheMap("user:categoryCount");

        UserOverview userOverview = new UserOverview();
        userOverview.setId(Integer.parseInt(SystemConstants.BLOG_ID));
        userOverview.setCountArticles(articleCountMap.get(SystemConstants.BLOG_ID));
        userOverview.setCountTags(tagCountMap.get(SystemConstants.BLOG_ID));
        userOverview.setCountCategorys(categoryCountMap.get(SystemConstants.BLOG_ID));
        userOverviewService.updateById(userOverview);

    }
}