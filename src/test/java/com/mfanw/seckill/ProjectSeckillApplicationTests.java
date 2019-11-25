package com.mfanw.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class ProjectSeckillApplicationTests {

    /**
     * 注入响应式的ReactiveRedisTemplate
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        opsForValue.set("my_key", "my_value");
        String mono2 = opsForValue.get("my_key");
        System.out.println(mono2);
        System.out.println("==============");
    }

}
