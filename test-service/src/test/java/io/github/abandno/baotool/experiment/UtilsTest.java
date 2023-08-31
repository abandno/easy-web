package io.github.abandno.baotool.experiment;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UtilsTest {

    @Test
    public void truncate() {
        assertTrue(Utils.truncate(null, 100) == null);
        assertEquals(Utils.truncate("abc", 100), "abc");
        assertEquals(Utils.truncate("abc", 3, "<...>"), "abc");
        assertEquals(Utils.truncate("abc123eftljlj", 5, "<...>"), "abc12");
        assertEquals(Utils.truncate("abc123eftljlj", 6, "<...>"), "a<...>");
        assertEquals(Utils.truncate("abc123eftljlj", 10, "<...>"), "abc12<...>");
    }

    @Test
    public void demo() {
        Utils.toString("origin value", "other if null");
        Utils.toDouble(123.456, 0D);
        Utils.contains(Arrays.asList(1, 2, 3, 4, 5), 3);
        Utils.truncate("abc defg", 3); // abc
    }


}