package oostd.am.advent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day5 {

    static class Line {
        enum Direction {
            HORIZONTAL,
            VERTICAL
        }

        Point start;
        Point end;
        Direction direction;

        Line(Point start, Point end) {
            this.start = start;
            this.end = end;
            this.abs();
            this.direction = this.determineDirection();
        }

        static Line of(String input) {
            String[] sCoords = input.split(" -> ");
            return new Line(Point.of(sCoords[0]), Point.of(sCoords[1]));
        }

        boolean horizontal() {
            return start.y == end.y;
        }

        boolean vertical() {
            return start.x == end.x;
        }

        List<Point> coveredPoints() {
            List<Point> coveredPoints = new ArrayList<>();

            if (direction == Direction.HORIZONTAL) {
                // step over x, assuming line is perfect horizontal
                int y = start.y;
                for (int x = start.x; x <= end.x; x++) {
                    coveredPoints.add(new Point(x, y));
                }
            }else {
                // step over y, assuming line is perfect vertical
                int x = start.x;
                for (int y = start.y; y <= end.y; y++) {
                    coveredPoints.add(new Point(x, y));
                }
            }
            return coveredPoints;
        }

        //Make lines go in positive direction, for the coordinate(x or y) with the most distance.
        private void abs() {
            int xDif = start.x - end.x;
            int yDif = start.y - end.y;

            if (Math.abs(xDif) >= Math.abs(yDif)) {
                //make x go in positive direction.
                if (start.x > end.x) {
                    //swap
                    Point temp = start;
                    start = end;
                    end = temp;
                }
            } else {
                //make y go in positive direction.
                if (start.y > end.y) {
                    //swap
                    Point temp = start;
                    start = end;
                    end = temp;
                }
            }
        }

        private Direction determineDirection() {
            //assumes Line has been made absolute(abs)
            if (end.x - start.x >= end.y - start.y) {
                return Direction.HORIZONTAL;
            }else{
                return Direction.VERTICAL;
            }
        }

    }

    static class Point {
        static Point of(String input) {
            String[] points = input.split(",");
            return new Point(Integer.parseInt(points[0]), Integer.parseInt(points[1]));
        }

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int x, y;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day5-input");

        Map<Point, Integer> coveredPoints = input.stream().map(Line::of)
                .filter(l -> l.horizontal() || l.vertical())
                .flatMap(l -> l.coveredPoints().stream())
                .collect(Collectors.toMap(p -> p, p -> 1, (oldValue, value) -> oldValue+value));

        long answer = coveredPoints.values().stream().filter(i -> i > 1).count();
        int debug = 0;

    }

}
