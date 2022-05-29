package org.ggox.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author: ggox
 * @date: 2022/5/29 16:46
 */
public class PathTest {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("/Users/ggox/develop/projects/playbytecode/src/test/java/org/ggox/test/PathTest.java");
        Path path1 = path.resolveSibling("SystemTest.java");
        Files.readAllLines(path1).forEach(System.out::println);
    }
}
