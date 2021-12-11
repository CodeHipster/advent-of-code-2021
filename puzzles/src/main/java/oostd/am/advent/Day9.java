package oostd.am.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day9 {

    static class Neighbour {
        enum Location {
            TOP(0, -1), LEFT(-1, 0), RIGHT(1, 0), BOTTOM(0, 1);
            int x;
            int y;

            Location(int x, int y) {
                this.x = x;
                this.y = y;
            }
        }

        Location location;
        int height;

        Neighbour(Location location, int height) {
            this.location = location;
            this.height = height;
        }
    }

    static class HeightMap {
        // array or rows [y][x]
        int[][] map;

        HeightMap(List<String> input) {
            map = input.stream()
                    .map(row -> Arrays.stream(row.trim().split("")).mapToInt(Integer::parseInt).toArray())
                    .toArray(int[][]::new);
        }

        List<Neighbour> neighbours(int x, int y) {
            List<Neighbour.Location> toCheck = new ArrayList<>(List.of(Neighbour.Location.values()));
            if (x == 0) toCheck.remove(Neighbour.Location.LEFT);
            if (x == map[0].length - 1) toCheck.remove(Neighbour.Location.RIGHT);
            if (y == 0) toCheck.remove(Neighbour.Location.TOP);
            if (y == map.length - 1) toCheck.remove(Neighbour.Location.BOTTOM);

            return toCheck.stream()
                    .map(l -> new Neighbour(l, map[y + l.y][x + l.x]))
                    .collect(Collectors.toList());
        }

    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day9-input");

        HeightMap heightMap = new HeightMap(input);

        List<Integer> riskList = new ArrayList<>();
        for(int y = 0; y < heightMap.map.length; y++){
            for(int x = 0; x < heightMap.map[y].length; x++){
                int height = heightMap.map[y][x];
                List<Neighbour> neighbours = heightMap.neighbours(x, y);
                Optional<Integer> lower = neighbours.stream().map(n -> n.height).filter(n -> n <= height).findAny();
                if(lower.isEmpty()){
                    riskList.add(1 + height);
                }
            }
        }

        int answer = riskList.stream().mapToInt(i -> i).sum();
        int debug = 0;
    }

}
