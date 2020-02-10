package jmips.conversion;

import jmips.mips.Statement;
import org.objectweb.asm.ClassReader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class ConverterTest {
    public static void main(String[] args) throws Throwable {
        try (InputStream in = new FileInputStream("test\\production\\jmipstest\\Main.class")) {
            ClassConverter converter = new ClassConverter();
            new ClassReader(in).accept(converter, 0);
            List<Statement> program = converter.getProgram();

            RegisterAssigner registerAssigner = new RegisterAssigner(program);
            registerAssigner.assign();
            program = registerAssigner.getProgram();

            for (Statement statement : program) {
                System.out.println(statement);
            }
        }
    }
}
