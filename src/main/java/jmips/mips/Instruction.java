package jmips.mips;

public class Instruction implements Statement {
    public final InstructionType type;
    public final Value[] arguments;

    public Instruction(InstructionType type, Value... arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return type + Util.formatArguments(arguments);
    }
}
