package com.caoxin.enums;

public enum CodeEnum {

    //数据库相关
    ADD_ERROR(101, "添加失败"),
    DEL_ERROR(102, "删除失败"),
    UPD_ERROR(103, "修改失败"),
    GET_ERROR(104, "查询失败"),

    // 成功或失败
    SUCCESS(200, "操作成功"),
    ERROR(201, "操作失败"),
    
    // 权限相关
    NEED_LOGIN(401, "需要登录后操作"),
    ACCOUNT_LOGGED_IN_ELSEWHERE(402, "账号在别处登录，强制下线"),
    NO_OPERATOR_AUTH(403, "无权限操作"),

    //系统问题
    SYSTEM_ERROR(500, "出现错误"),

    //用户获取友链状态
    USER_NOT_APPLY(601, "用户还未申请"),

    //登录和注册相关
    LOGIN_ERROR(700, "用户名或密码错误"),
    REGISTE_ERROR(701, "注册失败"),
    USERNAME_EXIST(702, "邮箱已注册"),
    USERNAME_NOT_NULL(705, "用户名不能为空"),
    PASSWORD_NOT_NULL(715, "密码不能为空"),
    CODE_SEND_ERROR(706, "验证码发送失败"),
    CODE_HAS_SEND(707, "操作过于频繁请稍后再试"),
    INPUT_CORRECT_CODE(708, "请输入正确的验证码"),

    //用户相关
    FILE_TYPE_ERROR(800, "文件类型错误，请上传png文件"),
    CONTENT_NOT_NULL(805, "评论内容不能为空"),
    IMG_MAX_ERROR(806, "上传图片不能大于2M"),

    //管理员相关
    ARTICLE_NOT_EXIST(901, "文章不存在"),
    CATEGORY_NOT_EXIST(902, "分类不存在"),
    TAG_NOT_EXIST(903, "标签不存在");
    
    
    int code;
    String msg;

    CodeEnum(int code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
