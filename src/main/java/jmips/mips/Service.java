package jmips.mips;

// https://courses.missouristate.edu/KenVollmar/MARS/Help/SyscallHelp.html
public enum Service {
    PRINT_INTEGER(1, Register.A0),
    PRINT_FLOAT(2, Register.F12),
    PRINT_DOUBLE(3, Register.F12),
    PRINT_STRING(4, Register.A0),
    READ_INTEGER(5),
    READ_FLOAT(6),
    READ_DOUBLE(7),
    READ_STRING(8, Register.A0, Register.A1),
    SBRK(9, Register.A0),
    EXIT(10),
    PRINT_CHARACTER(11, Register.A0),
    READ_CHARACTER(12),
    OPEN_FILE(13, Register.A0, Register.A1, Register.A2),
    READ_FROM_FILE(14, Register.A0, Register.A1, Register.A2),
    WRITE_TO_FILE(15, Register.A0, Register.A1, Register.A2),
    CLOSE_FILE(16, Register.A0),
    EXIT2(17, Register.A0),
    TIME(30),
    MIDI_OUT(31, Register.A0, Register.A1, Register.A2, Register.A3),
    SLEEP(32, Register.A0),
    MIDI_OUT_SYNCHRONOUS(33, Register.A0, Register.A1, Register.A2, Register.A3),
    PRINT_INTEGER_IN_HEXADECIMAL(34, Register.A0),
    PRINT_INTEGER_IN_BINARY(35, Register.A0),
    PRINT_INTEGER_AS_UNSIGNED(36, Register.A0),
    SET_SEED(40, Register.A0, Register.A1),
    RANDOM_INT(41, Register.A0),
    RANDOM_INT_RANGE(42, Register.A0, Register.A1),
    RANDOM_FLOAT(43, Register.A0),
    RANDOM_DOUBLE(44, Register.A0),
    CONFIRM_DIALOG(50, Register.A0),
    INPUT_DIALOG_INT(51, Register.A0),
    INPUT_DIALOG_FLOAT(52, Register.A0),
    INPUT_DIALOG_DOUBLE(53, Register.A0),
    INPUT_DIALOG_STRING(54, Register.A0, Register.A1, Register.A2),
    MESSAGE_DIALOG(55, Register.A0, Register.A1),
    MESSAGE_DIALOG_INT(56, Register.A0, Register.A1),
    MESSAGE_DIALOG_FLOAT(57, Register.A0, Register.F12),
    MESSAGE_DIALOG_DOUBLE(58, Register.A0, Register.F12),
    MESSAGE_DIALOG_STRING(59, Register.A0, Register.A1);

    public final int code;
    public final Register[] argumentRegisters;

    Service(int code, Register... argumentRegisters) {
        this.code = code;
        this.argumentRegisters = argumentRegisters;
    }
}
