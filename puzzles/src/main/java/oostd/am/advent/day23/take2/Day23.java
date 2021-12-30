package oostd.am.advent.day23.take2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import oostd.am.advent.tools.Coordinate;
import oostd.am.advent.tools.FileReader;

//try recursive without expensive lists and streams.
public class Day23 {

    static Burrow burrow;

    static boolean endState(Burrow burrow, Pod[] pods) {
        for (Pod p : pods) {
            if (burrow.tiles[p.pos.y][p.pos.x].type != p.type) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day23-input");

        Map<Integer, Tile[]> rooms = new HashMap<>();
        rooms.put(1, new Tile[]{new Tile(new Coordinate(3, 3), 1), new Tile(new Coordinate(3, 2), 1)});
        rooms.put(10, new Tile[]{new Tile(new Coordinate(5, 2), 10), new Tile(new Coordinate(5, 3), 10)});
        rooms.put(100, new Tile[]{new Tile(new Coordinate(7, 2), 100), new Tile(new Coordinate(7, 3), 100)});
        rooms.put(1000, new Tile[]{new Tile(new Coordinate(9, 2), 1000), new Tile(new Coordinate(9, 3), 1000)});

        burrow = new Burrow(input, rooms);

        // TO TRY: change type into an integer 1, 10 , 100, 1000 (double use of single value)
        // TO TRY: create a hashmap of paths from each tile to each other tile?

        Pod[] pods = burrow.startPositions.toArray(Pod[]::new);

        long cost = Long.MAX_VALUE;
        Deque<State> states = new ArrayDeque<>();
        states.push(new State(pods, 0, 0));

        int statesProcessed = 0;
        while (!states.isEmpty()) {
//            states.forEach(state -> System.out.println(state.toString(burrow)));
//            System.out.println("----------------");
            State state = states.pop();
            statesProcessed++;
            if (endState(burrow, state.pods)) {
                System.out.println(state.energy);
                if (state.energy < cost) cost = state.energy;
                continue;
            }
            states.addAll(state.next(burrow));
        }

        System.out.println(cost);

        int debug = 0;
    }


}
