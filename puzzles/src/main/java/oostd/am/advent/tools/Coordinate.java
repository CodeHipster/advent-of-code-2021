package oostd.am.advent.tools;

import java.util.Objects;

public class Coordinate {
    public int x, y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate add(Coordinate other) {
        return new Coordinate(x + other.x, y + other.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

    public Coordinate clone() {
        return new Coordinate(x, y);
    }

    // Wrap the coordinates around a map.
    public Coordinate wrap(int width, int height) {
        return new Coordinate(x % width, y % height);
    }
}