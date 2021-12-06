package oostd.am.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day6Plus {

    static class Fish {

        Consumer<Fish> spawnHandler;
        int replicationTimer;

        Fish(Consumer<Fish> spawnHandler) {
            this(6, spawnHandler);
        }

        Fish(int daysLeft, Consumer<Fish> spawnHandler) {
            this.replicationTimer = daysLeft;
            this.spawnHandler = spawnHandler;
        }

        void age() {
            if (replicationTimer > 0) {
                replicationTimer--;
            } else {
                replicationTimer = 6;
                this.spawnHandler.accept(new Fish(8, spawnHandler));
            }
        }
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day6-input");

        // days and count of fish

        Map<Integer, Long> initial = Arrays.stream(input.get(0).split(","))
                .collect(Collectors.toMap(Integer::parseInt, i -> 1L, Long::sum));

        HashMap<Integer, Long> ponds = new HashMap<>(initial);

        for (int d = 0; d < 256; d++) {
            HashMap<Integer, Long> swapPond = new HashMap<>();

            //move fish to a lower pond
            ponds.forEach((days, count) -> {
                if (days != 0) {
                    swapPond.merge(days -1, count, Long::sum);
                }else{
                    // cloning day
                    swapPond.merge(8,count, Long::sum);
                    swapPond.merge(6,count, Long::sum);
                }
            });

            ponds = swapPond;

        }

        long answer = ponds.values().stream().mapToLong(Long::longValue).sum();

        int debug = 0;
    }

}
