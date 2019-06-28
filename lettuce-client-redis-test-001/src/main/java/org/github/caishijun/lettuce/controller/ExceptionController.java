package org.github.caishijun.lettuce.controller;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class ExceptionController {
    private final static String REDIS_HOST = "192.168.1.33";
    private final static int REDIS_PORT = 6379;
    private final static int START_DB = 15;
    //key超过128
    @RequestMapping("/largeKeyTest")
    @ResponseBody
    public String largeKeyTest() {
        String key="keykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeyke:128key";

        // 利用redis-server所绑定的IP和Port创建URI，
        RedisURI redisURI = RedisURI.create(REDIS_HOST, REDIS_PORT);
        redisURI.setDatabase(1);

        // 创建集Redis单机模式客户端
        RedisClient redisClient = RedisClient.create(redisURI);
        // 开启连接
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisCommands<String, String> cmd = connect.sync();

        // set操作，成功则返回OK
        cmd.set(key, "value-test");

        // get操作，成功命中则返回对应的value，否则返回null
        String value=cmd.get(key);
        System.err.println("get:"+value );

        connect.close();
        redisClient.shutdown();

        return "success";
    }

    //错误的port
    @RequestMapping("/errorPortTest")
    @ResponseBody
    public String errorPortTest() {
        String key="key";

        // 利用redis-server所绑定的IP和Port创建URI，
        RedisURI redisURI = RedisURI.create(REDIS_HOST, 6378);
        redisURI.setDatabase(2);

        // 创建集Redis单机模式客户端
        RedisClient redisClient = RedisClient.create(redisURI);
        // 开启连接
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisCommands<String, String> cmd = connect.sync();

        // set操作，成功则返回OK
        cmd.set(key, "value-test");

        // get操作，成功命中则返回对应的value，否则返回null
        String value=cmd.get(key);
        System.err.println("get:"+value );

        connect.close();
        redisClient.shutdown();
        return "success";
    }

    //错误的ip
    @RequestMapping("/errorIpTest")
    @ResponseBody
    public String errorIpTest() {
        String key="key";

        // 利用redis-server所绑定的IP和Port创建URI，
        RedisURI redisURI = RedisURI.create("192.168.1.34", REDIS_PORT);
        redisURI.setDatabase(3);

        // 创建集Redis单机模式客户端
        RedisClient redisClient = RedisClient.create(redisURI);
        // 开启连接
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisCommands<String, String> cmd = connect.sync();

        // set操作，成功则返回OK
        cmd.set(key, "value-test");

        // get操作，成功命中则返回对应的value，否则返回null
        String value=cmd.get(key);
        System.err.println("get:"+value );

        connect.close();
        redisClient.shutdown();
        return "success";
    }

    //算数错误
    @RequestMapping("/ArithmeticError")
    @ResponseBody
    public String ArithmeticError() {
        String key="key";

        // 利用redis-server所绑定的IP和Port创建URI，
        RedisURI redisURI = RedisURI.create(REDIS_HOST, REDIS_PORT);
        redisURI.setDatabase(4);

        // 创建集Redis单机模式客户端
        RedisClient redisClient = RedisClient.create(redisURI);
        // 开启连接
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisCommands<String, String> cmd = connect.sync();

        // set操作，成功则返回OK
        cmd.set(key, "value-test");

        cmd.select(1/0);

        // get操作，成功命中则返回对应的value，否则返回null
        String value=cmd.get(key);
        System.err.println("get:"+value );

        connect.close();
        redisClient.shutdown();

        return "success";
    }

    //io.lettuce.core.RedisCommandTimeoutException: Command timed out after no timeout
    @RequestMapping("/TimeoutException")
    @ResponseBody
    public String TimeoutException() {
        String key="key";

        // 利用redis-server所绑定的IP和Port创建URI，
        RedisURI redisURI = RedisURI.create(REDIS_HOST, REDIS_PORT);
        redisURI.setDatabase(5);

        // 创建集Redis单机模式客户端
        RedisClient redisClient = RedisClient.create(redisURI);
        redisClient.setDefaultTimeout(0, TimeUnit.SECONDS);
        // 开启连接
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisCommands<String, String> cmd = connect.sync();

        // set操作，成功则返回OK
        cmd.set(key, "value-test");


        // get操作，成功命中则返回对应的value，否则返回null
        String value=cmd.get(key);
        System.err.println("get:"+value );

        connect.close();
        redisClient.shutdown();

        return "success";
    }
}
