package com.tj.practice.client.impl;

import com.tj.practice.client.TestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("testService03")
public class TEST03Service implements TestClient {
    @Override
    public String test() {
        return "测试 Get方式 获取JavaBean03 [testService03]";
    }
}
