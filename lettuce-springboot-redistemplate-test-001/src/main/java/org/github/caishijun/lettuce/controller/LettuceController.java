package org.github.caishijun.lettuce.controller;

import org.github.caishijun.lettuce.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class LettuceController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/stringRedisTemplate")
    public String stringRedisTemplate() {

        // key value 设置key过期时间 单位秒
        stringRedisTemplate.opsForValue().set("111","222",60,TimeUnit.SECONDS);
        String s = stringRedisTemplate.opsForValue().get("111");
        System.out.println(s);

        return "success";
    }

    @RequestMapping("/redisTemplate")
    public String redisTemplate() {

        // redis存储数据
        String key = "name";
        redisTemplate.opsForValue().set(key, "yukong");
        // 获取数据
        String value = (String) redisTemplate.opsForValue().get(key);
        System.out.println("获取缓存中key为" + key + "的值为：" + value);

        User user = new User();
        user.setUsername("yukong");
        user.setSex(18);
        user.setId(1L);
        String userKey = "yukong";
        redisTemplate.opsForValue().set(userKey, user);
        User newUser = (User) redisTemplate.opsForValue().get(userKey);
        System.out.println("获取缓存中key为" + userKey + "的值为：" + newUser);

        return "success";
    }


}