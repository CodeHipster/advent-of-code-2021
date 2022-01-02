package oostd.am.advent.day24.take3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

//split the program in steps and find acceptable values for each step.
public class Day24 {

    public static void main(String[] args) {
        Compiler compiler = new Compiler();
        Alu alu = new Alu();

        //<step, <output-z, input-args>>
        Map<Integer, Map<Long, Integer[]>> outputOptions = new HashMap<>();

        HashMap<Long, Integer[]> initial = new HashMap<>();
        initial.put(0L, new Integer[14]);
        outputOptions.put(0, initial);
        for (int i = 1; i <= 14; i++) {
            long start = System.currentTimeMillis();
            int stepIndex = i - 1;
            System.out.println("input: " + i);
            // <output-z, previous-input-args>
            HashMap<Long, Integer[]> outputs = new HashMap<>();
            outputOptions.put(i, outputs);
            List<String> input = new FileReader().readInput("day24/step" + i + ".txt");
            List<Instruction> program = compiler.compile(input);
            Map<Long, Integer[]> inputOptions = outputOptions.get(i - 1);
            inputOptions.forEach((in, map) -> {
                for (int j = 1; j <= 9; j++) {
                    alu.reset(); // not really required.
                    alu.vars[3] = in;
                    long[] vars = alu.run(program, new int[]{j});
                    Integer[] map2 = Arrays.copyOf(map, 14);
                    map2[stepIndex] = j;
                    outputs.merge(vars[3], map2, (old, newVal) -> {
                        for (int k = 0; k <= stepIndex; k++) {
                            if (old[k] < newVal[k]) return old;
                            else if (old[k] > newVal[k]) return newVal;
                        }
                        throw new RuntimeException("the latest input is always greater if all previous values are equal.");
                    }); //potentially overriding a lower input value.
                }
            });
            System.out.println("input: " + i + " outputs: " + outputs.size() + " duration: " + (System.currentTimeMillis() - start));
        }


        //digit 1(line:199), input-arg:1-9 input-z:0 , output-z:3-11


        //digit 12(line:199), input-arg:1-9 input-z:0-666 , output-z:0-25
        //digit 13(line:217), input-arg:6-9 input-z: 0-25, output-z: 8-11
        //digit 14(line:235),input-arg:1-9 input-z: 8-16, output-z: 0

        System.out.println(Arrays.stream(outputOptions.get(14).get(0L)).map(l -> ""+ l).collect(Collectors.joining("")));
        int debug = 1;

    }

}
