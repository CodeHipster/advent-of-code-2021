package oostd.am.advent.day23.take2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import oostd.am.advent.tools.Coordinate;

class Pod {
    Coordinate pos;
    int type;

    Pod(Coordinate pos, int type) {
        this.pos = pos;
        this.type = type;
    }

    public Pod clone() {
        return new Pod(pos.clone(), type);
    }

    List<Move> destinations(Burrow burrow, Pod[] pods) {
        if (!allowedToMove(burrow, pods)) return new ArrayList<>();
        List<Move> traverse = this.traverse(burrow, pods);
        Optional<Move> success = traverse.stream().filter(m -> inPosition(m.tile, pods)).findFirst();
        return success.map(Arrays::asList).orElse(traverse);
    }

    //TODO: optimize if required.
    private List<Move> traverse(Burrow burrow, Pod[] pods) {
        Coordinate[] blocked = new Coordinate[8];
        for (int i = 0; i < 8; i++) {
            blocked[i] = pods[i].pos;
        }
        Tile current = burrow.tiles[pos.y][pos.x];
        return traverse(current, null, blocked, 0, burrow, pods, current.type);
    }

    // recursive method.
    private List<Move> traverse(Tile current, Tile previous, Coordinate[] blocked, int steps, Burrow burrow, Pod[] pods, int startType) {
        List<Move> moves = new ArrayList<>();

        if (previous != null) {
            // can't move into other rooms, unless you are already in it.
            if (current.type != 0 // this move is in a room
                    && current.type != this.type // and it is not the destination room
                    && current.type != previous.type) // and did not move inside the room.
                return moves;

            // can't move through other amphis.
            for (Coordinate b : blocked) {
                if (b.equals(current.pos)) return moves;
            }
            // can't move into occupied room.
            if (current.type == this.type && !destinationAvailable(burrow, pods))
                return moves;

            if (!current.isExit() // can't stop on an exit.
                    && !(current.type == 0 && startType == 0) // can't stop in hallway if we started there.
                    && !(current.type != 0 && current.type != this.type) // cant stop in another room.
                    && !blockingRoom(current, pods))
                moves.add(new Move(current, steps));
        }

        for (Tile n : current.neighbours) {
            if (!n.equals(previous)) {
                moves.addAll(traverse(n, current, blocked, steps + 1, burrow, pods, startType));
            }
        }
        return moves;
    }

    private boolean blockingRoom(Tile current, Pod[] pods) {
        if(current.type == this.type && current.neighbours.length == 2){
            for (Tile n : current.neighbours) {
                if (n.type == current.type) { //neighbour is same room
                    for (Pod p : pods) {
                        if (p.type == this.type && p.pos.equals(n.pos))
                            return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean allowedToMove(Burrow burrow, Pod[] pods) {
        Tile current = burrow.tiles[pos.y][pos.x];
        if (current.type == 0 && destinationAvailable(burrow, pods))
            // we are in the hallway and the destination is available.
            return true;
        return !inPosition(current, pods);
    }

    private boolean destinationAvailable(Burrow burrow, Pod[] pods) {
        for (Tile t : burrow.rooms.get(this.type)) {
            for (Pod p : pods) {
                if (p.pos.equals(t.pos) && p.type != this.type) return false;
            }
        }
        return true;
    }

    private boolean inPosition(Tile current, Pod[] pods) {
        // if inside incorrect room
        if (current.type != this.type) return false;

        if (current.neighbours.length == 1) return true;
        else { // we might be in position if the other spot is taken by a correct pod.
            for (Tile n : current.neighbours) {
                if (n.type == current.type) { //neighbour is same room
                    for (Pod p : pods) {
                        if (p.type == this.type && p.pos.equals(n.pos))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pod pod = (Pod) o;
        return type == pod.type && pos.equals(pod.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, type);
    }

    @Override
    public String toString() {
        return pos.toString() + ", " + type;
    }

}