package com.caoxin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoxin.domain.entity.UserOverview;
import com.caoxin.mapper.UserOverviewMapper;
import com.caoxin.service.UserOverviewService;
import org.springframework.stereotype.Service;


@Service("userOverviewService")
public class UserOverviewServiceImpl extends ServiceImpl<UserOverviewMapper, UserOverview> implements UserOverviewService {

}
