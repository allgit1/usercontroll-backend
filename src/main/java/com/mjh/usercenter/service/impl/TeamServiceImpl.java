package com.mjh.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mjh.usercenter.Enum.TeamStatusEnum;
import com.mjh.usercenter.common.ErrorCode;
import com.mjh.usercenter.exception.BusinessException;
import com.mjh.usercenter.mapper.TeamMapper;
import com.mjh.usercenter.model.domain.Team;
import com.mjh.usercenter.model.domain.User;
import com.mjh.usercenter.model.domain.UserTeam;
import com.mjh.usercenter.service.TeamService;
import com.mjh.usercenter.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
* @author mjh
* @description 针对表【team】的数据库操作Service实现
* @createDate 2023-05-02 13:27:22
*/
@Transactional(rollbackFor = Exception.class)
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService {
    @Resource
    private UserTeamService userTeamService;
    @Override
    public long addTeam(Team team, User loginUser) {
        //1.请求参数为空？
        if (team == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2.是否登录，未登录不允许创建
        if (loginUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        final long userId = loginUser.getId();
        //3.校验信息：
        //（1）.队伍人数>1且<20
        int maxNun = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNun>20||maxNun<1){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍人数不满足要求");
        }
        //（2）.队伍标题小于20
        String name = team.getName();
        if (StringUtils.isBlank(name)||name.length()>20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍标题不满足要求");
        }
        //(3).描述小于512
        String description = team.getDescription();
        if (StringUtils.isNotBlank(description) && description.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述不满足要求");
        }
        //4.status是否公开，不传默认为0公开
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum teamStatusEnum = TeamStatusEnum.getTeamStatusEnum(status);
        if (teamStatusEnum==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
        //5.如果status为加密状态，一定要有密码，且密码<=32
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(status)){
            if (StringUtils.isNotBlank(password)||password.length()>32){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码位数过长");
            }
        }
        //过期时间>当前时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "已失效");
        }
        //7.检验用户最多创建五个队伍
        QueryWrapper<Team>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        long hasTeamNum=this.count(queryWrapper);
        if(hasTeamNum>=5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户最多创建5个队伍");
        }
        //8.插入队伍关系到队伍表
        team.setId(null);
        team.setUserId(userId);
        boolean result = this.save(team);
        Long teamId = team.getId();
        if(!result||team==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        //9.插入用户，队伍关系 到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"创建队伍失败");
        }
        return teamId;
    }
}




