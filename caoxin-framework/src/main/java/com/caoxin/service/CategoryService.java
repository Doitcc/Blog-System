package com.caoxin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.Category;


public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}
