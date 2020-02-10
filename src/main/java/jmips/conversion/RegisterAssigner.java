package jmips.conversion;

import jmips.mips.*;

import java.util.*;

public class RegisterAssigner { // TODO: currently just implemented quickly to get some working code
    private final List<Statement> program;
    private final Map<Variable, Register> registers = new HashMap<>();
    private final List<Register> freeRegisters = new ArrayList<>(Arrays.asList(
            Register.S0,
            Register.S1,
            Register.S2,
            Register.S3,
            Register.S4,
            Register.S5,
            Register.S6,
            Register.S7,
            Register.T1,
            Register.T2,
            Register.T3,
            Register.T4,
            Register.T5,
            Register.T6,
            Register.T7,
            Register.T8,
            Register.T9
    ));

    public RegisterAssigner(List<Statement> program) {
        this.program = program;
    }

    public void assign() {
        for (Statement statement : program) {
            if (statement instanceof Instruction) {
                for (Value argument : ((Instruction) statement).arguments) {
                    if (argument instanceof Variable) {
                        registers.computeIfAbsent((Variable) argument, k -> takeRegister());
                    }
                }
            }
        }
    }

    private Register takeRegister() {
        return freeRegisters.remove(0);
    }

    public List<Statement> getProgram() {
        List<Statement> newProgram = new ArrayList<>();

        for (Statement statement : program) {
            if (statement instanceof Instruction) {
                Instruction instruction = ((Instruction) statement);
                Value[] arguments = instruction.arguments;

                for (int i = 0; i < arguments.length; i++) {
                    Value argument = arguments[i];
                    if (argument instanceof Variable) {
                        argument = registers.get(argument);

                        if (argument == null) {
                            throw new IllegalStateException("variable wasn't assigned register");
                        }

                        arguments[i] = argument;
                    }
                }

                statement = new Instruction(instruction.type, arguments);
            }

            newProgram.add(statement);
        }

        return newProgram;
    }
}
