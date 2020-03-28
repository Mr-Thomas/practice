package com.tj.practice;

import com.tj.practice.common.util.idWorker.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class Create_Id_test {

    @Autowired
    private IdWorker idWorker;

    /**
     * 随机生成id
     */
    @Test
    public void CreateId(){
        long l = idWorker.nextId();
        System.out.println(l);
    }
}
