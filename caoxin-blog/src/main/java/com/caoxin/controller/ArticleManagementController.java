package com.caoxin.controller;

import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.vo.AddArticleVo;
import com.caoxin.domain.vo.ModifyArticleVo;
import com.caoxin.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/management")
public class ArticleManagementController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/write")
    public ResponseResult add(@RequestBody AddArticleVo articleDto) {
        return articleService.write(articleDto);
    }

    @PutMapping("/modify")
    public ResponseResult modify(@RequestBody ModifyArticleVo modifyArticleVo) {
        return articleService.modify(modifyArticleVo);
    }

    @DeleteMapping("/delete")
    public ResponseResult delete(Long id) {
        return articleService.delete(id);
    }
}
