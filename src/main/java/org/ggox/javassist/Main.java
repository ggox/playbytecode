package org.ggox.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

/**
 * @author: ggox
 * @date: 2022/5/29 16:14
 */
public class Main {

    private static final String CLASS_DIRECTORY = "/Users/ggox/develop/projects/playbytecode/src/main/java/org/ggox/javassist/";

    public static void main(String[] args) throws Exception {
        // 添加方法
        addMethod();

        // 修改方法
        modifyMethod();
    }

    private static void addMethod() throws Exception {
        ClassPool cp = ClassPool.getDefault();
        cp.insertClassPath("./JavassistBean.class");
        CtClass ct = cp.getCtClass(JavassistBean.class.getName());
        CtMethod method = new CtMethod(CtClass.voidType, "foo", new CtClass[]{CtClass.intType, CtClass.longType}, ct);
        method.setModifiers(Modifier.PUBLIC);
        ct.addMethod(method);
        ct.writeFile("./src/main/java/org/ggox/javassist/v2");
        ct.defrost();
    }

    private static void modifyMethod() throws Exception {
        ClassPool cp = ClassPool.getDefault();
        cp.insertClassPath("./JavassistBean.class");
        CtClass ct = cp.getCtClass(JavassistBean.class.getName());
        CtMethod ctMethod = ct.getMethod("test02", "(II)I");
        ctMethod.setBody("return $1 * $2;");
        ct.writeFile("./src/main/java/org/ggox/javassist/v3");
        ct.defrost();
    }
}
