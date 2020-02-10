package jmips.conversion;

import jmips.mips.Label;
import jmips.mips.Service;
import jmips.mips.Statement;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.util.*;

public class ClassConverter extends ClassVisitor {
    private final ProgramWriter program = new ProgramWriter();
    private String className;
    private List<MethodNode> methods = new ArrayList<>();

    public ClassConverter() {
        super(Opcodes.ASM7);
    }

    public List<Statement> getProgram() {
        return program.getProgram();
    }

    @Override
    public void visitEnd() {
        methods.sort(Comparator.comparingInt(method -> {
            if (method.name.equals("<clinit>")) {
                return -2;
            }

            if (method.name.equals("main") && method.desc.equals("([Ljava/lang/String;)V")) {
                return -1;
            }

            return 0;
        }));


        Set<String> takenNames = new HashSet<>();
        for (MethodNode method : methods) {
            if (!takenNames.add(method.name)) {
                throw new UnsupportedOperationException("methods must all have different names");
            }

            if (method.name.equals("<clinit>")) {
                try {
                    new Analyzer<>(new BufferFinder(api, program)).analyze(className, method);
                } catch (AnalyzerException e) {
                    throw new RuntimeException(e);
                }

                continue;
            }

            if (method.name.equals("<init>")) {
                continue;
            }

            if (!method.name.equals("main")) {
                program.label(new Label(getLabelName(method.name)));
            }

            try {
                MethodConverter converter = new MethodConverter(program);
                new Analyzer<>(converter).analyze(className, method);
            } catch (AnalyzerException e) {
                throw new RuntimeException(e);
            }

            if (method.name.equals("main")) {
                program.syscall(Service.EXIT);
            }
        }

    }

    private String getLabelName(String name) {
        return name; // TODO: camelCase -> snake_case
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        className = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodNode methodNode = new MethodNode(api, access, name, descriptor, signature, exceptions);
        methods.add(methodNode);
        return methodNode;
    }
}
