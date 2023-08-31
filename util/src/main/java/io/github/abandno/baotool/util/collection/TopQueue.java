package io.github.abandno.baotool.util.collection;

import cn.hutool.core.comparator.CompareUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 取 Top K
 * <p>
 * {@link PriorityQueue} 增强，增加元素个数限制，
 * 保留前K个元素(最小, 比较器决定)
 *
 * PriorityQueue#add 虽然接受 Object, 但真的传入一个 new Object,
 * 会报 java.lang.ClassCastException: java.lang.Object cannot be cast to java.lang.Comparable
 * @author L&J
 */
public class TopQueue<E> implements Collection<E> {
    protected final PriorityQueue<E> queue;
    protected final Comparator<E> comparator;
    /**
     * 堆的最大容量,即 top k,所以maxsize=k
     */
    protected final int maxSize;
    /**
     * 提高判断 contains 的效率
     */
    protected final Set<E> elements = new HashSet<>();

    /**
     * 按 comparator 规则排序，取前 K 个
     * @param maxSize    top K
     * @param comparator 比较器
     */
    public TopQueue(int maxSize, Comparator<E> comparator) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException();
        }
        this.maxSize = maxSize;
        this.queue = new PriorityQueue<>(maxSize, comparator.reversed());
        this.comparator = comparator;
    }

    /**
     * 元素必须时 Comparable , 否则, cast Exception
     * @param maxSize top K
     */
    public TopQueue(int maxSize) {
        this(maxSize, (a, b) -> {
            // must be Comparable
            return CompareUtil.compare((Comparable) a, (Comparable) b);
        });
    }

    // public TopQueue() {
    //     this(Integer.MAX_VALUE - 1);
    // }

    /**
     * @param e
     * @return true 添加; false 未添加.
     */
    @Override
    public synchronized boolean add(E e) {
        boolean f = false;
        // 未达到最大容量，直接添加
        if (queue.size() < maxSize) {
            f = queue.add(e);
        } else { // 队列已满
            // 取堆顶元素
            E peek = queue.peek();
            // 将新元素与当前堆顶元素比较，保留较小的元素. queue peek 它眼里的最小，实际是用户眼里的最大
            if (comparator.compare(e, peek) < 0) {
                E poll = this.poll();
                f = queue.add(e);
            }
        }
        if (f) {
            elements.add(e);
        }
        return f;
    }

    public E poll() {
        E poll = this.queue.poll();
        this.elements.remove(poll);
        return poll;
    }

    @Override
    public synchronized boolean remove(Object o) {
        if (!this.contains(o)) {
            return true;
        }
        queue.remove(o);
        elements.remove(o);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c.stream().allMatch(this::contains);
    }

    /**
     * @param c
     * @return true: 全部加入到 queue; false: 非全部加入
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        int i = 0;
        for (E e : c) {
            if (add(e)) {
                i++;
            }
        }

        return c.size() == i;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int i = 0;
        for (Object e : c) {
            if (remove(e)) {
                i++;
            }
        }

        return c.size() == i;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        for (E e : this) {
            if (!c.contains(e)) {
                remove(e);
            }
        }
        return true;
    }

    @Override
    public synchronized void clear() {
        this.queue.clear();
        this.elements.clear();
    }


    @Override
    public boolean contains(Object e) {
        return this.elements.contains(e);
    }

    @Override
    public int size() {
        return this.queue.size();
    }

    @Override
    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    /**
     * 返回 PriorityQueue 的迭代器.
     */
    @Override
    public Iterator<E> iterator() {
        return queue.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.queue.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.queue.toArray(a);
    }

    /**
     * @return 按比较器排序返回
     */
    public List<E> toList() {
        return this.queue.stream().sorted(this.comparator).collect(Collectors.toList());
    }


    /**
     * rank 排序取 top
     * <p>
     * 如果需要用 rank, 就将 maxSize 设置的比 rank 稍大(酌情).
     *
     * 相等的并列排名. top 10 表示取 rank 最前的 10 名
     * <pre>
     * - maxRank: 最大 rank 排名, 例如 10, 取 rank 前 10 .
     *   [1, 2, 2, 3], top 2 => [1, 2]; top rank 2 => [1, 2, 2]
     * - maxSize: 队列最大 size.
     * 两者同时要满足.
     *
     * </pre>
     * @param n top rank n
     */
    public List<E> rank(int n) {
        // 倒叙 list
        List<E> list = this.toList();
        ArrayList<E> res = new ArrayList<>();
        int rank = 0;
        E pre = null;
        for (E e : list) {
            // e vs pre 只会 > 或 = (比较器角度的)，遇到等的 rank 不变，遇到大的 rank++
            if (rank == 0 || comparator.compare(e, pre) > 0) {
                rank++;
            }
            if (rank > n) {
                break;
            }

            res.add(e);
            pre = e;
        }

        return res;
    }


    @Override
    public String toString() {
        return "TopQueue{maxSize=" + this.maxSize + ", queue=" + this.toList() + "}";
    }
}