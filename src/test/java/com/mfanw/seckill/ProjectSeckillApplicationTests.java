package com.mfanw.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
class ProjectSeckillApplicationTests {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 注入响应式的ReactiveRedisTemplate
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testRedis() {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        opsForValue.set("my_key", "my_value");
        String mono2 = opsForValue.get("my_key");
        System.out.println(mono2);
        System.out.println("==============");
        for (int i = 0; i < 10; i++) {
            Long result = redisTemplate.opsForValue().increment("time");
            System.out.println(i + "\t" + result);
        }
    }

}
