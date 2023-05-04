package com.mjh.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mjh.usercenter.mapper.UserMapper;
import com.mjh.usercenter.model.domain.User;
import com.mjh.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
    @Resource
    private RedissonClient redissonClient;
    //重点用户
    private List<Long> mainUserList = Arrays.asList(1l);

    //每天执行，预热推荐用户
    //使用redisson来实现分布式锁
    @Scheduled(cron = "0 24 18 * * *")
    public void doCacheRecommendUser(){
        RLock lock = redissonClient.getLock("mjh:precachejob:docache:lock");
        try {
            //只有一个线程可以获取到锁,-1开启开门狗机制，到期自动续期
            if (lock.tryLock(0,-1,TimeUnit.MILLISECONDS)){
                Thread.sleep(300000);
                for (Long userId: mainUserList){
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
                    String redisKey = String.format("mjh:user:recommend:%s", userId);
                    ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
                    //写缓存
                    try {
                        valueOperations.set(redisKey,userPage,30000, TimeUnit.MILLISECONDS);
                    }catch (Exception e){
                        log.error("redis set key error",e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("docacheRecommondUser error",e);
        }finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }
}
