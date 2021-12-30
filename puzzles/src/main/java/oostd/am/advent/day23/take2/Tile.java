package oostd.am.advent.day23.take2;

import java.util.Arrays;
import oostd.am.advent.tools.Coordinate;

public class Tile {
    Coordinate pos;
    int type;
    Tile[] neighbours;

    public Tile(Coordinate pos, int type) {
        this.pos = pos;
        this.type = type;
    }

    boolean isExit() {
        if (this.type != 0) return false;
        for (Tile n : neighbours) {
            if (n.type != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return pos.toString() + ", " + type;
    }
}