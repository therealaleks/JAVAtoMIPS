package jmips.mips;

/**
 * A named variable, which will be transformed into a register with a comment by the
 * register-assignment pass.
 */
public class Variable implements SettableValue, Value {
    public static int counter = 0;
    public final String description;
    private final int id;

    public Variable(String description) {
        this.description = description;
        id = -1;
    }

    public Variable() {
        description = null;
        id = counter++;
    }

    @Override
    public ValueKind getKind() {
        return ValueKind.REGISTER;
    }

    @Override
    public String toString() {
        return "{" + (description != null ? description : "var" + id) + "}";
    }
}
