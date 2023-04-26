package com.mjh.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mjh.usercenter.mapper.UserMapper;
import com.mjh.usercenter.model.domain.User;
import com.mjh.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * encoding='utf-8'
 *
 * @author mjh
 * date:2023-04-26 22:13
 **/
@Component
@Slf4j
public class PerCacheJob {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String,Object>redisTemplate;
    //重点用户
    private List<Long> mainUserList = Arrays.asList(1l);

    //每天执行，预热推荐用户
    @Scheduled(cron = "0 24 18 * * *")
    public void doCacheRecommendUser(){
        for (Long userId: mainUserList){
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
            String redisKey = String.format("yupao:user:recommend:%s", userId);
            ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
            //写缓存
            try {
                valueOperations.set(redisKey,userPage,30000, TimeUnit.MILLISECONDS);
            }catch (Exception e){
                log.error("redis set key error",e);
            }
        }
    }
}
