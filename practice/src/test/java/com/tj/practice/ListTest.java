package com.tj.practice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.tj.practice.common.util.ListUtil;
import com.tj.practice.model.User;
import com.tj.practice.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/15 11:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ListTest {
    private final String KEY = "DEVICE_KEY";

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void addRedis() {
        List list = Arrays.asList("1", "2", "3", "4", "5");
        list.forEach(ids -> {
            long currentTimestamp = System.currentTimeMillis();
            log.info("currentTimestamp:{}", currentTimestamp);
            redisUtil.add(KEY, ids, currentTimestamp);
        });
    }

    @Test
    public void splitListTest() {
        long startTime = System.currentTimeMillis();
        Set<Object> set = redisUtil.rangeByScore(KEY, 0, startTime);
        if (set.size() > 0) {
            List<String> idList = Lists.newArrayList();
            CollectionUtils.addAll(idList, set.iterator());
            log.info("idList:{}",idList);
            List<List<String>> splitIdList = ListUtil.splitList(idList, 3);
            log.info("splitIdList:{}",splitIdList);
            splitIdList.parallelStream().forEach(ids ->{
                log.info("ids:{}",ids);
            });
        }
    }

    @Test
    public void List2String(){
        List list = Arrays.asList("1", "2", "3", "4", "5");
        String listStr = JSON.toJSONString(list);
        log.info("listStr{}",listStr);

        List lists = JSONArray.parseArray(listStr);
        log.info("lists {}",lists);
    }
}