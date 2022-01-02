package oostd.am.advent.day24.take2.instructions;

@FunctionalInterface
public interface Instruction {

    // manipulates vars.
    void execute(long[] vars, int[] args); // where value could be an index
}
