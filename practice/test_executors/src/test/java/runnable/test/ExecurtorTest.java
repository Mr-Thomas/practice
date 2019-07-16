package runnable.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Created by Nancy on 2019/7/13 16:01
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ExecurtorTest {

    //创建固定数量线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Test
    public void TestExecutor() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            System.out.println("创建线程: "+i);
            Runnable runnable = new Runnable(){
                @Override
                public void run() {
                    System.out.println("启动线程");
                }
            };
            // 在未来某个时间执行给定的命令
            executorService.execute(runnable);
        }
        //关闭线程
        executorService.shutdown();
        //等待子线程结束，再继续执行下面的代码
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        System.out.println("all thread complete");
    }
}
