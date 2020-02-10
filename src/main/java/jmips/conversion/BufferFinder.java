package jmips.conversion;

import jmips.mips.StringValue;
import jmips.mips.Value;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.analysis.SourceInterpreter;
import org.objectweb.asm.tree.analysis.SourceValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BufferFinder extends SourceInterpreter { // TODO: error checking
    private final ProgramWriter program;
    private final Map<MethodInsnNode, Value> buffers = new HashMap<>();

    public BufferFinder(int api, ProgramWriter program) {
        super(api);
        this.program = program;
    }


    @Override
    public SourceValue unaryOperation(AbstractInsnNode insn, SourceValue value) {
        if (insn.getOpcode() == Opcodes.PUTSTATIC) {
            String bufferName = ((FieldInsnNode) insn).name.toLowerCase();

            if (value.insns.isEmpty()) {
                throw new IllegalStateException();
            }

            AbstractInsnNode valueInsn = value.insns.stream().findFirst().orElseThrow(IllegalStateException::new);
            Value bufferValue = buffers.get(valueInsn);

            if (bufferValue instanceof StringValue) {
                program.string(bufferName, ((StringValue) bufferValue).value);
            }
        }

        return super.unaryOperation(insn, value);
    }

    @Override
    public SourceValue naryOperation(AbstractInsnNode insn, List<? extends SourceValue> values) {
        if (insn.getOpcode() == Opcodes.INVOKESTATIC) {
            MethodInsnNode methodInsn = (MethodInsnNode) insn;
            String selector = methodInsn.owner + "/" + methodInsn.name + methodInsn.desc;

            switch (selector) {
                case "jmips/runtime/MipsSystem/buffer(Ljava/lang/String;)Ljmips/runtime/MipsBuffer;": {
                    AbstractInsnNode argument = values.get(0).insns.stream().findFirst().orElseThrow(() -> new IllegalAccessError("wrong argument count"));

                    if (!(argument instanceof LdcInsnNode)) {
                        throw new IllegalStateException("argument must be constant");
                    }

                    Object constant = ((LdcInsnNode) argument).cst;
                    if (!(constant instanceof String)) {
                        throw new IllegalStateException("wrong argument type");
                    }

                    buffers.put(methodInsn, new StringValue((String) constant));
                }
            }
        }

        return super.naryOperation(insn, values);
    }
}
