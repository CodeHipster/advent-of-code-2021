package oostd.am.advent.day24.take1;

import java.util.List;

public class Alu {

    long[] vars = new long[4];

    void process(Instruction instruction, int[] arguments) {
        int variable = instruction.variable;
        int value = instruction.value;
        boolean valueIsVar = instruction.valueIsVar;
        switch (instruction.type) {
            case inp:
                vars[variable] = arguments[value];
                break;
            case add:
                if (valueIsVar) vars[variable] += vars[value];
                else vars[variable] += value;
                break;
            case div:
                if (valueIsVar) vars[variable] /= vars[value];
                else vars[variable] /= value;
                break;
            case mul:
                if (valueIsVar) vars[variable] *= vars[value];
                else vars[variable] *= value;
                break;
            case mod:
                if (valueIsVar) vars[variable] %= vars[value];
                else vars[variable] %= value;
                break;
            case eql:
                if (valueIsVar) vars[variable] = (vars[variable] == vars[value]) ? 1 : 0;
                else vars[variable] = (vars[variable] == value) ? 1 : 0;
                break;
            default:
                throw new RuntimeException();
        }
    }


    long[] run(List<Instruction> program, int[] arguments) {
        reset();

        for (Instruction i : program) {
            process(i, arguments);
        }
//        System.out.println(Arrays.stream(vars).mapToObj(l -> ""+ l).collect(Collectors.joining(",")));
        return vars;
    }

    private void reset() {
        vars = new long[4];
    }
}
