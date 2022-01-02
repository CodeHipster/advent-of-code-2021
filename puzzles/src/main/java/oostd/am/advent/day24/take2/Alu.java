package oostd.am.advent.day24.take2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import oostd.am.advent.day24.take2.instructions.Instruction;

public class Alu {

    long[] vars = new long[4];

    long[] run(List<Instruction> program, int[] arguments) {
        reset();

        for (Instruction i : program) {
            i.execute(vars, arguments);
            int debug = 0;
        }
//        System.out.println(Arrays.stream(vars).mapToObj(l -> ""+ l).collect(Collectors.joining(",")));
        return vars;
    }

    private void reset() {
        vars = new long[4];
    }
}
