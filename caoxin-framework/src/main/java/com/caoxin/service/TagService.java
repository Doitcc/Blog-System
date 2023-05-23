package com.caoxin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.Tag;
import com.caoxin.domain.vo.PageVo;
import com.caoxin.domain.vo.TagListVo;


public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize);

    ResponseResult<TagListVo> add(String name);
}
