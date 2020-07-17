package com.tj.practice;

import com.tj.practice.common.util.Base64Util;
import com.tj.practice.common.util.ImgBase64;
import com.tj.practice.common.util.ImgCutter;
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

import java.io.*;
import java.util.*;
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
    private final String DEVICE_ID_CID_KEY = "DEVICE_ID_CID_KEY";
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void hashKeyTest() {
        List<String> list = Arrays.asList("deviceId00", "deviceId01");
        Map<String, List<String>> map = new HashMap<String, List<String>>() {{
            put("deviceId00", Arrays.asList("摄像头id00", "摄像头id01", "摄像头id02"));
            put("deviceId01", Arrays.asList("摄像头id03", "摄像头id04", "摄像头id05", "摄像头id06"));
            put("deviceId02", Arrays.asList("摄像头id07", "摄像头id08", "摄像头id09"));
        }};
        redisUtil.setHashMap(DEVICE_ID_CID_KEY, map);
        Map<String, List<String>> hashMap = redisUtil.getHashMap(DEVICE_ID_CID_KEY);
        log.info("Redis设备id和摄像头id map:{}", hashMap);
        List<String> stringList = new ArrayList<>();
        for (String s : list) {
            List<String> o = hashMap.get(s);
            stringList.addAll(o);
        }
        log.info("摄像头id集合：{}", stringList);
        String deviceId = getDeviceIdByCid(hashMap, "摄像头id03");
        log.info("根据摄像头id 取设备id：{}", deviceId);
    }

    public String getDeviceIdByCid(Map<String, List<String>> hashMap, String Cid) {
        //根据摄像头id 取设备id
        for (String key : hashMap.keySet()) {
            if (hashMap.get(key).contains(Cid)) {
                return key;
            }
        }
        return null;
    }

    @Test
    public void getFileByImgURL() throws Exception {
        String URL = "http://192.168.101.1:19091/staticResource/v1/storage/cycle/fid/1590997048623001999";
//        String URL = "https://192.168.101.1/staticResource/v1/storage/permanent/fid/1590560261111001999";
        String fileBase64 = Base64Util.encodeToString(ImgBase64.getImageBase64(URL));
        log.info("base64图片：{}", fileBase64);
    }

    public static byte[] File2byte(File tradeFile) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    @Test
    public void test() {
        Set<Object> ids = redisUtil.getZSetByRange(KEY, 0, -1);
        log.info("ids:{}", ids);
        List list = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13");
        Set<ZSetOperations.TypedTuple<Object>> idSet = (Set<ZSetOperations.TypedTuple<Object>>) list.parallelStream()
                .filter(id -> (!ids.contains(id)))
                .map(id -> (new DefaultTypedTuple<Object>(id, Double.valueOf(System.currentTimeMillis() / 1000))))
                .collect(Collectors.toSet());
        log.info("idSet:{}", idSet);
        if (idSet.size() > 0) {
            redisUtil.batchAddZset(KEY, idSet);
        }

    }

    @Test
    public void test1() {
        Set<Object> ids = redisUtil.getZSetByRange(KEY, 0, -1);
        log.info("ids:{}", ids);

        Set<Object> set = redisUtil.rangeByScore(KEY, 0, Double.valueOf(System.currentTimeMillis()));
        log.info("set:{}", set.toArray());

        log.info("获取指定成员的score值：{}", redisUtil.score(KEY, "1"));
    }
}
