package com.caoxin.runner;

import com.caoxin.constants.SystemConstants;
import com.caoxin.domain.entity.UserOverview;
import com.caoxin.service.UserOverviewService;
import com.caoxin.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserArticlesAndTagsRunner implements CommandLineRunner {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserOverviewService userOverviewService;

    //服务器启动将数据库的相关信息加载到redis
    @Override
    public void run(String... args) throws Exception {
        UserOverview userOverview = userOverviewService.getById(SystemConstants.BLOG_ID);

        Map<String, Integer> articleCountMap = new HashMap<>();
        articleCountMap.put(userOverview.getId().toString(), userOverview.getCountArticles());

        Map<String, Integer> tagCountMap = new HashMap<>();
        tagCountMap.put(userOverview.getId().toString(), userOverview.getCountTags());

        Map<String, Integer> categoryCountMap = new HashMap<>();
        categoryCountMap.put(userOverview.getId().toString(), userOverview.getCountCategorys());

        redisUtils.setCacheMap("user:articleCount", articleCountMap);
        redisUtils.setCacheMap("user:tagCount", tagCountMap);
        redisUtils.setCacheMap("user:categoryCount", categoryCountMap);

    }
}
