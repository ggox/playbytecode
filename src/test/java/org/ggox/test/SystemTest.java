package org.ggox.test;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author: ggox
 * @date: 2022/5/29 15:55
 */
public class SystemTest {

    @Test
    public void test_nanoTime() throws InterruptedException {
        long startTs = System.nanoTime();
        TimeUnit.SECONDS.sleep(1);
        long endTs = System.nanoTime();
        System.out.println(endTs - startTs);
    }
}
