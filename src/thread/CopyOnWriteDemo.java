package thread;

/**
 *
 * 读多写少场景下的线程安全方案
 */
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
public class CopyOnWriteDemo {
    private static List<String> cowList = new CopyOnWriteArrayList<>(); // 写时复制列表
    public static void main(String[] args) {
        // 写操作：创建新数组并复制原数据，保证线程安全
        cowList.add("write once");
        // 读操作：直接访问底层数组，无需加锁，效率高
        for (String s : cowList) {
            System.out.println(s);
        }
    }
}
