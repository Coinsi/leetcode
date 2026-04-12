package lru;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;

/**
 * 高并发 LRU 缓存实现，采用分段锁机制。
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class ConcurrentLRUCache<K, V> {

    /**
     * 默认分段数量（必须是 2 的幂，便于位运算计算段索引）
     */
    private static final int DEFAULT_SEGMENTS = 16;

    /**
     * 每个 Segment 内部的双向链表节点
     */
    private static final class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * 缓存分段，每个分段独立维护一个 LRU 链表和锁
     */
    private static class Segment<K, V> {
        // 用于快速查找的并发哈希表
        private final ConcurrentHashMap<K, Node<K, V>> map = new ConcurrentHashMap<>();
        // 保护链表操作的锁
        private final ReentrantLock lock = new ReentrantLock();
        // 双向链表哨兵节点（简化边界处理）
        private final Node<K, V> head;
        private final Node<K, V> tail;
        // 当前分段容量上限
        private final int capacity;
        // 当前分段元素数量（仅在锁内修改，但可无锁读取近似值）
        private volatile int size;

        Segment(int capacity) {
            this.capacity = capacity;
            head = new Node<>(null, null);
            tail = new Node<>(null, null);
            head.next = tail;
            tail.prev = head;
        }

        /**
         * 获取元素，若存在则将其移动到链表头部（最近使用）
         */
        V get(K key) {
            Node<K, V> node = map.get(key);
            if (node == null) {
                return null;
            }
            lock.lock();
            try {
                // 双重检查：在加锁前 node 可能已被移除，需再次确认节点是否仍在 map 中
                if (map.containsKey(key)) {
                    moveToHead(node);
                    return node.value;
                }
                return null;
            } finally {
                lock.unlock();
            }
        }

        /**
         * 放入元素，若键已存在则更新值并移动到头部；
         * 若容量已满，则驱逐最久未使用的元素（链表尾部）
         */
        V put(K key, V value) {
            Node<K, V> node;
            V oldValue = null;
            lock.lock();
            try {
                node = map.get(key);
                if (node != null) {
                    // 更新已有节点
                    oldValue = node.value;
                    node.value = value;
                    moveToHead(node);
                } else {
                    // 新建节点
                    node = new Node<>(key, value);
                    map.put(key, node);
                    addToHead(node);
                    size++;

                    // 超过容量则驱逐尾部节点
                    if (size > capacity) {
                        Node<K, V> removed = removeTail();
                        map.remove(removed.key);
                        size--;
                    }
                }
            } finally {
                lock.unlock();
            }
            return oldValue;
        }

        /**
         * 移除元素
         */
        V remove(K key) {
            Node<K, V> node;
            V value = null;
            lock.lock();
            try {
                node = map.remove(key);
                if (node != null) {
                    value = node.value;
                    unlink(node);
                    size--;
                }
            } finally {
                lock.unlock();
            }
            return value;
        }

        /**
         * 清空当前分段
         */
        void clear() {
            lock.lock();
            try {
                map.clear();
                head.next = tail;
                tail.prev = head;
                size = 0;
            } finally {
                lock.unlock();
            }
        }

        /**
         * 遍历当前分段（仅在持有锁时调用，避免并发修改）
         */
        void forEach(BiConsumer<? super K, ? super V> action) {
            lock.lock();
            try {
                for (Node<K, V> cur = head.next; cur != tail; cur = cur.next) {
                    action.accept(cur.key, cur.value);
                }
            } finally {
                lock.unlock();
            }
        }

        int size() {
            return size; // 近似值，高并发下可能略有不准确
        }

        // ----- 链表操作（必须在持有锁时调用）-----

        private void addToHead(Node<K, V> node) {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }

        private void unlink(Node<K, V> node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        private void moveToHead(Node<K, V> node) {
            unlink(node);
            addToHead(node);
        }

        private Node<K, V> removeTail() {
            Node<K, V> last = tail.prev;
            unlink(last);
            return last;
        }
    }

    // ----- 主缓存 -----
    private final Segment<K, V>[] segments;
    private final int segmentMask;
    private final int segmentShift;
    private final int totalCapacity;

    /**
     * 创建支持高并发的 LRU 缓存
     *
     * @param capacity     最大容量
     * @param concurrencyLevel 并发级别（分段数），实际会调整为不小于该值的 2 的幂
     */
    public ConcurrentLRUCache(int capacity, int concurrencyLevel) {
        if (capacity <= 0 || concurrencyLevel <= 0) {
            throw new IllegalArgumentException();
        }

        // 计算分段数量（取不小于 concurrencyLevel 的 2 的幂）
        int ssize = 1;
        while (ssize < concurrencyLevel) {
            ssize <<= 1;
        }
        this.segments = new Segment[ssize];
        this.segmentShift = 32 - Integer.numberOfTrailingZeros(ssize);
        this.segmentMask = ssize - 1;
        this.totalCapacity = capacity;

        // 平均分配容量，剩余部分追加到第一个分段
        int baseCap = capacity / ssize;
        int remainder = capacity % ssize;
        for (int i = 0; i < ssize; i++) {
            int segCap = baseCap + (i == 0 ? remainder : 0);
            segments[i] = new Segment<>(segCap);
        }
    }

    /**
     * 使用默认并发级别（16）创建缓存
     */
    public ConcurrentLRUCache(int capacity) {
        this(capacity, DEFAULT_SEGMENTS);
    }

    /**
     * 根据键的哈希值定位所属分段
     */
    private Segment<K, V> segmentFor(K key) {
        int hash = key.hashCode();
        // 使用 Wang/Jenkins hash 的变体分散高位影响
        hash ^= (hash >>> 20) ^ (hash >>> 12);
        hash ^= (hash >>> 7) ^ (hash >>> 4);
        return segments[(hash >>> segmentShift) & segmentMask];
    }

    public V get(K key) {
        return segmentFor(key).get(key);
    }

    public V put(K key, V value) {
        return segmentFor(key).put(key, value);
    }

    public V remove(K key) {
        return segmentFor(key).remove(key);
    }

    public void clear() {
        for (Segment<K, V> seg : segments) {
            seg.clear();
        }
    }

    /**
     * 遍历缓存中的所有条目（顺序不保证）
     */
    public void forEach(BiConsumer<? super K, ? super V> action) {
        for (Segment<K, V> seg : segments) {
            seg.forEach(action);
        }
    }

    /**
     * 获取当前近似大小（高并发下可能不精确）
     */
    public int size() {
        int total = 0;
        for (Segment<K, V> seg : segments) {
            total += seg.size();
        }
        return total;
    }

    /**
     * 返回最大容量
     */
    public int capacity() {
        return totalCapacity;
    }

    // ----- 简单测试 -----
    public static void main(String[] args) throws InterruptedException {
        ConcurrentLRUCache<Integer, String> cache = new ConcurrentLRUCache<>(100);

        // 基本功能测试
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");
        System.out.println(cache.get(1)); // one
        System.out.println(cache.get(2)); // two
        cache.put(4, "four"); // 可能触发驱逐
        System.out.println(cache.get(3)); // 可能为 null（若被驱逐）

        // 并发测试
        int threadCount = 20;
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            final int tid = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    int key = (tid * 1000 + j) % 200;
                    cache.put(key, "val-" + key);
                    cache.get(key);
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Final size approx: " + cache.size());
        cache.forEach((k, v) -> System.out.println(k + " -> " + v));
    }
}