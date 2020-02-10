package jmips.mips;

public class Directive implements Statement {
    public final DirectiveType type;
    public final Value[] arguments;

    public Directive(DirectiveType type, Value... arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return type + Util.formatArguments(arguments);
    }
}
