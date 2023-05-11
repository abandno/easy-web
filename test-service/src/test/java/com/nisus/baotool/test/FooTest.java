package com.nisus.baotool.test;

import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;

/**
 * @author dafei
 * @version 0.1
 * @date 2023/4/18 23:08
 */
public class FooTest {

    @Test
    public void foo() {
        PriorityQueue<Integer> q = new PriorityQueue<>();
        q.add(100);
        q.add(1);
        q.add(50);
        while (q.isEmpty() == false) {
            System.out.println(q.poll());
        }
    }



}
