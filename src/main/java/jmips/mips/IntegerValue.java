package jmips.mips;

public class IntegerValue implements Value {
    public final int value;

    public IntegerValue(int value) {
        this.value = value;
    }

    @Override
    public ValueKind getKind() {
        return ValueKind.IMMEDIATE;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
