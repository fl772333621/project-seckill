package com.mfanw.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@SpringBootTest
class ProjectSeckillApplicationTests {

    private Logger logger = Logger.getLogger(ProjectSeckillApplicationTests.class.getName());

    @Autowired
    private RestTemplate restTemplate;

    private volatile int allCount = 0;

    /**
     * 添加抢购商品
     */
    @Test
    void addSecKillTest() {
        String url = "http://127.0.0.1:8080/add_sec_kill?id=1&startTime=2019-11-25 18:12:00&goodsCount=20";
        String msg = restTemplate.getForObject(url, String.class);
        logger.warning(msg);
    }

    /**
     * 开始抢购
     */
    @Test
    void secKillTest() throws Exception {
        int threadCount = 1000;
        int killCount = 10;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            final int fi = i;
            new Thread(() -> {
                for (int c = 0; c < killCount; c++) {
                    String url = "http://127.0.0.1:8080/sec_kill?id=1&userId=" + fi;
                    String msg = restTemplate.getForObject(url, String.class);
                    double qps = ++allCount * 1000D / (System.currentTimeMillis() - startTime);
                    logger.warning("name:" + fi + " count:" + c + " allCount:" + allCount + " QPS:" + qps + " MSG" + msg);
                }
            }).start();
        }
        Thread.sleep(1000 * 60);
    }

}
