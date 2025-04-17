package alg.stream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Main {

   //线程交替打印123
    public static void main(String[] args) {
        Semaphore s1 = new Semaphore(0);
        Semaphore s2 = new Semaphore(0);
        Semaphore s3 = new Semaphore(0);

        new Thread(() -> {
            while (true) {
                try {
                    System.out.print(1);
                    Thread.sleep(1000);
                    s2.release();
                    s1.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(() -> {
            while (true) {
                try {
                    s2.acquire();

                    System.out.print(2);
                    Thread.sleep(1000);

                    s3.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(() -> {
            while (true) {
                try {
                    s3.acquire();

                    System.out.print(3);
                    Thread.sleep(1000);
                    s1.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();





    }
}
