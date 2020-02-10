package jmips.mips;

public interface Value extends org.objectweb.asm.tree.analysis.Value {
    ValueKind getKind();

    @Override
    default int getSize() {
        return 1;
    }

    default boolean isImmediate() {
        return getKind() == ValueKind.IMMEDIATE;
    }
}
