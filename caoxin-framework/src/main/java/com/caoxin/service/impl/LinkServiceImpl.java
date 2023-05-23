package com.caoxin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoxin.constants.SystemConstants;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.Link;
import com.caoxin.domain.vo.AdminLinkVo;
import com.caoxin.domain.vo.OrdinaryLinkVo;
import com.caoxin.enums.CodeEnum;
import com.caoxin.mapper.LinkMapper;
import com.caoxin.service.LinkService;
import com.caoxin.utils.BeanCopyUtils;
import com.caoxin.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLinkAdmin() {

        LambdaQueryWrapper<Link> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Link::getCreateTime);
        List<Link> links = this.list(lqw);
        //转换成vo
        List<AdminLinkVo> adminLinkVos = BeanCopyUtils.copyBeanList(links, AdminLinkVo.class);
        //封装返回
        return ResponseResult.okResult(adminLinkVos);
    }

    @Override
    public ResponseResult getAllLinkOrdinary() {

        LambdaQueryWrapper<Link> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        lqw.orderByAsc(Link::getCreateTime);
        List<Link> links = this.list(lqw);
        List<OrdinaryLinkVo> ordinaryLinkVos = BeanCopyUtils.copyBeanList(links, OrdinaryLinkVo.class);

        return ResponseResult.okResult(ordinaryLinkVos);
    }

    @Override
    public ResponseResult editLinkStatus(Long id, String status) {

        Link link = new Link();
        link.setId(id);
        link.setStatus(status);
        this.updateById(link);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLink(Long id) {

        this.removeById(id);
        return ResponseResult.okResult();
    }


    @Override
    public ResponseResult applyLink(Link link) {
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<Link> lqw=new LambdaQueryWrapper();
        lqw.eq(Link::getCreateBy,userId);
        Link one = getOne(lqw);
        if(one!=null){
            link.setStatus(SystemConstants.LINK_STATUS);
            update(link,lqw);
            return ResponseResult.okResult();
        }
        link.setStatus(SystemConstants.LINK_STATUS);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult queryStatus() {
        LambdaQueryWrapper<Link> lqw = new LambdaQueryWrapper<>();
        Long userId = SecurityUtils.getUserId();
        lqw.eq(Link::getCreateBy, userId);
        lqw.eq(Link::getDelFlag, SystemConstants.NOT_DELETED);
        Link oneLink = this.getOne(lqw);
        OrdinaryLinkVo ordinaryLinkVo = BeanCopyUtils.copyBean(oneLink, OrdinaryLinkVo.class);

        return ResponseResult.okResult(ordinaryLinkVo);
    }

    @Override
    public ResponseResult queryStatusSelf() {

        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<Link> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Link::getCreateBy,userId);
        Link oneLink = this.getOne(lqw);
        if(oneLink!=null){
            AdminLinkVo adminLinkVo = BeanCopyUtils.copyBean(oneLink, AdminLinkVo.class);
            return ResponseResult.okResult(adminLinkVo);
        }
        return ResponseResult.errorResult(CodeEnum.USER_NOT_APPLY);
    }

    @Override
    public ResponseResult modifyLink(OrdinaryLinkVo ordinaryLinkVo) {
        
        Long userId = SecurityUtils.getUserId();
        Link link = BeanCopyUtils.copyBean(ordinaryLinkVo, Link.class);
//        link.setCreateBy(userId);
        link.setStatus(SystemConstants.LINK_STATUS);
        LambdaQueryWrapper<Link> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Link::getCreateBy,userId);
        this.update(link,lqw);
        return ResponseResult.okResult();
    }
}
