package oostd.am.advent.day23.take1;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class Pod {
    public int energy;
    int x, y;
    Tile.Type destination;

    Pod(int x, int y, Tile.Type destination, int energy) {
        this.x = x;
        this.y = y;
        this.destination = destination;
        this.energy = energy;
    }

    public Pod clone() {
        return new Pod(x, y, destination, energy);
    }

    List<Move> destinations(Burrow burrow, List<Pod> pods) {
        List<Coordinate> blocked = new ArrayList<>();
        blocked.addAll(pods.stream().map(a -> new Coordinate(a.x, a.y)).collect(Collectors.toList()));
        Deque<Move> moves = new ArrayDeque<>();
        List<Move> initial = moves(burrow.tiles[y][x], blocked, burrow, 0);
        moves.addAll(initial);

        List<Tile.Type> allowed = allowed(burrow, pods);

        List<Move> destinations = new ArrayList<>();
        while (!moves.isEmpty()) {
            Move move = moves.pop();
            blocked.add(new Coordinate(move.tile.x, move.tile.y));
            if (allowed.contains(move.tile.type)) {
                if (!move.tile.isExit()) {
                    destinations.add(move);
                }
            }
            //add neighbours as moves.
            List<Move> nextMoves = moves(move.tile, blocked, burrow, move.steps);
            moves.addAll(nextMoves);
        }
        return destinations;

    }

    // Determine which tileTypes are allowed as destination from current tile.
    private List<Tile.Type> allowed(Burrow burrow, List<Pod> pods) {
        Tile current = burrow.tiles[this.y][this.x];
        boolean occupied = burrow.occupied(destination, pods);
        if (current.type == Tile.Type.HALL) {
            if (!occupied) return Arrays.asList(this.destination);
            else return new ArrayList<>();
        } else if (current.type == destination) {
            // We can move out if it is occupied
            if (!occupied) return Arrays.asList(destination);
            else return Arrays.asList(Tile.Type.HALL, destination); //We dont want to move out, else we will loop forever.
        } else {
            // else current is any of the other rooms.
            // can't move into own burrow if occupied by another.
            if (!occupied) return Arrays.asList(Tile.Type.HALL, destination);
            else return Arrays.asList(Tile.Type.HALL); // can't go anywhere.
        }
    }

    private List<Move> moves(Tile tile, List<Coordinate> blocked, Burrow burrow, final int steps) {
        return tile.neighbours.stream()
                .map(n -> new Coordinate(n.x, n.y))
                .filter(c -> !blocked.contains(c))
                .map(c -> new Move(burrow.tiles[c.y][c.x], steps + 1))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pod pod = (Pod) o;
        return energy == pod.energy && x == pod.x && y == pod.y && destination == pod.destination;
    }

    @Override
    public int hashCode() {
        return Objects.hash(energy, x, y, destination);
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "," + destination + "]";
    }
}