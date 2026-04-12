package test;

import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;

/**

 CPU调压工具

 @author
 */
public class TuningCPU {
    final static int cpus = Runtime.getRuntime().availableProcessors();
    private CoreTest core = new CoreTest();
    ConcurrentHashMap<FullTaskThread, ActivityState> poolRunning = new ConcurrentHashMap<FullTaskThread, ActivityState>();
    ConcurrentHashMap<FullTaskThread, ActivityState> poolIdle = new ConcurrentHashMap<FullTaskThread, ActivityState>();

    public static void main(String[] args) {
        System.out.println("Your system CPU number:" + cpus + "\n");
        int num = 0;
        boolean answerFlag = false;
        TuningCPU tester = new TuningCPU();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input threads:");
        while (true) {
            String input = scanner.nextLine();
            input = input.toLowerCase();
            switch (input) {
                case "exit":
                case "quit":
                    System.exit(0);
                default:
                    if ("n".equals(input)) {
                        continue;
                    } else if (answerFlag && "y".equals(input)) {
                // answerFlag = false;
                    } else {
                        try {
                            num = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            showError();
                            continue;
                        }
                        if (num >= cpus && !answerFlag) {
                            System.out
                                    .println("Warning ! The all CPU will FULL! Continue ? Y/N");
                            answerFlag = true;
                            continue;
                        }
                    }
                    tester.refresh(num);
            }
        }

    }

    private void refresh(int threadNum) {
        if (threadNum < 0)
            return;
        core.execute(threadNum);
        try {
            Thread.sleep(50);
        } catch (Exception e) {
        }
        System.out.println("Current TEST Threads:" + poolRunning.size());
    }

    private static void showError() {
        System.out.println("error input!");
    }

    class CoreTest {
        public void execute(int threadWantNum) {
            if (poolRunning.size() == threadWantNum)
                return;

            int oldRunningSize = poolRunning.size();
            int oldIdleSize = poolIdle.size();
            // 扩容
            if (poolRunning.size() < threadWantNum) {
                int compenzation = threadWantNum - oldRunningSize;

                for (FullTaskThread thread : poolIdle.keySet()) {
                    thread.toRunning(poolIdle.get(thread));
                    poolRunning.put(thread, poolIdle.get(thread));
                    poolIdle.remove(thread);
                    LockSupport.unpark(thread);
                    compenzation--;
                    if (compenzation == 0)
                        break;
                }

                for (int i = 0; i < compenzation; i++) {
                    FullTaskThread thread = new FullTaskThread();
                    poolRunning.put(thread, new ActivityState());
                    thread.start();
                }
            } else {
                // 缩容
                int i = 0;
                for (FullTaskThread thread : poolRunning.keySet()) {
                    if (i < oldRunningSize - threadWantNum) {
                        thread.toIdle(poolRunning.get(thread));
                        Thread.State state = thread.getState();
                        while (true) {
                            if (state == Thread.State.WAITING)
                                break;
                            state = thread.getState();
                        }
                        i++;
                        poolIdle.put(thread, poolRunning.get(thread));
                        poolRunning.remove(thread);
                    } else
                        break;
                }
            }
        }

    }

    enum STATUS {
        INIT, RUNNING, IDLE
    }

    class ActivityState {
        public long p1, p2, p3, p4, p5, p6, p7;
        volatile STATUS flag = STATUS.RUNNING;
        public long p11, p12, p13, p14, p15, p16, p17;

        public STATUS getFlag() {
            return flag;
        }

        public void setFlag(STATUS flag) {
            this.flag = flag;
        }
    }

    class FullTaskThread extends Thread {
        @Override
        public void run() {
            TuningCPU.STATUS flag = ((TuningCPU.ActivityState) poolRunning.get(this)).getFlag();
            while (true) {
                if (flag == TuningCPU.STATUS.RUNNING) {
                    flag = ((TuningCPU.ActivityState) poolRunning.get(this)).getFlag();
                } else if (flag == TuningCPU.STATUS.IDLE) {
                    LockSupport.park();
                    flag = TuningCPU.STATUS.RUNNING;
                }
            }
        }




        public void toIdle(TuningCPU.ActivityState obj) {
            obj.setFlag(TuningCPU.STATUS.IDLE);
        }

        public void toRunning(TuningCPU.ActivityState obj) {
            obj.setFlag(TuningCPU.STATUS.RUNNING);
        }
    }


}