package com.tj.practice;

import com.tj.practice.common.util.ListUtil;
import com.tj.practice.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/15 14:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisTest {

    private final String KEY = "DEVICE_KEY";
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void test(){
        Set<Object> ids = redisUtil.getZSetByRange(KEY, 0, -1);
        log.info("ids:{}",ids);
        List list = Arrays.asList("1","2","3","4","5","6","7","8","9","10","11","12","13");
        Set<ZSetOperations.TypedTuple<Object>> idSet = (Set<ZSetOperations.TypedTuple<Object>>) list.parallelStream()
                .filter(id -> (!ids.contains(id)))
                .map(id -> (new DefaultTypedTuple<Object>(id, Double.valueOf(System.currentTimeMillis() / 1000))))
                .collect(Collectors.toSet());
        log.info("idSet:{}",idSet);
        if(idSet.size()>0){
            redisUtil.batchAddZset(KEY,idSet);
        }

    }

    @Test
    public void test1(){
        Set<Object> ids = redisUtil.getZSetByRange(KEY, 0, -1);
        log.info("ids:{}",ids);

        Set<Object> set = redisUtil.rangeByScore(KEY, 0, Double.valueOf(System.currentTimeMillis()));
        log.info("set:{}",set);
    }
}
