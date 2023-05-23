package com.caoxin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoxin.constants.SystemConstants;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.User;
import com.caoxin.domain.entity.UserOverview;
import com.caoxin.domain.vo.UserInfoVo;
import com.caoxin.enums.CodeEnum;
import com.caoxin.exception.SystemException;
import com.caoxin.mapper.UserMapper;
import com.caoxin.service.UserOverviewService;
import com.caoxin.service.UserService;
import com.caoxin.utils.BeanCopyUtils;
import com.caoxin.utils.RedisUtils;
import com.caoxin.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Autowired
    private UserOverviewService userOverviewService;
    @Override
    public ResponseResult updateUserInfo(User user) {
        if(user.getType().equals(SystemConstants.ADMIN_ID)){
            UserOverview userOverview=new UserOverview();
            userOverview.setId(Integer.parseInt(SystemConstants.BLOG_ID));
            userOverview.setExplanation(user.getExplanation());
            userOverview.setNickName(user.getNickName());
            userOverview.setAvatar(user.getAvatar());
            userOverviewService.updateById(userOverview);   
        }
        
        updateById(user);
        
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public ResponseResult register(User user,String code) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(CodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(CodeEnum.PASSWORD_NOT_NULL);
        }
        //对用户名进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(CodeEnum.USERNAME_EXIST);
        }
        
        String redisCode = redisUtils.getCacheObject("emailCode:" + user.getUserName());
        if(code.equals(redisCode)){
            //对密码进行加密
            String encodePassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodePassword);
            user.setType("0");
            user.setExplanation("这家伙很懒，什么也没留下..");
            user.setAvatar("https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png");

            String defaultNickName = UUID.randomUUID().toString()
                    .replaceAll("[^0-9a-z]","")
                    .replaceAll("[a-z]","1")
                    .substring(0, 12);
            user.setNickName("用户1"+defaultNickName);
            
            //存入数据库
            boolean flag = save(user);
            if(flag){
                redisUtils.deleteObject("emailCode:"+user.getUserName());
            }
            return flag?ResponseResult.okResult():ResponseResult.errorResult(CodeEnum.REGISTE_ERROR);
        }
        
        return ResponseResult.errorResult(CodeEnum.INPUT_CORRECT_CODE);
    }
    

    @Autowired
    private RedisUtils redisUtils;
    @Override
    public ResponseResult getUserOverview() {
        Map<String, Object> articleCountMap = redisUtils.getCacheMap("user:articleCount");
        Map<String, Object> tagCountMap = redisUtils.getCacheMap("user:tagCount");
        Map<String, Object> categoryCountMap = redisUtils.getCacheMap("user:categoryCount");
        Integer articleCount = (Integer) articleCountMap.get(SystemConstants.BLOG_ID);
        Integer tagCount = (Integer) tagCountMap.get(SystemConstants.BLOG_ID);
        Integer categoryCount = (Integer) categoryCountMap.get(SystemConstants.BLOG_ID);
        
        //TODO 先读缓存，如果缓存里面没有再读数据库

        UserOverview userOverview=new UserOverview();
        userOverview.setCountArticles(articleCount);
        userOverview.setCountTags(tagCount);
        userOverview.setCountCategorys(categoryCount);
        
        UserOverview userOverviewServiceById = userOverviewService.getById(SystemConstants.BLOG_ID);
        userOverview.setId(Integer.parseInt(SystemConstants.BLOG_ID));
        userOverview.setNickName(userOverviewServiceById.getNickName());
        userOverview.setExplanation(userOverviewServiceById.getExplanation());
        userOverview.setAvatar(userOverviewServiceById.getAvatar());

        return ResponseResult.okResult(userOverview);
    }
    
    
    @Override
    public ResponseResult forgetPassword(String userName, String password,String code) {
        String redisCode = redisUtils.getCacheObject("emailCode:" + userName);
        if(code.equals(redisCode)){
            //对密码进行加密
            String encodePassword = passwordEncoder.encode(password);
            User user=new User();
            user.setPassword(encodePassword);
            
            LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
            lqw.eq(User::getUserName,userName);
            
            boolean flag = this.update(user,lqw);
            if(flag){
                redisUtils.deleteObject("emailCode:"+userName);
            }
            return flag?ResponseResult.okResult():ResponseResult.errorResult(CodeEnum.ERROR);
        }

        return ResponseResult.errorResult(CodeEnum.INPUT_CORRECT_CODE);
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.eq(User::getUserName,userName);
        return count(lqw)>0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.eq(User::getNickName,nickName);
        return count(lqw)>0;
    }
}
