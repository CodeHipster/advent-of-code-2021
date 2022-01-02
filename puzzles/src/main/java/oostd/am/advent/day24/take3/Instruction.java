package oostd.am.advent.day24.take3;

public class Instruction {
    Type type;
    int variable; //0,1,2,3 (w,x,y,z)
    int value; //a value or variable.
    boolean valueIsVar; // true if value represents a variable.

    public Instruction(Type type, int variable, int value, boolean valueIsVar) {
        this.type = type;
        this.variable = variable;
        this.value = value;
        this.valueIsVar = valueIsVar;
    }

    // shorthand for creating inp instruction.
    // for type inp, the value contains the index of the argument list.
    public Instruction(int variable, int argIndex) {
        this.type = Type.inp;
        this.variable = variable;
        this.value = argIndex;
        this.valueIsVar = false;
    }
}
