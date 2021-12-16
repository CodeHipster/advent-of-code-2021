package oostd.am.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day15Plus {
    enum Location {
        TOP(0, -1), LEFT(-1, 0), RIGHT(1, 0), BOTTOM(0, 1);
        int x;
        int y;

        Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    static class RouteOption {
        Position position;
        long risk;

        public RouteOption(Position position, long risk) {
            this.position = position;
            this.risk = risk;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RouteOption that = (RouteOption) o;
            return risk == that.risk && position.equals(that.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, risk);
        }
    }

    static class CaveSystem {
        int[][] riskMap;

        CaveSystem(List<String> input) {
            riskMap = input.stream()
                    .map(s -> Arrays.stream(s.split("")).mapToInt(Integer::parseInt).toArray())
                    .toArray(int[][]::new);

            int[][] largeMap = new int[riskMap.length * 5][riskMap[0].length * 5];
            for (int y = 0; y < largeMap.length; y++) {
                int yAdd = y / riskMap.length;
                for (int x = 0; x < largeMap[y].length; x++) {
                    int xAdd = x / riskMap[0].length;
                    int sx = x % riskMap[0].length;
                    int sy = y % riskMap.length;
                    int risk = (((riskMap[sy][sx]-1)+yAdd + xAdd)%9)+1;
                    largeMap[y][x] = risk;
                }
            }
            riskMap = largeMap;
        }

        private List<RouteOption> getOptions(RouteOption option, long[][] riskRouteMap) {
            List<Location> toCheck = new ArrayList<>(List.of(Location.values()));
            int x = option.position.x;
            int y = option.position.y;
            if (x == 0) toCheck.remove(Location.LEFT);
            if (x == riskRouteMap[0].length - 1) toCheck.remove(Location.RIGHT);
            if (y == 0) toCheck.remove(Location.TOP);
            if (y == riskRouteMap.length - 1) toCheck.remove(Location.BOTTOM);

            return toCheck.stream()
                    .map(l -> new RouteOption(new Position(x + l.x, y + l.y), riskMap[y + l.y][x + l.x] + option.risk))
                    .collect(Collectors.toList());
        }

        long[][] calculateRiskRoute(Position start) {
            long[][] riskRouteMap = new long[riskMap.length][riskMap[0].length];
            for (long[] row : riskRouteMap) Arrays.fill(row, -1);
            List<RouteOption> options = new ArrayList<>();
            options.add(new RouteOption(start, 0));

            while (!options.isEmpty()) {
                RouteOption routeOption = options.remove(0);
                riskRouteMap[routeOption.position.y][routeOption.position.x] = routeOption.risk;
                List<RouteOption> neighbours = getOptions(routeOption, riskRouteMap);
                options.addAll(neighbours);
                options = options.stream()
                        .filter(o -> riskRouteMap[o.position.y][o.position.x] == -1) //remove resolved choices
                        .sorted(Comparator.comparingLong(o -> o.risk))  // sort lowest risk first
                        .collect(Collectors.toList());
            }
            return riskRouteMap;
        }

    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day15-input");

        CaveSystem caveSystem = new CaveSystem(input);
        long[][] longs = caveSystem.calculateRiskRoute(new Position(0, 0));

        int debug = 0;
    }


}
