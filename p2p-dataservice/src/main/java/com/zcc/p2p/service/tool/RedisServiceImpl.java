package com.zcc.p2p.service.tool;

import com.zcc.p2p.common.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author:zcc
 * @data:2019/2/26 0026
 */

@Service(value = "redisServiceImpl")
public class RedisServiceImpl implements RedisService {


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 将验证码存入Redis中
     * @param key
     * @param value
     */
    @Override
    public void set(String key, String value) {

        redisTemplate.opsForValue().set(key,value,60, TimeUnit.SECONDS);

    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Long getOnlyNumber() {
        return redisTemplate.opsForValue().increment(Constant.ONLY_NUMBER,1l);
    }
}
