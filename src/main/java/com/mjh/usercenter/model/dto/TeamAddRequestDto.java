package com.mjh.usercenter.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * encoding='utf-8'
 *
 * @author mjh
 * date:2023-05-04 15:43
 **/
@Data
public class TeamAddRequestDto {


    /**
     * 队伍名称
     */
    private String name;

    /**
     * 队伍描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0-公开，1-私有，2-加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;




}
