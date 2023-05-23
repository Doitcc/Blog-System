package com.caoxin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.Link;
import com.caoxin.domain.vo.OrdinaryLinkVo;


public interface LinkService extends IService<Link> {

    ResponseResult getAllLinkAdmin();

    ResponseResult getAllLinkOrdinary();

    ResponseResult editLinkStatus(Long id,String status);

    ResponseResult deleteLink(Long id);

    ResponseResult applyLink(Link link);

    ResponseResult queryStatus();

    ResponseResult queryStatusSelf();

    ResponseResult modifyLink(OrdinaryLinkVo ordinaryLinkVo);
}
