package com.mfanw.seckill.controller;

import com.mfanw.seckill.beans.SecKillBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 抢购
 */
@RestController
public class SecKillController {

    private Logger logger = Logger.getLogger(SecKillController.class.getName());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 请求次数计数器，无用可删除
     */
    private int requestCount = 0;

    /**
     * 全部抢购商品
     */
    private Map<Long, SecKillBean> secKillBeanMap = new HashMap<>();

    /**
     * 抢购
     *
     * @param id 抢购的商品ID
     */
    @GetMapping("/sec_kill")
    public String secKill(@RequestParam Long id, @RequestParam Long userId) {
        logger.warning("秒杀总请求次数" + ++requestCount);
        SecKillBean secKillBean = secKillBeanMap.get(id);
        if (secKillBean == null || secKillBean.getStartTime() == 0) {
            return "暂无抢购";
        }
        long toStart = secKillBean.getStartTime() - System.currentTimeMillis();
        if (toStart > 0) {
            return id + " 抢购暂未开始，距离抢购开始还剩" + toStart / 1000d + "秒";
        }
        Long result = stringRedisTemplate.opsForValue().decrement("goods_" + secKillBean.getId());
        if (result != null && result >= 0) {
            String message = id + " 抢购成功！您的ID是" + userId + "，您是第" + (secKillBean.getGoodsCount() - result) + "位幸运儿！";
            logger.warning(message);
            // 返回成功标志前可进行入库操作，或者进入消息队列延迟入库（这里的入库指代的是存储订单信息到MySQL等数据库）
            return message;
        }
        return id + " 抢购失败！商品可选购数目不足！";
    }

    /**
     * 添加抢购
     *
     * @param id         抢购ID
     * @param startTime  抢购开始时间（标准时间）
     * @param goodsCount 抢购商品总数目
     */
    @GetMapping("/add_sec_kill")
    public String addSecKill(@RequestParam Long id, @RequestParam String startTime, @RequestParam Integer goodsCount) {
        try {
            SecKillBean secKillBean = new SecKillBean();
            secKillBean.setId(id); // 自行注意ID不要重复
            // 设置商品开始抢购时间，默认为一分钟后开始抢购
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
            secKillBean.setStartTime(date.getTime());
            secKillBean.setGoodsCount(goodsCount);
            secKillBeanMap.put(id, secKillBean);
            stringRedisTemplate.opsForValue().set("goods_" + secKillBean.getId(), "" + secKillBean.getGoodsCount());
            logger.warning("当前全部抢购信息：" + secKillBeanMap.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "success";
    }

}
