package com.caoxin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoxin.constants.SystemConstants;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.LeaveMessage;
import com.caoxin.domain.entity.User;
import com.caoxin.enums.CodeEnum;
import com.caoxin.mapper.MessageMapper;
import com.caoxin.service.LeaveMessageService;
import com.caoxin.service.UserService;
import com.caoxin.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service("messageService")
public class LeaveMessageServiceImpl extends ServiceImpl<MessageMapper, LeaveMessage> implements LeaveMessageService {
    
    @Autowired
    private UserService userService;

    @Override
    public ResponseResult loggedIn(String content) {
        User user = userService.getById(SecurityUtils.getUserId());

        LeaveMessage leaveMessage =new LeaveMessage();
        leaveMessage.setContent(content);
        leaveMessage.setCreateBy(SecurityUtils.getUserId());
        leaveMessage.setCreateTime(new Date());
        leaveMessage.setAvatar(user.getAvatar());
        boolean flag = this.save(leaveMessage);
        return flag?ResponseResult.okResult(leaveMessage.getId()):ResponseResult.errorResult(CodeEnum.ERROR);
    }

    @Override
    public ResponseResult notLoggedIn(String content,String avatar) {
        LeaveMessage leaveMessage =new LeaveMessage();
        leaveMessage.setContent(content);
        leaveMessage.setCreateBy(SystemConstants.VISITOR_ID);
        leaveMessage.setCreateTime(new Date());
        leaveMessage.setAvatar(avatar);
        boolean flag = this.save(leaveMessage);
        
        return flag?ResponseResult.okResult(leaveMessage.getId()):ResponseResult.errorResult(CodeEnum.ERROR);
    }

    @Override
    public ResponseResult getMessage() {
        List<LeaveMessage> leaveMessageList = this.list();
        
        return ResponseResult.okResult(leaveMessageList);
    }
}
