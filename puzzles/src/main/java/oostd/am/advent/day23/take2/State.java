package oostd.am.advent.day23.take2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import oostd.am.advent.tools.Coordinate;

class State {
    Pod[] pods;
    int steps;
    long energy;

    public State(Pod[] pods, int steps, long energy) {
        this.pods = clone(pods);
        this.steps = steps;
        this.energy = energy;
    }

    public List<State> next(Burrow burrow) {
        // figure out what next states are possible.
        List<State> next = new ArrayList<>();
        // for each pod check where they can move
        for (Pod a : pods) {
            List<Move> destinations = a.destinations(burrow, pods);
            //create a new state for each destination
            List<State> states = destinations.stream().map(m -> {
                Coordinate store = a.pos.clone();
                a.pos = m.tile.pos;
                State state = new State(pods, this.steps + m.steps, this.energy + (long) m.steps * a.type);
                a.pos = store; //Do not modify original pod positions.
                return state;
            }).collect(Collectors.toList());
            next.addAll(states);
        }
        return next;
    }

    private static Pod[] clone(Pod[] pods) {
        Pod[] newArray = pods.clone();
        for (int i = 0; i < 8; i++) {
            newArray[i] = pods[i].clone();
        }
        return newArray;
    }

    public String toString(Burrow burrow) {
        StringBuilder sb = new StringBuilder();
        sb.append("Steps: " + this.steps);
        sb.append("\n");
        sb.append("Energy: " + this.energy);
        sb.append("\n");
        var tiles = burrow.tiles;
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Coordinate c = new Coordinate(x, y);
                if (tiles[y][x] == null) {
                    sb.append("#");
                    continue;
                }
                Optional<Character> pod = Arrays.stream(this.pods).filter(a -> a.pos.equals(c)).map(a -> {
                    switch (a.type) {
                        case 1:
                            return 'A';
                        case 10:
                            return 'B';
                        case 100:
                            return 'C';
                        case 1000:
                            return 'D';
                        default:
                            throw new RuntimeException();
                    }
                }).findFirst();
                if (pod.isPresent()) sb.append(pod.get());
                else {
                    sb.append(".");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}