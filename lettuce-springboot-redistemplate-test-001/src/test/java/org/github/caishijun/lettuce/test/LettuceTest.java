package org.github.caishijun.lettuce.test;

import org.github.caishijun.lettuce.test.http.HttpClientUtils;
import org.junit.Test;

public class LettuceTest {
    private static String HOST = "localhost";
    private static int PORT = 8080;

    private static int FOR_TIMES = 1;
    private static int SLEEP_TIME = 5000;

    public static String getUrl(String uri) {
        return "http://" + HOST + ":" + PORT + uri;
    }

    @Test
    public void stringRedisTemplate() throws Exception {
        for (int i = 0; i < FOR_TIMES; i++) {
            HttpClientUtils.sendGetRequest(getUrl("/stringRedisTemplate"));
            Thread.sleep(SLEEP_TIME);
        }
    }

    @Test
    public void redisTemplate() throws Exception {
        for (int i = 0; i < FOR_TIMES; i++) {
            HttpClientUtils.sendGetRequest(getUrl("/redisTemplate"));
            Thread.sleep(SLEEP_TIME);
        }
    }

    @Test
    public void runAll() throws Exception {
        while (true) {
            stringRedisTemplate();
            redisTemplate();
        }
    }
}
