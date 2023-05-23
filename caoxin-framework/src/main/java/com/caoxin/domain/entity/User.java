package com.caoxin.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class User{
    //主键
    private Long id;
    //用户名
    private String userName;
    //昵称
    private String nickName;
    //密码
    private String password;
    //用户类型：0代表普通用户，1代表管理员
    private String type;
    //账号状态（0正常 1停用）
    private String status;
    //邮箱
//    private String email;
    //手机号
    private String phonenumber;
    //用户性别（0男，1女，2未知）
    private String sex;
    //头像
    private String avatar;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
    
    private String website;
    private String explanation;

/*===================================================================*/

//    @TableField(exist = false)
//    private GrantedAuthority authorities;
//    
//    public GrantedAuthority getAuthorities() {
//        return this.authorities;
//    }
}
