package thread;

/**
 * @author 天天摸鱼的java工程师
 * @time 2025年6月4日
 * 多线程环境下ArrayList元素丢失问题复现
 */
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
public class ArrayListThreadUnsafeDemo {
    private static List<String> list = new ArrayList<>(); // 共享的ArrayList实例
    private static final int THREAD_COUNT = 100; // 线程数量
    private static final int ELEMENT_PER_THREAD = 1000; // 每个线程添加的元素数量
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT); // 用于等待所有线程完成
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < ELEMENT_PER_THREAD; j++) {
                        // 生成唯一字符串并添加到list，模拟多线程并发写
                        list.add(UUID.randomUUID().toString());
                    }
                } finally {
                    latch.countDown(); // 线程完成时减少计数器
                }
            }).start();
        }
        latch.await(); // 等待所有线程完成
        System.out.println("预期元素总数：" + (THREAD_COUNT * ELEMENT_PER_THREAD));
        System.out.println("实际元素总数：" + list.size());
        // 输出结果通常小于100000，证明多线程下元素可能丢失
    }
}

