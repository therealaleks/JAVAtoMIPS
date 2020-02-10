package jmips.mips;

// Based on mars.assembler.Directives
public enum DirectiveType {
    DATA,
    TEXT,
    WORD,
    ASCII,
    ASCIIZ,
    BYTE,
    ALIGN,
    HALF,
    SPACE,
    DOUBLE,
    FLOAT,
    EXTERN,
    KDATA,
    KTEXT,
    GLOBL,
    SET,
    EQV,
    MACRO,
    END_MACRO,
    INCLUDE;

    @Override
    public String toString() {
        return "." + name().toLowerCase();
    }
}
