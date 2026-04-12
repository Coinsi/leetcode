package thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CountDownLatchDemo {

    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(5);
        ExecutorService executorService = new ThreadPoolExecutor(6, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());

        for (int i = 0; i < 6; i++) {
            final int num = i + 1;
            Runnable runnable = () -> {
                try {
                    Thread.sleep(num * 1000);
                    System.out.println("运动员 " + num + " 到达了终点。");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            };
            executorService.submit(runnable);
        }

        try {
            System.out.println("裁判员一声令下：各就位！");
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("主线程结束。");

        executorService.shutdown();
    }
}
