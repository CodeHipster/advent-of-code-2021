package oostd.am.advent.day24.take1;

import java.util.ArrayList;
import java.util.List;

public class Compiler {
    List<Instruction> compile(List<String> program) {
        List<Instruction> compiled = new ArrayList<>();

        int argPos = 0;
        for (String s : program) {
            String[] inst = s.split(" ");
            String type = inst[0];
            switch (type) {
                case "inp":
                    compiled.add(new Instruction(parse(inst[1]), argPos++));
                    break;
                case "add":
                case "mul":
                case "div":
                case "mod":
                case "eql":
                    compiled.add(new Instruction(Type.valueOf(type), parse(inst[1]), parse(inst[2]), isVariable(inst[2])));
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
