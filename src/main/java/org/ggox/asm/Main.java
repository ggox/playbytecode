package org.ggox.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import static org.objectweb.asm.ClassReader.SKIP_CODE;
import static org.objectweb.asm.ClassReader.SKIP_DEBUG;
import static org.objectweb.asm.ClassReader.SKIP_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ASM9;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.IRETURN;

/**
 * 主方法
 *
 * @author: ggox
 * @date: 2022/5/28 15:38
 */
public class Main {

    private static final String CLASS_DIRECTORY = "/Users/ggox/develop/projects/playbytecode/src/main/java/org/ggox/asm/";

    public static void main(String[] args) {
        // 打印字段和方法
        printFieldsAndMethods();

        // 新增字段
        addNewField();

        // 新增方法
        addNewMethod();

        // 移除方法
        removeMethod("xyz");

        // 修改方法内容
        modifyMethodContent();

        // 方法前后增加日志打印
        addLogAroundMethodV1();

        // 增加 try-catch-finally 逻辑
        addLogAroundMethodV2();
    }

    private static void printFieldsAndMethods() {
        byte[] bytes = loadBytes(CLASS_DIRECTORY + "AsmBean.class");
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(0);
        ClassVisitor cv = new ClassVisitor(ASM9, cw) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                System.out.println("version: " + version + " name: " + name + " signature: " + signature + " super: " + superName + " interfaces: " + Arrays.toString(interfaces));
                super.visit(version, access, name, signature, superName, interfaces);
            }

