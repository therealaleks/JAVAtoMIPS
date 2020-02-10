package jmips.mips;

public class Label implements Value {
    public final String name;

    public Label(String name) {
        this.name = name;
    }

    @Override
    public ValueKind getKind() {
        return ValueKind.ADDRESS;
    }

    @Override
    public String toString() {
        return name;
    }
}
