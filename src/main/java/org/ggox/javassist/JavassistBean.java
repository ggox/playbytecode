package org.ggox.javassist;

/**
 * Javassist 测试 bean
 *
 * @author: ggox
 * @date: 2022/5/29 16:13
 */
public class JavassistBean {

    private int a = 10;

    private void test01() {
        System.out.println("hello world");
    }

    private int test02(int a, int b) {
        return a + b;
    }
}