            @Override
            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                System.out.print("field: " + name);
                System.out.print(" descriptor: " + descriptor);
                System.out.println(" signature: " + signature);
                return super.visitField(access, name, descriptor, signature, value);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                System.out.print("method: " + name);
                System.out.print(" descriptor: " + descriptor);
                System.out.println(" signature: " + signature);
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        };
        cr.accept(cv, SKIP_CODE | SKIP_DEBUG);
    }

    private static void addNewField() {
        byte[] bytes = loadBytes(CLASS_DIRECTORY + "AsmBean.class");
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(0);
        ClassVisitor cv = new ClassVisitor(ASM9, cw) {
            @Override
            public void visitEnd() {
                super.visitEnd();
                FieldVisitor fv = cv.visitField(ACC_PUBLIC, "xyz", "Ljava/lang/Object;", "TR;", null);
                if (fv != null) {
                    fv.visitEnd();
                }
            }
        };
        cr.accept(cv, SKIP_CODE | SKIP_DEBUG);
        byte[] v2 = cw.toByteArray();
        storeBytes(v2, CLASS_DIRECTORY + "AsmBeanV2.class");
    }

    private static void addNewMethod() {
        byte[] bytes = loadBytes(CLASS_DIRECTORY + "AsmBean.class");
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(0);

        ClassVisitor cv = new ClassVisitor(ASM9, cw) {
            @Override
            public void visitEnd() {
                super.visitEnd();
                MethodVisitor mv = cv.visitMethod(ACC_PROTECTED, "xyz", "(JLjava/util/Map;)V", "(JLjava/util/Map<Ljava/lang/String;[Ljava/lang/Object;>;)V", new String[]{"Ljava/lang/RuntimeException;"});
                if (mv != null) {
                    mv.visitEnd();
                }
            }
        };
        cr.accept(cv, SKIP_CODE | SKIP_DEBUG);
        byte[] v2 = cw.toByteArray();
        storeBytes(v2, CLASS_DIRECTORY + "AsmBeanV3.class");
    }

    private static void removeMethod(String methodName) {
        byte[] bytes = loadBytes(CLASS_DIRECTORY + "AsmBeanV3.class");
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(0);

        ClassVisitor cv = new ClassVisitor(ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                if (Objects.equals(methodName, name)) {
                    return null;
                }
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        };
        cr.accept(cv, SKIP_CODE | SKIP_DEBUG);
        byte[] v2 = cw.toByteArray();
        storeBytes(v2, CLASS_DIRECTORY + "AsmBeanV4.class");
    }

    private static void modifyMethodContent() {
        byte[] bytes = loadBytes(CLASS_DIRECTORY + "AsmBean.class");
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
        ClassVisitor cvv = new ClassVisitor(ASM9, cw) {

            private int access;
            private String name;
            private String descriptor;
            private String signature;
            private String[] exceptions;

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                if ("test01".equals(name)) {
                    this.access = access;
                    this.name = name;
                    this.descriptor = descriptor;
                    this.signature = signature;
                    this.exceptions = exceptions;
                    return null;
                }
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }

            @Override
            public void visitEnd() {
                super.visitEnd();
                MethodVisitor mv = cv.visitMethod(this.access, this.name, this.descriptor, this.signature, this.exceptions);
                if (mv != null) {
                    mv.visitCode();
                    mv.visitVarInsn(ILOAD, 1);
                    mv.visitIntInsn(BIPUSH, 100);
                    mv.visitInsn(IADD);
                    mv.visitInsn(IRETURN);
                    mv.visitMaxs(0, 0);
                }
            }
        };
        cr.accept(cvv, SKIP_CODE | SKIP_DEBUG);
        byte[] v2 = cw.toByteArray();
        storeBytes(v2, CLASS_DIRECTORY + "AsmBeanV5.class");
    }

    private static void addLogAroundMethodV1() {
        byte[] bytes = loadBytes(CLASS_DIRECTORY + "AsmBean.class");
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(COMPUTE_FRAMES | COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                if (!"test04".equals(name)) {
                    return mv;
                }
                return new AdviceAdapter(ASM9, mv, access, name, descriptor) {

                    @Override
                    protected void onMethodEnter() {
                        super.onMethodEnter();
                        // 新增 System.out.println("enter " + name);
                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        mv.visitLdcInsn("enter " + name);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                    }

                    @Override
                    protected void onMethodExit(int opcode) {
                        super.onMethodExit(opcode);
                        // 新增 System.out.println("[normal,err] exit " + name);
                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        if (opcode == Opcodes.ATHROW) {
                            mv.visitLdcInsn("err exit " + name);
                        } else {
                            mv.visitLdcInsn("normal exit " + name);
                        }
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                    }
                };
            }
        };
        cr.accept(cv, SKIP_FRAMES);
        byte[] v2 = cw.toByteArray();
        storeBytes(v2, CLASS_DIRECTORY + "AsmBeanV6.class");
    }

    private static void addLogAroundMethodV2() {
        byte[] bytes = loadBytes(CLASS_DIRECTORY + "AsmBean.class");
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(COMPUTE_FRAMES | COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                if (!"test04".equals(name)) {
                    return mv;
                }
                return new AdviceAdapter(ASM9, mv, access, name, descriptor) {

                    private Label startLabel;

                    @Override
                    protected void onMethodEnter() {
                        super.onMethodEnter();
                        startLabel = new Label();
                        mv.visitLabel(startLabel);
                        // 新增 System.out.println("enter " + name);
                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        mv.visitLdcInsn("enter " + name);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                    }

                    @Override
                    public void visitMaxs(int maxStack, int maxLocals) {
                        Label endLabel = new Label();
                        mv.visitTryCatchBlock(startLabel, endLabel, endLabel, null);
                        mv.visitLabel(endLabel);

                        // 生成 finally 代码
                        finallyBlock(ATHROW);
                        mv.visitInsn(ATHROW);
                        super.visitMaxs(maxStack, maxLocals);
                    }

                    private void finallyBlock(int opcode) {
                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        if (opcode == ATHROW) {
                            mv.visitLdcInsn("err exit " + name);
                        } else {
                            mv.visitLdcInsn("normal exit " + name);
                        }
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                    }

                    @Override
                    protected void onMethodExit(int opcode) {
                        super.onMethodExit(opcode);
                        // 不重复处理
                        if (opcode != ATHROW) {
                            finallyBlock(opcode);
                        }
                    }
                };
            }
        };
        cr.accept(cv, ClassReader.SKIP_FRAMES);
        byte[] v2 = cw.toByteArray();
        storeBytes(v2, CLASS_DIRECTORY + "AsmBeanV7.class");
    }

    static byte[] loadBytes(String fileName) {
        try {
            return Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void storeBytes(byte[] bytes, String fileName) {
        try {
            Files.write(Paths.get(fileName), bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}