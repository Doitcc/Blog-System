package com.caoxin.scheduled;

import com.caoxin.domain.entity.Article;
import com.caoxin.service.ArticleService;
import com.caoxin.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCount {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ArticleService articleService;
    
    @Scheduled(cron = "0 0/1 * * * ?")
    public void updateViewCount() {
        //获取redis中的浏览量
        Map<String, Integer> viewCountMap = redisUtils.getCacheMap("article:viewCount");

        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新到数据库中
        articleService.updateBatchById(articles);
    }

}
