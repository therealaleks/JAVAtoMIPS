package jmips.mips;

public class Util {
    public static String formatArguments(Value[] arguments) {
        StringBuilder result = new StringBuilder();

        boolean first = true;
        for (Object argument : arguments) {
            result.append(first ? " " : ", ");
            result.append(argument);
            first = false;
        }

        return result.toString();
    }
}
