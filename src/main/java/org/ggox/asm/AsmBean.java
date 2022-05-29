package org.ggox.asm;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * @author: ggox
 * @date: 2022/5/28 15:38
 */
public class AsmBean<R> {

    private int a = 0;
    private R b;
    private R[] arr;

    private Map<String, R> map;

    private int test01(int a) {
        return a;
    }

    @AsmDemo
    private void test02(@AsmDemo String a) {

    }

    private <T> void test03(T t) {

    }

    private void test04(Map<String, Object[]> map) {
        System.out.println("040404");
        if (map != null) {
            throw new RuntimeException("test");
        } else {
            System.out.println(1 / 0);
        }
    }

    private <S> S test05(Map<S, R> t) {
        return null;
    }

    /**
     * @author: ggox
     * @date: 2022/5/28 17:47
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
    @interface AsmDemo {
    }
}
