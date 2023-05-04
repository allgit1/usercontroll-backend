package com.mjh.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mjh.usercenter.model.domain.Team;
import com.mjh.usercenter.model.domain.User;


/**
* @author mjh
* @description 针对表【team】的数据库操作Service
* @createDate 2023-05-02 13:27:22
*/
public interface TeamService extends IService<Team> {

    long addTeam(Team team, User loginUser);
}
