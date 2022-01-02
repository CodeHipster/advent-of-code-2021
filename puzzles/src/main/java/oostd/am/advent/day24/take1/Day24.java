package oostd.am.advent.day24.take1;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day24 {

    // TODO: check if we can use some sort of greater or lesser computation.
    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day24-input");

        Alu alu = new Alu();

        Compiler compiler = new Compiler();
        List<Instruction> program = compiler.compile(input);

        long[] run = alu.run(program, new int[]{1,3,5,7,9,2,4,6,8,9,9,9,9,9});
        System.out.println(Arrays.stream(run).mapToObj(l -> ""+ l).collect(Collectors.joining(",")));


//        for (int i = 1; i <= 9; i++) {
//            for (int z = 3; z <= 11; z++) {
//                alu.vars[3] = z;
//                long[] run = alu.run(program, new int[]{i});
////                if (run[3] >= 0 && run[3] <= 25) {
//                    System.out.println("input-arg: " + i + ", input-z: " + z + ", output-z: " + run[3]);
////                }
//            }
//        }
        //digit 1(line:199), input-arg:1-9 input-z:0 , output-z:3-11



        //digit 12(line:199), input-arg:1-9 input-z:0-666 , output-z:0-25
        //digit 13(line:217), input-arg:6-9 input-z: 0-25, output-z: 8-11
        //digit 14(line:235),input-arg:1-9 input-z: 8-16, output-z: 0


//

        long start = System.currentTimeMillis();
        long end = start + 1_000;
        long processed = 0;

        int[] monad = new int[]{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9};
        int index = monad.length - 1;
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
