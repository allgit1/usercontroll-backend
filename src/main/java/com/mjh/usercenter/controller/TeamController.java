package com.mjh.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mjh.usercenter.common.BaseResponse;
import com.mjh.usercenter.common.ErrorCode;
import com.mjh.usercenter.common.ResultUtils;
import com.mjh.usercenter.exception.BusinessException;
import com.mjh.usercenter.model.domain.Team;
import com.mjh.usercenter.model.domain.User;
import com.mjh.usercenter.model.dto.TeamAddRequestDto;
import com.mjh.usercenter.model.dto.TeamQueryDto;
import com.mjh.usercenter.service.TeamService;
import com.mjh.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 *
 * @author mjh
 */
@RestController
@RequestMapping("/team")
@CrossOrigin(origins = {"http://127.0.0.1:5173","http://localhost:5173/"}, allowCredentials = "true")
@Slf4j
public class TeamController {

    @Resource
    private UserService userService;
    @Resource
    private TeamService teamService;

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequestDto teamAddRequestDto, HttpServletRequest request){
       if(teamAddRequestDto==null){
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
       }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequestDto,team);
        long teamId = teamService.addTeam(team, loginUser);
       return ResultUtils.success(teamId);
    }
    @PostMapping("/remove")
    public BaseResponse<Boolean> removeTeam(@RequestBody long id) {
        if (id<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean remove = teamService.removeById(id);
        if (!remove) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody Team team) {
        if (team==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.updateById(team);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }
    @GetMapping("/get")
    public BaseResponse<Team> getTeam(@RequestBody long id) {
        if (id<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询失败");
        }
        return ResultUtils.success(team);
    }
    @GetMapping("/list")
    public BaseResponse<List<Team>> listTeams(TeamQueryDto teamQueryDto) {
        if (teamQueryDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(team,teamQueryDto);
        QueryWrapper<Team>queryWrapper=new QueryWrapper<>(team);
        List<Team> teamList = teamService.list(queryWrapper);
        if (teamList == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询失败");
        }
        return ResultUtils.success(teamList);
    }
    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamsByPage(TeamQueryDto teamQueryDto){
        if (teamQueryDto==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team=new Team();
        BeanUtils.copyProperties(teamQueryDto,team);
        Page<Team>teamPage=new Page<>(teamQueryDto.getPageNum(),teamQueryDto.getPageSize());
        QueryWrapper<Team>queryWrapper=new QueryWrapper<>(team);
        Page<Team>resultPage=teamService.page(teamPage,queryWrapper);
        return ResultUtils.success(resultPage);
    }




}
