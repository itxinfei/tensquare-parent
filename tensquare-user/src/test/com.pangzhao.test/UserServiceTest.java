package com.pangzhao.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * 注册一个管理账号admin,密码admin
 *
 * @author Administrator
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Test
    public void register() {
        System.out.println("demo");
    }
}