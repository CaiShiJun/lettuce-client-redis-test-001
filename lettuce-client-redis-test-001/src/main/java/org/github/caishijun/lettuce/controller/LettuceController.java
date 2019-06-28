package org.github.caishijun.lettuce.controller;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


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


            // cmd.auth("dfasdfsads");

            // 列表操作
            String[] valuelist = {"China", "Americal", "England"};
            // 将一个或多个值插入到列表头部，此处插入多个
            cmd.lpush("listNameCai", valuelist);
            // 移出并获取列表的第一个元素
            System.out.println(cmd.lpop("listNameCai"));
            // 获取列表长度
            System.out.println(cmd.llen("listNameCai111"));
            // 通过索引获取列表中的元素
            System.out.println(cmd.lindex("listNameCai", 1));


            cmd.bitcount("keyCai");//bitcount
            cmd.clientList();//没采到
            cmd.commandCount();//command
            cmd.dbsize();//dbsize
            cmd.decr("keyCai");//decr
            cmd.echo("value-test");//echo
            cmd.exists("keyCai");//exists
            cmd.flushdb();//flushdb
            cmd.geopos("keyCai","value-test");//geopos
            cmd.hgetall("keyCai");//hgetall
            cmd.keys("keyCai");//keys
            cmd.mget("keyCai");//mget
            cmd.objectEncoding("keyCai");//object
            cmd.pttl("keyCai");//pttl
            // cmd.reset();//没采到
            cmd.ttl("keyCai");//ttl
            cmd.unlink("keyCai");//unlink
            cmd.watch("keyCai");//watch

            // connect.close();
            redisClient.shutdown();
        }

        return "success";
    }

    @RequestMapping("/asyncInvoke")
    public String asyncInvoke() throws ExecutionException, InterruptedException {

        for (int i = START_DB; i < 16; i++) {
            // 利用redis-server所绑定的IP和Port创建URI，
            RedisURI redisURI = RedisURI.create(REDIS_HOST, REDIS_PORT);
            redisURI.setDatabase(i);

            // 创建集Redis单机模式客户端
            RedisClient redisClient = RedisClient.create(redisURI);//select
            // 开启连接
            StatefulRedisConnection<String, String> connect = redisClient.connect();
            RedisAsyncCommands<String, String> cmd = connect.async();

            // set操作，成功则返回OK
            cmd.set("keyCai", "value-test");//set

            cmd.ping();//ping

            // get操作，成功命中则返回对应的value，否则返回null
            cmd.get("keyCai");//get
            // 删除指定的key
            cmd.del("keyCai", "keyCai1");//del
            // 获取redis-server信息，内容极为丰富
            cmd.info();//info

            // 列表操作
            String[] valuelist = {"China", "Americal", "England"};
            // 将一个或多个值插入到列表头部，此处插入多个
            cmd.lpush("listNameCai", valuelist);//lpush
            // 移出并获取列表的第一个元素
            System.out.println(cmd.lpop("listNameCai"));//lpop
            // 获取列表长度
            System.out.println(cmd.llen("listNameCai"));//llen
            // 通过索引获取列表中的元素
            System.out.println(cmd.lindex("listNameCai", 1));//lindex

            cmd.bitcount("keyCai");//bitcount
            cmd.clientList();//
            cmd.commandCount();//
            cmd.dbsize();//
            cmd.decr("keyCai");//
            cmd.echo("value-test");//
            cmd.exists("keyCai");//
            cmd.flushdb();//
            cmd.geopos("keyCai","value-test");//
            cmd.hgetall("keyCai");//
            cmd.keys("keyCai");//
            cmd.mget("keyCai");//
            cmd.objectEncoding("keyCai");//
            // cmd.reset();//
            cmd.ttl("keyCai");//ttl
            cmd.unlink("keyCai");//unlink
            cmd.watch("keyCai");//watch

            connect.close();
            // redisClient.shutdown();
        }

        return "success";
    }

    @RequestMapping("/reacInvoke")
    public String reacInvoke() {

        for (int i = START_DB; i < 16; i++) {
            // 利用redis-server所绑定的IP和Port创建URI，
            RedisURI redisURI = RedisURI.create(REDIS_HOST, REDIS_PORT);
            redisURI.setDatabase(i);

            // 创建集Redis单机模式客户端
            RedisClient redisClient = RedisClient.create(redisURI);
            // 开启连接
            StatefulRedisConnection<String, String> connect = redisClient.connect();
            RedisReactiveCommands<String,String> reac=  connect.reactive();

            reac.set("key_reac","value_reac").subscribe(System.out::println);

            Mono<String> mono = reac.get("key_reac");
            reac.get("key_reac").subscribe();
            reac.select(0).subscribe();
            reac.auth("key_reac");
            reac.bitcount("key_reac").subscribe();
            reac.bitpos("key_reac",true).subscribe();

            reac.clientList();
            reac.command();

            reac.dbsize();
            reac.decr("key_reac");

            reac.echo("key_reac");
            reac.flushdb();
            reac.getset("key_reac","value_reac");
            reac.hgetall("key_reac");
            reac.info();
            reac.keys("key_reac");
            reac.llen("key_reac");
            reac.lpop("key_reac");
            reac.lindex("key_reac",1);
            reac.lpush("key_reac","value");
            Map<String,String> map=new HashMap<>();
            map.put("k1","v1");
            map.put("k2","v2");
            reac.mset(map);

            reac.ping();
            reac.persist("k1");

            reac.rename("k1","k3");

            reac.ttl("k2");

            reac.watch("k3");

            reac.xtrim("k3",111);
            reac.xdel("k3","v1");

            reac.zrange("k2",111,222);

            connect.close();
            redisClient.shutdown();
        }

        return "success";
    }

    @RequestMapping("/syncInvokeCluster")
    public String syncInvokeCluster() {

        // 利用redis-server所绑定的IP和Port创建URI，
        List<RedisURI> redisURIList = new ArrayList<RedisURI>();

        RedisURI redisURI_0 = RedisURI.create("192.168.2.41", 7001);
        redisURI_0.setDatabase(10);

        RedisURI redisURI_1 = RedisURI.create("192.168.2.41", 7002);
        redisURI_1.setDatabase(11);

        RedisURI redisURI_2 = RedisURI.create("192.168.2.41", 7003);
        redisURI_2.setDatabase(12);

        RedisURI redisURI_3 = RedisURI.create("192.168.2.41", 7004);
        redisURI_3.setDatabase(13);

        RedisURI redisURI_4 = RedisURI.create("192.168.2.41", 7005);
        redisURI_4.setDatabase(14);

        RedisURI redisURI_5 = RedisURI.create("192.168.2.41", 7006);
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

        commands.bitcount("keyCai");//bitcount
        commands.clientList();//
        commands.commandCount();//command
        commands.dbsize();//dbsize
        commands.decr("keyCai");//decr
        commands.echo("value-test");//echo
        commands.exists("keyCai");//exists
        commands.flushdb();//flushdb
        commands.geopos("keyCai","value-test");//geopos
        commands.hgetall("keyCai");//hgetall
        commands.keys("keyCai");//keys
        commands.mget("keyCai");//mget
        commands.objectEncoding("keyCai");//object
        commands.pttl("keyCai");//pttl
        commands.reset();//没采到
        commands.ttl("keyCai");//ttl

        commands.clusterInfo();//
        commands.sort("k");//sort

        clusterCon.close();
        redisClusterClient.shutdown();

        return "success";
    }

    @RequestMapping("/asyncInvokeCluster")
    public String asyncInvokeCluster() {

        // 利用redis-server所绑定的IP和Port创建URI，
        List<RedisURI> redisURIList = new ArrayList<RedisURI>();

        RedisURI redisURI_0 = RedisURI.create("192.168.2.41", 7001);
        redisURI_0.setDatabase(10);

        RedisURI redisURI_1 = RedisURI.create("192.168.2.41", 7002);
        redisURI_1.setDatabase(11);

        RedisURI redisURI_2 = RedisURI.create("192.168.2.41", 7003);
        redisURI_2.setDatabase(12);

        RedisURI redisURI_3 = RedisURI.create("192.168.2.41", 7004);
        redisURI_3.setDatabase(13);

        RedisURI redisURI_4 = RedisURI.create("192.168.2.41", 7005);
        redisURI_4.setDatabase(14);

        RedisURI redisURI_5 = RedisURI.create("192.168.2.41", 7006);
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

        commands.bitcount("keyCai");//bitcount
        commands.clientList();//没采到
        commands.commandCount();//command
        commands.dbsize();//dbsize
        commands.decr("keyCai");//decr
        commands.echo("value-test");//echo
        commands.exists("keyCai");//exists
        commands.flushdb();//flushdb
        commands.geopos("keyCai","value-test");//geopos
        commands.hgetall("keyCai");//hgetall
        commands.keys("keyCai");//keys
        commands.mget("keyCai");//mget
        commands.objectEncoding("keyCai");//object
        commands.pttl("keyCai");//pttl
        commands.reset();//没采到
        commands.ttl("keyCai");//ttl

        commands.clusterInfo();
        commands.sort("k");


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

        clusterCon.close();
        // redisClusterClient.shutdown();

        return "success";
    }

    @RequestMapping("/syncInvokeSentienl")
    public String syncInvokeSentienl() {
        String key="keykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeyke:128key";
        RedisClient redisClient=RedisClient.create("redis-sentinel://192.168.2.62:26301,192.168.2.62:26302,192.168.2.62:26303/0#mymaster");
        StatefulRedisConnection<String,String> connection=redisClient.connect();

        RedisCommands<String,String> commands=connection.sync();
        commands.set("keyCai", "value-test");
        commands.set(key, "value-test");
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

        commands.bitcount("keyCai");//bitcount
        commands.clientList();//没采到
        commands.commandCount();//command
        commands.dbsize();//dbsize
        commands.decr("keyCai");//decr
        commands.echo("value-test");//echo
        commands.exists("keyCai");//exists
        commands.flushdb();//flushdb
        commands.geopos("keyCai","value-test");//geopos
        commands.hgetall("keyCai");//hgetall
        commands.keys("keyCai");//keys
        commands.mget("keyCai");//mget
        commands.objectEncoding("keyCai");//object
        commands.pttl("keyCai");//pttl
        commands.reset();//没采到
        commands.ttl("keyCai");//ttl
        commands.unlink("keyCai");//unlink

        commands.clusterInfo();


        connection.close();
        redisClient.shutdown();
        return "success";
    }

    @RequestMapping("/asyncInvokeSentienl")
    public String asyncInvokeSentienl() {
        RedisClient redisClient=RedisClient.create("redis-sentinel://192.168.2.62:26301,192.168.2.62:26302,192.168.2.62:26303/0#mymaster");
        StatefulRedisConnection<String,String> connection=redisClient.connect();

        RedisAsyncCommands<String,String> commands=connection.async();
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

        commands.bitcount("keyCai");//bitcount
        commands.clientList();//没采到
        commands.commandCount();//command
        commands.dbsize();//dbsize
        commands.decr("keyCai");//decr
        commands.echo("value-test");//echo
        commands.exists("keyCai");//exists
        commands.flushdb();//flushdb
        commands.geopos("keyCai","value-test");//geopos
        commands.hgetall("keyCai");//hgetall
        commands.keys("keyCai");//keys
        commands.mget("keyCai");//mget
        commands.objectEncoding("keyCai");//object
        commands.pttl("keyCai");//pttl
        // commands.reset();//没采到
        commands.ttl("keyCai");//ttl
        commands.unlink("keyCai");//unlink

        commands.clusterInfo();

        connection.close();
        redisClient.shutdown();
        return "success";
    }



}
