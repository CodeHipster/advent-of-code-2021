package oostd.am.advent.day23.take1;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    int x, y;
    Type type;
    List<Tile> neighbours = new ArrayList<>();

    public Tile(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    boolean isExit() {
        return this.type == Type.HALL && neighbours.stream().anyMatch(n -> n.type != Type.HALL);
    }

    public enum Type {
        WALL,
        HALL,
        A,
        B,
        C,
        D
    }
}