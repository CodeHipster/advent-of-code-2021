package oostd.am.advent.day23.take1;

import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day23 {

    static boolean endState(Burrow burrow, State state) {
        long inPosition = state.amphis.stream().filter(a -> burrow.tiles[a.y][a.x].type == a.destination).count();
        return inPosition == state.amphis.size();
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day23-input");

        Burrow burrow = new Burrow(input);
        // Set room id's
        burrow.addRoom(3, 2, Tile.Type.A);
        burrow.addRoom(3, 3, Tile.Type.A);
        burrow.addRoom(5, 2, Tile.Type.B);
        burrow.addRoom(5, 3, Tile.Type.B);
        burrow.addRoom(7, 2, Tile.Type.C);
        burrow.addRoom(7, 3, Tile.Type.C);
        burrow.addRoom(9, 2, Tile.Type.D);
        burrow.addRoom(9, 3, Tile.Type.D);

        burrow.initializeNeighbours();

        State initial = new State(burrow.amphis.stream().map(Pod::clone).collect(Collectors.toList()), 0);

        Queue<State> states = new UniqueQueue<>();
        states.add(initial);

        long energySpend = Long.MAX_VALUE;
        while (!states.isEmpty()) {
            State state = states.remove();
            if (endState(burrow, state)) { // state is desired endState
                if (state.energyUsed < energySpend) energySpend = state.energyUsed;
                System.out.println(energySpend);
                continue;
            }
            List<State> next = state.next(burrow);
            states.addAll(next);
        }

        System.out.println(energySpend);
        int debug = 0;
    }
}
