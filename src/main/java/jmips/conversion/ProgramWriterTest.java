package jmips.conversion;

import jmips.mips.Service;

public class ProgramWriterTest {
    public static void main(String[] args) {
        ProgramWriter program = new ProgramWriter();
        program.syscall(Service.PRINT_STRING, program.string("test_string", "Test"));
        program.syscall(Service.EXIT);
        System.out.println(program);
    }
}
