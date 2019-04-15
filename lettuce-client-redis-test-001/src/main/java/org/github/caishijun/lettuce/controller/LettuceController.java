package org.github.caishijun.lettuce.controller;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class LettuceController {

    private final static String REDIS_HOST = "192.168.1.33";
    private final static int REDIS_PORT = 6379;
    private final static int START_DB = 15;

    @RequestMapping("/syncInvoke")
    public String syncInvoke() {

        for (int i = START_DB; i < 16; i++) {
            // 利用redis-server所绑定的IP和Port创建URI，
            RedisURI redisURI = RedisURI.create(REDIS_HOST, REDIS_PORT);
            redisURI.setDatabase(i);

            // 创建集Redis单机模式客户端
            RedisClient redisClient = RedisClient.create(redisURI);
            // 开启连接
            StatefulRedisConnection<String, String> connect = redisClient.connect();
            RedisCommands<String, String> cmd = connect.sync();

            // set操作，成功则返回OK
            cmd.set("keyCai", "value-test");

            // get操作，成功命中则返回对应的value，否则返回null
            cmd.get("keyCai");
            // 删除指定的key
            cmd.del("keyCai", "keyCai1");
            // 获取redis-server信息，内容极为丰富
            cmd.info();

            // 列表操作
            String[] valuelist = {"China", "Americal", "England"};
            // 将一个或多个值插入到列表头部，此处插入多个
            cmd.lpush("listNameCai", valuelist);
            // 移出并获取列表的第一个元素
            System.out.println(cmd.lpop("listNameCai"));
            // 获取列表长度
            System.out.println(cmd.llen("listNameCai"));
            // 通过索引获取列表中的元素
            System.out.println(cmd.lindex("listNameCai", 1));
        }

        return "success";
    }

    @RequestMapping("/asyncInvoke")
    public String asyncInvoke() {

        for (int i = START_DB; i < 16; i++) {
            // 利用redis-server所绑定的IP和Port创建URI，
            RedisURI redisURI = RedisURI.create(REDIS_HOST, REDIS_PORT);
            redisURI.setDatabase(i);

            // 创建集Redis单机模式客户端
            RedisClient redisClient = RedisClient.create(redisURI);
            // 开启连接
            StatefulRedisConnection<String, String> connect = redisClient.connect();
            RedisAsyncCommands<String, String> cmd = connect.async();

            // set操作，成功则返回OK
            cmd.set("keyCai", "value-test");

            cmd.ping();

            // get操作，成功命中则返回对应的value，否则返回null
            cmd.get("keyCai");
            // 删除指定的key
            cmd.del("keyCai", "keyCai1");
            // 获取redis-server信息，内容极为丰富
            cmd.info();

            // 列表操作
            String[] valuelist = {"China", "Americal", "England"};
            // 将一个或多个值插入到列表头部，此处插入多个
            cmd.lpush("listNameCai", valuelist);
            // 移出并获取列表的第一个元素
            System.out.println(cmd.lpop("listNameCai"));
            // 获取列表长度
            System.out.println(cmd.llen("listNameCai"));
            // 通过索引获取列表中的元素
            System.out.println(cmd.lindex("listNameCai", 1));
        }

        return "success";
    }

    @RequestMapping("/syncInvokeCluster")
    public String syncInvokeCluster() {

        // 利用redis-server所绑定的IP和Port创建URI，
        List<RedisURI> redisURIList = new ArrayList<RedisURI>();

        RedisURI redisURI_0 = RedisURI.create("192.168.3.52", 6381);
        redisURI_0.setDatabase(10);

        RedisURI redisURI_1 = RedisURI.create("192.168.3.52", 6379);
        redisURI_1.setDatabase(11);

        RedisURI redisURI_2 = RedisURI.create("192.168.3.51", 7002);
        redisURI_2.setDatabase(12);

        RedisURI redisURI_3 = RedisURI.create("192.168.3.51", 7000);
        redisURI_3.setDatabase(13);

        RedisURI redisURI_4 = RedisURI.create("192.168.3.52", 6380);
        redisURI_4.setDatabase(14);

        RedisURI redisURI_5 = RedisURI.create("192.168.3.51", 7001);
        redisURI_5.setDatabase(15);

        redisURIList.add(redisURI_0);
        redisURIList.add(redisURI_1);
        redisURIList.add(redisURI_2);
        redisURIList.add(redisURI_3);
        redisURIList.add(redisURI_4);
        redisURIList.add(redisURI_5);

        // 创建集Redis集群模式客户端
        RedisClusterClient redisClusterClient = RedisClusterClient.create(redisURIList);
        // 连接到Redis集群
        StatefulRedisClusterConnection<String, String> clusterCon = redisClusterClient.connect();
        // 获取集群同步命令对象
        RedisClusterCommands<String, String> commands = clusterCon.sync();

        // set操作，成功则返回OK
        commands.set("keyCai", "value-test");
        // get操作，成功命中则返回对应的value，否则返回null
        commands.get("keyCai");
        // 删除指定的key
        commands.del("keyCai", "keyCai1");
        // 获取redis-server信息，内容极为丰富
        commands.info();

        // 列表操作
        String[] valuelist = {"China", "Americal", "England"};
        // 将一个或多个值插入到列表头部，此处插入多个
        commands.lpush("listNameCai", valuelist);
        // 移出并获取列表的第一个元素
        System.out.println(commands.lpop("listNameCai"));
        // 获取列表长度
        System.out.println(commands.llen("listNameCai"));
        // 通过索引获取列表中的元素
        System.out.println(commands.lindex("listNameCai", 1));

        return "success";
    }

    @RequestMapping("/asyncInvokeCluster")
    public String asyncInvokeCluster() {

        // 利用redis-server所绑定的IP和Port创建URI，
        List<RedisURI> redisURIList = new ArrayList<RedisURI>();

        RedisURI redisURI_0 = RedisURI.create("192.168.3.52", 6381);
        redisURI_0.setDatabase(10);

        RedisURI redisURI_1 = RedisURI.create("192.168.3.52", 6379);
        redisURI_1.setDatabase(11);

        RedisURI redisURI_2 = RedisURI.create("192.168.3.51", 7002);
        redisURI_2.setDatabase(12);

        RedisURI redisURI_3 = RedisURI.create("192.168.3.51", 7000);
        redisURI_3.setDatabase(13);

        RedisURI redisURI_4 = RedisURI.create("192.168.3.52", 6380);
        redisURI_4.setDatabase(14);

        RedisURI redisURI_5 = RedisURI.create("192.168.3.51", 7001);
        redisURI_5.setDatabase(15);

        redisURIList.add(redisURI_0);
        redisURIList.add(redisURI_1);
        redisURIList.add(redisURI_2);
        redisURIList.add(redisURI_3);
        redisURIList.add(redisURI_4);
        redisURIList.add(redisURI_5);

        // 创建集Redis集群模式客户端
        RedisClusterClient redisClusterClient = RedisClusterClient.create(redisURIList);
        // 连接到Redis集群
        StatefulRedisClusterConnection<String, String> clusterCon = redisClusterClient.connect();
        // 获取集群同步命令对象
        RedisClusterAsyncCommands<String, String> commands = clusterCon.async();

        commands.set("keyCai", "value-test");
        // get操作，成功命中则返回对应的value，否则返回null
        commands.get("keyCai");
        // 删除指定的key
        commands.del("keyCai", "keyCai1");
        // 获取redis-server信息，内容极为丰富
        commands.info();

        // 列表操作
        String[] valuelist = {"China", "Americal", "England"};
        // 将一个或多个值插入到列表头部，此处插入多个
        commands.lpush("listNameCai", valuelist);
        // 移出并获取列表的第一个元素
        System.out.println(commands.lpop("listNameCai"));
        // 获取列表长度
        System.out.println(commands.llen("listNameCai"));
        // 通过索引获取列表中的元素
        System.out.println(commands.lindex("listNameCai", 1));


        // set操作，成功则返回OK
        /*try {
            Long start = new Date().getTime();
            commands.set("keyCai", "value-test").get();
            Long end = new Date().getTime();

            System.out.println("CaiTest : duration = " + (end - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/

        return "success";
    }

}
