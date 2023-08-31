package io.github.abandno.baotool.util.collection;

import cn.hutool.core.comparator.CompareUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nisus
 * @version 0.1
 * @since 2023/5/12 0:13
 */
public class TopQueueTest {
    @Test
    public void foo() {
        TopQueue<Comparable> q = new TopQueue<>(3);
        // TopQueue<Comparable> q = new TopQueue<>(3, (a, b) -> CompareUtil.compare(b, a));
        q.addAll(Arrays.asList(
                2000, 1, 44, 5, 7, 11, 44, 22
        ));

        System.out.println(q);

        System.out.println(q.stream().sorted().collect(Collectors.toList()));

        System.out.println(q.toList());

        System.out.println(q);


    }


    @Test
    public void isNullGreater() {
        List<Integer[]> list = Arrays.asList(new Integer[]{null}, new Integer[]{6}, new Integer[]{2}, new Integer[]{10}, new Integer[]{null}, new Integer[]{22}, new Integer[]{1});
        TopQueue<Integer[]> tpq = new TopQueue<>(3, (a, b) -> CompareUtil.compare(a[0], b[0], true));

        tpq.addAll(list);

        for (Integer[] i : tpq.toList()) {
            System.out.println(i[0]);
        }
    }

    @Test
    public void rank() {
        TopQueue<Integer> q = new TopQueue<>(6);
        // TopQueue<Comparable> q = new TopQueue<>(3, (a, b) -> CompareUtil.compare(b, a));
        q.addAll(Arrays.asList(
                66, 1, 1, 1, 5, 5, 7, 11, 44, 22, 66, 1, 22
        ));

        System.out.println(q);

        System.out.println(q.rank(3));
    }
}