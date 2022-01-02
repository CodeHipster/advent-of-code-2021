package oostd.am.advent.day24.take2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import oostd.am.advent.day24.take2.instructions.Instruction;
import oostd.am.advent.tools.FileReader;

/**
 * Try to see if compiling instructions to functions is faster then using a switch and an instructions object.
 * Which is not the case. Switch processes +-11 million per second
 * While functional interfaces processes +- 6 million per second.
 */
public class Day24 {

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day24-test-input");

        Alu alu = new Alu();

        Compiler compiler = new Compiler();
        List<Instruction> program = compiler.compile(input);

        int[] monad = new int[]{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9};
        int index = monad.length - 1;

        long start = System.currentTimeMillis();
        long end = start + 1_000;
        long processed = 0;
        do {
            for (int i = 9; i > 0; i--) {
                monad[index] = i;
                long[] result = alu.run(program, monad);
                processed++;
                if (result[3] == 0) {
                    int win = 0;
                    System.out.println(Arrays.stream(monad).mapToObj(nr -> "" + nr).collect(Collectors.joining()));
                }
            }
        }
        while (lower(monad) && System.currentTimeMillis() < end);

        System.out.println(processed);
        int debug = 1;

    }

    static boolean lower(int[] monad) {
        int index = monad.length - 1;
        while (true) {
            if (monad[index] == 1) {
                monad[index] = 9;
                index--;
                if (index < 0) return false;
            } else {
                monad[index]--;
                return true;
            }
        }
    }

}
