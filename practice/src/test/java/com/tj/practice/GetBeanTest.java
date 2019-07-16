package com.tj.practice;

import com.tj.practice.client.TestClient;
import com.tj.practice.core.ApplicationContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GetBeanTest {

    @Test
    public void getBeanTest() {
        //注入testService服务
        TestClient testClient = (TestClient) ApplicationContextHelper.getApplicationContext().getBean(getServiceName());
        String str = testClient.test();
        log.error("GetBean测试: {}", str);
    }

    private String getServiceName() {
        List<String> stringList = Arrays.asList("testService01", "testService02", "testService03");
        Random random = new Random();
        int i = random.nextInt(stringList.size());
        String str = stringList.get(i);
        log.error("服务: {}",str);
        return str;
    }
}
