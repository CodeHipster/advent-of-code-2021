package oostd.am.advent;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day9Plus {

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
        int basin;

        Neighbour(Location location, int height, int basin) {
            this.location = location;
            this.height = height;
            this.basin = basin;
        }
    }

    static class MapData {
        // array or rows [y][x]
        int[][] heightMap;
        int[][] basinMap;
        int basinId = 1;

        MapData(List<String> input) {
            heightMap = input.stream()
                    .map(row -> Arrays.stream(row.trim().split("")).mapToInt(Integer::parseInt).toArray())
                    .toArray(int[][]::new);
            basinMap = new int[heightMap.length][heightMap[0].length];
            fillBasinMap();
        }

        List<Neighbour> neighbours(int x, int y) {
            List<Neighbour.Location> toCheck = new ArrayList<>(List.of(Neighbour.Location.values()));
            if (x == 0) toCheck.remove(Neighbour.Location.LEFT);
            if (x == heightMap[0].length - 1) toCheck.remove(Neighbour.Location.RIGHT);
            if (y == 0) toCheck.remove(Neighbour.Location.TOP);
            if (y == heightMap.length - 1) toCheck.remove(Neighbour.Location.BOTTOM);

            return toCheck.stream()
                    .map(l -> new Neighbour(l, heightMap[y + l.y][x + l.x], basinMap[y + l.y][x + l.x]))
                    .collect(Collectors.toList());
        }

        int merge(int basin1, int basin2) {
            for (int y = 0; y < basinMap.length; y++) {
                for (int x = 0; x < basinMap[y].length; x++) {
                    if(basinMap[y][x] == basin2){
                        basinMap[y][x] = basin1;
                    }
                }
            }

            return basin1;
        }

        void fillBasinMap() {
            for (int y = 0; y < heightMap.length; y++) {
                for (int x = 0; x < heightMap[y].length; x++) {
                    if (heightMap[y][x] == 9) {
                        continue; //not setting basin value.
                    }
                    //get neighbours. get basins.
                    List<Neighbour> neighbours = neighbours(x, y);
                    Set<Integer> unique = neighbours.stream().map(n -> n.basin).filter(i-> i > 0).collect(Collectors.toSet());
                    switch (unique.size()) {
                        case 0:
                            basinMap[y][x] = basinId++;
                            continue;
                        case 1:
                            basinMap[y][x] = unique.iterator().next();
                            continue;
                        case 2:
                            Iterator<Integer> iterator = unique.iterator();
                            int b1 = iterator.next();
                            var b2 = iterator.next();
                            basinMap[y][x] = merge(b1,b2);
                            continue;
                        default:
                            throw new RuntimeException("unexpected nr of basin neighbours");
                    }
                }
            }
        }
    }

    static class BasinFactory {
        int id = 0;

        Basin create() {
            return new Basin(id++); //add and then increment.
        }
    }

    static class Basin {
        public Basin(int id) {
            this.count = 0;
            this.id = id;
        }

        int count;
        int id;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Basin basin = (Basin) o;
            return count == basin.count && id == basin.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(count, id);
        }
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day9-input");

        MapData mapData = new MapData(input);

        Map<Integer, Long> collect = Arrays.stream(mapData.basinMap)
                .flatMapToInt(r -> Arrays.stream(r)).mapToObj(i -> i)
                .filter(i -> i>0)
                .collect(Collectors.toMap(i -> i, i -> 1L, Long::sum));

        List<Long> list = collect.values().stream().sorted((f1, f2) -> Long.compare(f2, f1)).collect(Collectors.toList());
        long answer = list.get(0) * list.get(1) * list.get(2);
        int debug = 0;
        //something fails in merging... merged basins are used?

    }

}
