package jmips.mips;

public class Address implements Value {
    public final Register register;
    public final int offset;

    public Address(Register register, int offset) {
        this.register = register;
        this.offset = offset;
    }

    @Override
    public ValueKind getKind() {
        return ValueKind.ADDRESS;
    }

    @Override
    public String toString() {
        return offset + "(" + register + ")";
    }
}
