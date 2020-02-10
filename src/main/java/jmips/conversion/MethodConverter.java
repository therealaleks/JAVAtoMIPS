package jmips.conversion;

import jmips.mips.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.Interpreter;

import java.util.List;
import java.util.function.BiFunction;

public class MethodConverter extends Interpreter<Value> {
    private final ProgramWriter program;
    private int stringCounter = 0;

    public MethodConverter(ProgramWriter program) {
        super(Opcodes.ASM7);
        this.program = program;
    }

    @Override
    public Value newValue(Type type) {
        return null; // TODO
    }

    @Override
    public Value newOperation(AbstractInsnNode insn) {
        switch (insn.getOpcode()) {
            case Opcodes.ACONST_NULL: {
                throw new UnsupportedOperationException("objects not yet implemented");
            }

            case Opcodes.ICONST_M1:
            case Opcodes.ICONST_0:
            case Opcodes.ICONST_1:
            case Opcodes.ICONST_2:
            case Opcodes.ICONST_3:
            case Opcodes.ICONST_4:
            case Opcodes.ICONST_5: {
                return wrap(insn.getOpcode() - Opcodes.ICONST_0);
            }

            case Opcodes.BIPUSH: {
                return wrap(((IntInsnNode) insn).operand);
            }

            case Opcodes.SIPUSH: {
                return wrap(((IntInsnNode) insn).operand);
            }

            case Opcodes.LDC: {
                Object constant = ((LdcInsnNode) insn).cst;

                if (constant instanceof Integer) {
                    return wrap((Integer) constant);
                }

                if (constant instanceof Long) {
                    throw longsNotSupported();
                }

                if (constant instanceof Float) {
                    throw floatsNotSupported();
                }

                if (constant instanceof Double) {
                    throw floatsNotSupported();
                }

                if (constant instanceof String) {
                    return wrap((String) constant);
                }

                if (constant instanceof Type) {
                    throw new UnsupportedOperationException("type constants not supported (don't use lambdas)");
                }

                throw new IllegalArgumentException("unknown constant type " + (constant == null ? null : constant.getClass().getName()));
            }

            case Opcodes.JSR: {
                throw new UnsupportedOperationException("jsr");
            }

            case Opcodes.GETSTATIC: { // TODO: check owner
                return new Label(((FieldInsnNode) insn).name.toLowerCase());
            }

            case Opcodes.NEW: {
                throw new UnsupportedOperationException("objects not yet implemented");
            }

            case Opcodes.LCONST_0:
            case Opcodes.LCONST_1: {
                throw longsNotSupported();
            }

            case Opcodes.FCONST_0:
            case Opcodes.FCONST_1:
            case Opcodes.FCONST_2:
            case Opcodes.DCONST_0:
            case Opcodes.DCONST_1: {
                throw floatsNotSupported();
            }
        }

        throw new IllegalArgumentException("unknown opcode " + insn.getOpcode());
    }

    @Override
    public Value copyOperation(AbstractInsnNode insn, Value value) {
        return value; // TODO: track the variable name
    }

