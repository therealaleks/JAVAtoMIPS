package jmips.conversion;

import jmips.mips.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class ProgramWriter {
    private final List<Statement> dataSection = new ArrayList<>();
    private final List<Statement> textSection = new ArrayList<>();
    private final Map<String, String> strings = new HashMap<>();
    private int unnamedStringCounter = 0;

    public List<Statement> getProgram() {
        List<Statement> program = new ArrayList<>();

        program.add(new Directive(DirectiveType.DATA));
        program.addAll(dataSection);

        program.add(new Directive(DirectiveType.TEXT));
        program.addAll(textSection);

        return program;
    }

    public void syscall(Service service, Value... arguments) {
        if (arguments.length != service.argumentRegisters.length) {
            throw new IllegalArgumentException("wrong argument count (got " + arguments.length + " expected " + service.argumentRegisters.length + ")");
        }

        set(Register.V0, service.code);

        for (int i = 0; i < arguments.length; i++) {
            set(service.argumentRegisters[i], arguments[i]);
        }

        instruction(InstructionType.SYSCALL);
    }

    public void set(Register register, Value value) {
        switch (value.getKind()) {
            case IMMEDIATE: {
                instruction(InstructionType.LI, register, value);
                break;
            }

            case STRING: {
                instruction(InstructionType.LI, register, string("string_" + unnamedStringCounter++, ((StringValue) value).value));
                break;
            }

            case ADDRESS: {
                instruction(InstructionType.LA, register, value);
                break;
            }

            case REGISTER: {
                if (register != value) {
                    instruction(InstructionType.MOVE, register, value);
                }

                break;
            }
        }
    }

    public void set(Register register, int value) {
        set(register, new IntegerValue(value));
    }

    public Label string(String name, String value) {
        String oldValue = strings.get(name);

        if (oldValue == null) {
            strings.put(name, value);
            dataSection.add(new LabelStatement(name));
            dataSection.add(new Directive(DirectiveType.ASCIIZ, new StringValue(value)));
        } else if (!oldValue.equals(value)) {
            throw new IllegalArgumentException("name already taken by string with different value");
        }

        return new Label(name);
    }

    public void label(Label label) {
        dataSection.add(new LabelStatement(label));
    }

    public void instruction(InstructionType type, Value... arguments) {
        textSection.add(new Instruction(type, arguments));
    }

    public void directive(DirectiveType type, Value... arguments) {
        textSection.add(new Directive(type, arguments));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (Statement statement : getProgram()) {
            result.append(statement);
            result.append("\n");
        }

        return result.toString();
    }
}
