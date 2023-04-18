package com.mjh.usercenter;

import com.mjh.usercenter.model.domain.User;
import com.mjh.usercenter.service.UserService;
import org.junit.jupiter.api.Test;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * encoding='utf-8'
 *
 * @author mjh
 * date:2023-04-18 21:29
 **/
@SpringBootTest
public class InsertUserTest {
    @Resource
    private UserService userService;
    /**
     * 批量插入用户
     */
    @Test
    public void doInsertUser(){
        StopWatch stopWatch= new StopWatch();
        stopWatch.start();
        final int INSERT_USER=100;
        List<User> userList=new ArrayList<>();
        for (int i = 0; i < INSERT_USER; i++) {
            User user = new User();
            user.setUsername("user"+i);
            user.setUserAccount(i+"user");
            user.setUserPassword("123123123");
            user.setEmail("email"+i);
            user.setAvatarUrl("https://img1.baidu.com/it/u=1645832847,2375824523&fm=253&fmt=auto&app=138&f=JPEG?w=480&h=480");
            user.setGender(0);
            user.setPhone("1231312");
            user.setEmail("12331234@qq.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("213123");
            user.setTags("[]");
            userList.add(user);
        }
        userService.saveBatch(userList,INSERT_USER);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());

    }
    /**
     * 并发批量插入用户
     */
    @Test
    public void doConcurrencyInsertUsers() {

        int batchCount=5000;
        int j=0;

    }
}
