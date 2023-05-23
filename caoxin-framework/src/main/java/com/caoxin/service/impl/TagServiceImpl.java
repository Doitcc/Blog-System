package com.caoxin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.Tag;
import com.caoxin.domain.vo.PageVo;
import com.caoxin.domain.vo.TagListVo;
import com.caoxin.mapper.TagMapper;
import com.caoxin.service.TagService;
import com.caoxin.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize) {
        //分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(StringUtils.hasText(name),Tag::getName,name);

        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        //封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Autowired
    private TagService tagService;
    @Autowired
    private RedisUtils redisUtils;
    
    @Override
    public ResponseResult<TagListVo> add(String name) {
        
        Tag tag=new Tag();
        tag.setName(name);
//        System.out.println(tag);
        boolean flag = tagService.save(tag);
        if(flag){
            //更新redis中号主标签数量
            redisUtils.incrementCacheMapValue("user:tagCount","1",1); //因为只有一个id（1） 
        }

        return ResponseResult.okResult();
    }
}
