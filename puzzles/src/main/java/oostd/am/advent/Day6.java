package oostd.am.advent;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day6 {

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

        List<Fish> swarm = new ArrayList<>();

        List<Fish> initial = Arrays.stream(input.get(0).split(","))
                .map(i -> new Fish(Integer.parseInt(i), swarm::add))
                .collect(Collectors.toList());

        swarm.addAll(initial);

        for(int d = 0;  d < 80; d++){
            // making a clone so we do not extend the list we are iterating through.
            List<Fish> clone = new ArrayList<>();
            clone.addAll(swarm);
            clone.forEach(fish -> fish.age());
        }

        int answer = swarm.size();

        int debug = 0;
    }

}