    @Override
    public Value unaryOperation(AbstractInsnNode insn, Value value) {
        switch (insn.getOpcode()) {
            case Opcodes.INEG: {
                if (value instanceof IntegerValue) {
                    return wrap(-unwrap(value));
                }

                Variable variable = new Variable();
                program.instruction(InstructionType.NEGU, variable, value);
                return variable;
            }

            case Opcodes.IINC: {
                program.instruction(InstructionType.ADDI, value, value, wrap(((IincInsnNode) insn).incr));
                return value;
            }

            case Opcodes.I2B:
            case Opcodes.I2C:
            case Opcodes.I2S: {
                return value; // TODO
            }

            case Opcodes.IFEQ:
            case Opcodes.IFNE:
            case Opcodes.IFLT:
            case Opcodes.IFGE:
            case Opcodes.IFGT:
            case Opcodes.IFLE:
            case Opcodes.TABLESWITCH:
            case Opcodes.LOOKUPSWITCH:
            case Opcodes.IRETURN:
            case Opcodes.ARETURN:
            case Opcodes.PUTSTATIC:
            case Opcodes.GETFIELD:
            case Opcodes.NEWARRAY:
            case Opcodes.ANEWARRAY:
            case Opcodes.ARRAYLENGTH:
            case Opcodes.ATHROW:
            case Opcodes.CHECKCAST:
            case Opcodes.INSTANCEOF:
            case Opcodes.MONITORENTER:
            case Opcodes.MONITOREXIT:
            case Opcodes.IFNULL:
            case Opcodes.IFNONNULL: {
                throw notYetImplemented(); // TODO
            }

            case Opcodes.LRETURN:
            case Opcodes.LNEG:
            case Opcodes.I2L:
            case Opcodes.L2I:
            case Opcodes.L2F:
            case Opcodes.L2D:
            case Opcodes.F2L:
            case Opcodes.D2L: {
                throw longsNotSupported();
            }

            case Opcodes.FRETURN:
            case Opcodes.DRETURN:
            case Opcodes.I2F:
            case Opcodes.I2D:
            case Opcodes.F2I:
            case Opcodes.F2D:
            case Opcodes.D2I:
            case Opcodes.D2F:
            case Opcodes.FNEG:
            case Opcodes.DNEG: {
                throw floatsNotSupported();
            }
        }

        throw new IllegalArgumentException("unknown opcode " + insn.getOpcode());
    }

