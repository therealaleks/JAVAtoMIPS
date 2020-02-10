package jmips.mips;

// Based on https://s3-eu-west-1.amazonaws.com/downloads-mips/documents/MD00565-2B-MIPS32-QRC-01.01.pdf
public enum InstructionType {
    /* Arithmetic Operations */
    ADD,
    ADDI,
    ADDIU,
    ADDU,
    CLO,
    CLZ,
    DIV,
    DIVU,
    LA,
    LI,
    LUI,
    MADD,
    MADDU,
    MOVE,
    MSUB,
    MSUBU,
    MUL,
    MULT,
    MULTU,
    NEGU,
    REM,
    REMU,
    SEB,
    SEH,
    SUB,
    SUBU,

    /* Shift and Rotate Operations */
    ROTR,
    ROTRV,
    SLL,
    SLLV,
    SRA,
    SRAV,
    SRL,

    /* Logical and Bit-Field Operations */
    AND,
    ANDI,
    EXT,
    INS,
    NOP,
    NOT,
    OR,
    ORI,
    WSBH,
    XOR,
    XORI,

    /* Condition Testing and Conditional Move Operations */
    MOVN,
    MOVZ,
    SLT,
    SLTI,
    SLTIU,
    SLTU,

    /* Accumulator Access Operations */
    MFHI,
    MFLO,
    MTHI,
    MTLO,

    /* Jumps and Branches */
    B,
    BAL,
    BEQ,
    BEQZ,
    BGEZ,
    BGEZAL,
    BGTZ,
    BLEZ,
    BLTZ,
    BLTZAL,
    BNE,
    BNEZ,
    J,
    JAL,
    JALR,
    JR,

    /* Load and Store Operations */
    LB,
    LBU,
    LH,
    LHU,
    LW,
    LWL,
    LWR,
    SB,
    SH,
    SW,
    SWL,
    SWR,
    ULW,
    USR,
    LL,
    SC,

    /* System Call Operations*/
    SYSCALL;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
