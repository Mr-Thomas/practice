package runnable.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Nancy on 2019/7/13 15:38
 */
public class ExecutorServiceTest {

    //创建固定线程池
    static ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(0);
            }
        });
    }

}