    @Override
    public Value binaryOperation(AbstractInsnNode insn, Value value1, Value value2) {
        boolean immediate1 = value1.isImmediate();
        boolean immediate2 = value2.isImmediate();

        switch (insn.getOpcode()) {
            case Opcodes.IALOAD:
            case Opcodes.LALOAD:
            case Opcodes.FALOAD:
            case Opcodes.DALOAD:
            case Opcodes.AALOAD:
            case Opcodes.BALOAD:
            case Opcodes.CALOAD:
            case Opcodes.SALOAD: {
                throw notYetImplemented();
            }

            case Opcodes.IADD: {
                if (immediate1 && immediate2) {
                    return wrap(unwrap(value1) + unwrap(value2));
                } else if (immediate2) {
                    Variable variable = new Variable();
                    program.instruction(InstructionType.ADDI, variable, value1, value2);
                    return variable;
                } else if (immediate1) {
                    Variable variable = new Variable();
                    program.instruction(InstructionType.ADDI, variable, value2, value1);
                    return variable;
                } else {
                    Variable variable = new Variable();
                    program.instruction(InstructionType.ADD, variable, value1, value2);
                    return variable;
                }
            }

            case Opcodes.ISUB: {
                if (immediate1 && immediate2) {
                    return wrap(unwrap(value1) - unwrap(value2));
                } else if (immediate2) { // x - c = x + -c
                    Variable variable = new Variable();
                    program.instruction(InstructionType.ADDI, variable, value1, wrap(-unwrap(value2)));
                    return variable;
                } else if (immediate1) { // c - x = x + -c
                    Variable variable = new Variable();
                    program.instruction(InstructionType.NEGU, variable, value2);
                    program.instruction(InstructionType.ADDI, variable, variable, value1);
                    return variable;
                } else {
                    Variable variable = new Variable();
                    program.instruction(InstructionType.SUB, variable, value1, value2);
                    return variable;
                }
            }

            case Opcodes.IMUL: {
                if (immediate1 || immediate2) {
                    Value register = immediate2 ? value1 : value2;
                    int immediate = unwrap(immediate2 ? value2 : value1);

                    if (immediate == 2) { // x * 2 = x + x
                        Variable variable = new Variable();
                        program.instruction(InstructionType.ADD, variable, register, register);
                        return variable;
                    }
                }

                return binaryInstruction(InstructionType.REM, (a, b) -> a % b, value1, value2);
            }

            case Opcodes.IDIV: {
                return binaryInstruction(InstructionType.DIV, (a, b) -> a / b, value1, value2);
            }

            case Opcodes.IREM: {
                return binaryInstruction(InstructionType.REM, (a, b) -> a % b, value1, value2);
            }

            case Opcodes.ISHL: {
                return binaryInstruction(InstructionType.SLL, (a, b) -> a << b, value1, value2);
            }

            case Opcodes.ISHR: {
                return binaryInstruction(InstructionType.SRA, (a, b) -> a >> b, value1, value2);
            }

            case Opcodes.IUSHR: {
                return binaryInstruction(InstructionType.SRL, (a, b) -> a >>> b, value1, value2);
            }

            case Opcodes.IAND: {
                return commutativeBinaryInstruction(InstructionType.AND, InstructionType.ANDI, (a, b) -> a & b, value1, value2);
            }

            case Opcodes.IOR: {
                return commutativeBinaryInstruction(InstructionType.OR, InstructionType.ORI, (a, b) -> a | b, value1, value2);
            }

            case Opcodes.IXOR: {
                return commutativeBinaryInstruction(InstructionType.XOR, InstructionType.XORI, (a, b) -> a | b, value1, value2);
            }

            case Opcodes.IF_ICMPEQ:
            case Opcodes.IF_ICMPNE:
            case Opcodes.IF_ICMPLT:
            case Opcodes.IF_ICMPGE:
            case Opcodes.IF_ICMPGT:
            case Opcodes.IF_ICMPLE:
            case Opcodes.IF_ACMPEQ:
            case Opcodes.IF_ACMPNE:
            case Opcodes.PUTFIELD: {
                throw notYetImplemented();
            }

            case Opcodes.LXOR:
            case Opcodes.LCMP:
            case Opcodes.LOR:
            case Opcodes.LAND:
            case Opcodes.LSUB:
            case Opcodes.LMUL:
            case Opcodes.LDIV:
            case Opcodes.LREM:
            case Opcodes.LSHL:
            case Opcodes.LSHR:
            case Opcodes.LUSHR:
            case Opcodes.LADD: {
                throw longsNotSupported();
            }

            case Opcodes.FADD:
            case Opcodes.DADD:
            case Opcodes.FSUB:
            case Opcodes.DSUB:
            case Opcodes.FMUL:
            case Opcodes.DMUL:
            case Opcodes.FDIV:
            case Opcodes.DDIV:
            case Opcodes.FREM:
            case Opcodes.DREM:
            case Opcodes.FCMPL:
            case Opcodes.FCMPG:
            case Opcodes.DCMPL:
            case Opcodes.DCMPG: {
                throw floatsNotSupported();
            }
        }

        throw new IllegalArgumentException("unknown opcode " + insn.getOpcode());
    }

    @Override
    public Value ternaryOperation(AbstractInsnNode insn, Value value1, Value value2, Value value3) {
        switch (insn.getOpcode()) {
            case Opcodes.IASTORE:
            case Opcodes.LASTORE:
            case Opcodes.FASTORE:
            case Opcodes.DASTORE:
            case Opcodes.AASTORE:
            case Opcodes.BASTORE:
            case Opcodes.CASTORE:
            case Opcodes.SASTORE: {
                throw notYetImplemented();
            }
        }

        throw new IllegalArgumentException("unknown opcode " + insn.getOpcode());
    }

