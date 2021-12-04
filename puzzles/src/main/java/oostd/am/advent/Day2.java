package oostd.am.advent;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import oostd.am.advent.tools.FileReader;

public class Day2 {

    static enum Direction {
        UP,
        DOWN,
        FORWARD
    }

    static class Movement {
        public Movement(String[] input) {
            direction = Direction.valueOf(input[0].toUpperCase());
            distance = Integer.parseInt(input[1]);
        }

        public Direction direction;
        public int distance;
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day2-input");

        AtomicInteger depth = new AtomicInteger();
        AtomicInteger horizontal = new AtomicInteger();
        input.stream()
                .parallel()
                .map(in -> new Movement(in.split("\\s")))
                .forEach(m -> {switch (m.direction) {
                    case UP:
                        depth.addAndGet(-m.distance);
                        break;
                    case FORWARD:
                        horizontal.addAndGet(m.distance);
                        break;
                    case DOWN:
                        depth.addAndGet(m.distance);
                        break;
                    default:
                        throw new RuntimeException("unknown direction");
                }});

        int answer = depth.get() * horizontal.get();

        int debug = 1;

    }
}
