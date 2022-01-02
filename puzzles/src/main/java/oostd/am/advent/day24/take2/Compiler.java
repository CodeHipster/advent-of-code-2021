package oostd.am.advent.day24.take2;

import java.util.ArrayList;
import java.util.List;
import oostd.am.advent.day24.take2.instructions.Instruction;

public class Compiler {
    List<Instruction> compile(List<String> program) {
        List<Instruction> compiled = new ArrayList<>();

        int argPos = 0;
        for (String s : program) {
            String[] inst = s.split(" ");
            String type = inst[0];
            final int index = parse(inst[1]);

            if(inst.length == 2) {
                final int arg = argPos++;
                compiled.add((long[] vars, int[] args) -> vars[index] = args[arg]);
                continue;
            }
            final int value = parse(inst[2]);
            switch (type) {
                case "inp":
                    // already processed.
                    break;
                case "add":
                    if(isVariable(inst[2])){
                        compiled.add((long[] vars, int[] args) -> vars[index] += vars[value]);
                    }else{
                        compiled.add((long[] vars, int[] args) -> vars[index] += value);
                    }
                    break;
                case "mul":
                    if(isVariable(inst[2])){
                        compiled.add((long[] vars, int[] args) -> vars[index] *= vars[value]);
                    }else{
                        compiled.add((long[] vars, int[] args) -> vars[index] *= value);
                    }
                    break;
                case "div":
                    if(isVariable(inst[2])){
                        compiled.add((long[] vars, int[] args) -> vars[index] /= vars[value]);
                    }else{
                        compiled.add((long[] vars, int[] args) -> vars[index] /= value);
                    }
                    break;
                case "mod":
                    if(isVariable(inst[2])){
                        compiled.add((long[] vars, int[] args) -> vars[index] %= vars[value]);
                    }else{
                        compiled.add((long[] vars, int[] args) -> vars[index] %= value);
                    }
                    break;
                case "eql":
                    if(isVariable(inst[2])){
                        compiled.add((long[] vars, int[] args) -> vars[index] = (vars[index] == vars[value]) ? 1 : 0);
                    }else{
                        compiled.add((long[] vars, int[] args) -> vars[index] = (vars[index] == value) ? 1 : 0);
                    }
                    break;
                default:
                    throw new RuntimeException();
            }
        }
        return compiled;
    }

    int parse(String variable) {
        switch (variable) {
            case "w":
                return 0;
            case "x":
                return 1;
            case "y":
                return 2;
            case "z":
                return 3;
            default:
                return Integer.parseInt(variable);
        }
    }

    boolean isVariable(String input) {
        return (input.equals("w") || input.equals("x") || input.equals("y") || input.equals("z"));
    }
}
