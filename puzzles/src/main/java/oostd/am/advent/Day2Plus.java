package oostd.am.advent;

import java.util.List;
import oostd.am.advent.tools.FileReader;

public class Day2Plus {

    enum CommandType {
        UP,
        DOWN,
        FORWARD
    }

    static class Command {
        public Command(String[] input) {
            type = CommandType.valueOf(input[0].toUpperCase());
            value = Integer.parseInt(input[1]);
        }

        public CommandType type;
        public int value;
    }

    static class Submarine{
        int aim;
        int depth;
        int horizontal;

        public void up(int value) {
            aim -= value;
        }

        public void down(int value) {
            aim += value;
        }

        public void forward(int value) {
            horizontal += value;
            depth += value * aim;
        }
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day2-input");

        final Submarine submarine = new Submarine();
        input.stream()
                .sequential()
                .map(in -> new Command(in.split("\\s")))
                .forEach(m -> {switch (m.type) {
                    case UP:
                        submarine.up(m.value);
                        break;
                    case FORWARD:
                        submarine.forward(m.value);
                        break;
                    case DOWN:
                        submarine.down(m.value);
                        break;
                    default:
                        throw new RuntimeException("unknown direction");
                }});

        int answer = submarine.depth * submarine.horizontal;

        int debug = 1;

    }
}
