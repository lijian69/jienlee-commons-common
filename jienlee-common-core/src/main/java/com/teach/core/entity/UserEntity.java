package com.teach.core.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 统一登录 返回的 页面
 * @author lijian
 */
@Data
@Builder
public class UserEntity {

    private Integer id;

    /**
     * 手机号登陆
     */
    private String phone;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 微信绑定 union id
     */
    private String unionId;


    private String token;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户身份 1：老师端，2：学生家长端
     */
    private Integer role;

    private TeacherEntity teacherEntity;

}
