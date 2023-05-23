package com.caoxin.controller;

import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.vo.PageVo;
import com.caoxin.enums.CodeEnum;
import com.caoxin.service.TagService;
import com.caoxin.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;


    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize) {
        return tagService.pageTagList(pageNum, pageSize);
    }

    @PostMapping("/add")
    public ResponseResult add(String name) {
        return tagService.add(name);
    }

    @Autowired
    private RedisUtils redisUtils;

    @DeleteMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable("id") Integer id) {
        boolean flag = tagService.removeById(id);
        if (flag) {
            redisUtils.incrementCacheMapValue("user:tagCount", "1", -1);
        }
        return flag ? ResponseResult.okResult() : ResponseResult.errorResult(CodeEnum.DEL_ERROR);
    }

}

