package jmips.mips;

public enum Register implements SettableValue, Value {
    /* Zero */
    ZERO,

    /* Assembler Temporary */
    AT,

    /* Result */
    V0,
    V1,

    /* Argument */
    A0,
    A1,
    A2,
    A3,

    /* Temporary */
    T0,
    T1,
    T2,
    T3,
    T4,
    T5,
    T6,
    T7,

    /* Saved */
    S0,
    S1,
    S2,
    S3,
    S4,
    S5,
    S6,
    S7,

    /* Temporary */
    T8,
    T9,

    /* Operating System */
    K0,
    K1,

    /* Global Pointer */
    GP,

    /* Stack Pointer */
    SP,

    /* Frame Pointer */
    FP,

    /* Return Address */
    RA,

    // TODO: floating point registers
    F12;


    @Override
    public ValueKind getKind() {
        return ValueKind.REGISTER;
    }

    @Override
    public String toString() {
        return "$" + name().toLowerCase();
    }
}