    @Override
    public Value naryOperation(AbstractInsnNode insn, List<? extends Value> values) {
        switch (insn.getOpcode()) {
            case Opcodes.INVOKEVIRTUAL:
            case Opcodes.INVOKESPECIAL:
            case Opcodes.INVOKESTATIC:
            case Opcodes.INVOKEINTERFACE: {
                MethodInsnNode methodInsn = (MethodInsnNode) insn;
                String selector = methodInsn.owner + "/" + methodInsn.name + methodInsn.desc;

                if (methodInsn.owner.startsWith("jmips/runtime/")) {
                    switch (selector) {
                        case "jmips/runtime/MipsSystem/print(Ljmips/runtime/MipsBuffer;)V": {
                            program.syscall(Service.PRINT_STRING, values.get(0));
                            return null;
                        }

                        case "jmips/runtime/MipsSystem/print(I)V": {
                            program.syscall(Service.PRINT_INTEGER, values.get(0));
                            return null;
                        }

                        case "jmips/runtime/MipsSystem/(I)V": {
                            program.syscall(Service.PRINT_INTEGER, values.get(0));
                            return null;
                        }

                        case "jmips/runtime/MipsSystem/readInt()I": {
                            program.syscall(Service.READ_INTEGER);
                            Variable variable = new Variable();
                            program.instruction(InstructionType.MOVE, variable, Register.V0);
                            return variable;
                        }

                        default: {
                            throw new UnsupportedOperationException("missing runtime method " + selector);
                        }
                    }
                }

                throw notYetImplemented();
            }

            case Opcodes.MULTIANEWARRAY: {
                throw notYetImplemented();
            }

            case Opcodes.INVOKEDYNAMIC: {
                throw new UnsupportedOperationException("invokedynamic not supported (don't use lambdas)");
            }
        }

        throw new IllegalArgumentException("unknown opcode " + insn.getOpcode());
    }

    @Override
    public void returnOperation(AbstractInsnNode insn, Value value, Value expected) {}

    @Override
    public Value merge(Value value1, Value value2) {
        throw notYetImplemented();
    }

    public Value binaryInstruction(InstructionType instruction, BiFunction<Integer, Integer, Integer> constantImplementation, Value value1, Value value2) {
        boolean immediate1 = value1.isImmediate();
        boolean immediate2 = value2.isImmediate();

        if (immediate1 && immediate2) {
            return new IntegerValue(constantImplementation.apply(((IntegerValue) value1).value, ((IntegerValue) value2).value));
        } else if (immediate2) {
            Variable variable = new Variable();
            program.instruction(InstructionType.LI, variable, value2);
            program.instruction(instruction, variable, value1, variable);
            return variable;
        } else if (immediate1) {
            Variable variable = new Variable();
            program.instruction(InstructionType.LI, variable, value1);
            program.instruction(instruction, variable, value1, variable);
            return variable;
        } else {
            Variable variable = new Variable();
            program.instruction(instruction, variable, value1, value2);
            return variable;
        }
    }

    public Value commutativeBinaryInstruction(InstructionType instruction,
                                              InstructionType immediateInstruction,
                                              BiFunction<Integer, Integer, Integer> constantImplementation,
                                              Value value1,
                                              Value value2) {
        boolean immediate1 = value1.isImmediate();
        boolean immediate2 = value2.isImmediate();

        if (immediate1 && immediate2) {
            return new IntegerValue(constantImplementation.apply(((IntegerValue) value1).value, ((IntegerValue) value2).value));
        } else if (immediate1 || immediate2) {
            Value register = immediate2 ? value1 : value2;
            Value immediate = immediate2 ? value2 : value1;
            Variable variable = new Variable();
            program.instruction(immediateInstruction, variable, register, immediate);
            return variable;
        } else {
            Variable variable = new Variable();
            program.instruction(instruction, variable, value1, value2);
            return variable;
        }
    }

    public List<Statement> getProgram() {
        return program.getProgram();
    }

    private Value wrap(int value) {
        return new IntegerValue(value);
    }

    private int unwrap(Value value) {
        return ((IntegerValue) value).value;
    }

    private Value wrap(String string) {
        // TODO: name assignment based on static final fields present in the class
        return program.string("unnamed_" + stringCounter++, string);
    }

    private UnsupportedOperationException floatsNotSupported() {
        return new UnsupportedOperationException("floating-points not yet implemented");
    }

    private UnsupportedOperationException longsNotSupported() {
        return new UnsupportedOperationException("can't use longs on 32-bit MIPS");
    }

    private UnsupportedOperationException notYetImplemented() {
        return new UnsupportedOperationException("not yet implemented");
    }
}
