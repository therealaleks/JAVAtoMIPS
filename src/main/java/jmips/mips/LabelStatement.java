package jmips.mips;

public class LabelStatement implements Statement {
    public final Label label;

    public LabelStatement(Label label) {
        this.label = label;
    }

    public LabelStatement(String name) {
        this(new Label(name));
    }

    @Override
    public String toString() {
        return label + ":";
    }
}
