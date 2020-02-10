package jmips.mips;

public class StringValue implements Value {
    public final String value;

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public ValueKind getKind() {
        return ValueKind.STRING;
    }

    @Override
    public String toString() {
        return "\"" + value.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"") + "\"";
    }
}
