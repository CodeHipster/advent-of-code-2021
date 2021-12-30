package oostd.am.advent.day23.take1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

class State {
    List<Pod> amphis;
    long energyUsed;

    public State(List<Pod> amphis, long energyUsed) {
        this.amphis = amphis;
        this.energyUsed = energyUsed;
    }

    public List<State> next(Burrow burrow) {
        // figure out what next states are possible.
        List<State> next = new ArrayList<>();
        // for each amphi check where they can move
        for (Pod a : amphis) {
            List<Move> destinations = a.destinations(burrow, amphis);
            //create a new state for each destination
            List<State> states = destinations.stream().map(m -> {
                List<Pod> copies = amphis.stream().map(Pod::clone).collect(Collectors.toList());
                Pod copy = copies.stream().filter(amp -> amp.equals(a)).findFirst().get();
                copy.x = m.tile.x;
                copy.y = m.tile.y;
                return new State(copies, this.energyUsed + (long) m.steps * a.energy);
            }).collect(Collectors.toList());
            next.addAll(states);
        }
        return next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return energyUsed == state.energyUsed && amphis.equals(state.amphis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amphis, energyUsed);
    }

    public String toString(Burrow burrow) {
        StringBuilder sb = new StringBuilder();
        var tiles = burrow.tiles;
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                int fx = x, fy = y;
                if (tiles[y][x] == null) {
                    sb.append(" ");
                    continue;
                }
                Optional<Pod> pod = this.amphis.stream().filter(a -> a.x == fx && a.y == fy).findFirst();
                if (pod.isPresent()) sb.append(pod.get().destination);
                else {
                    if (tiles[y][x].type == Tile.Type.WALL) {
                        sb.append("#");
                    } else {
                        sb.append(".");
                    }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}