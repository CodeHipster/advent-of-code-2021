package oostd.am.advent.day23.take2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import oostd.am.advent.tools.Coordinate;

public class Burrow {

    Tile[][] tiles;
    List<Pod> startPositions = new ArrayList<>();
    Map<Integer, Tile[]> rooms;

    Burrow(List<String> input, Map<Integer, Tile[]> rooms){
        this.rooms = rooms;

        Integer maxX = input.stream().map(String::length).max(Integer::compare).get();
        tiles = new Tile[input.size()][maxX];

        for (int y = 0; y < input.size(); y++) {
            String row = input.get(y);
            for (int x = 0; x < row.length(); x++) {
                char c = row.charAt(x);
                switch (c) {
                    case '.':
                        tiles[y][x] = new Tile(new Coordinate(x, y), 0);
                        break;
                    case 'A':
                        startPositions.add(new Pod(new Coordinate(x, y), 1));
                        tiles[y][x] = new Tile(new Coordinate(x, y), 0);
                        break;
                    case 'B':
                        startPositions.add(new Pod(new Coordinate(x, y), 10));
                        tiles[y][x] = new Tile(new Coordinate(x, y), 0);
                        break;
                    case 'C':
                        startPositions.add(new Pod(new Coordinate(x, y), 100));
                        tiles[y][x] = new Tile(new Coordinate(x, y), 0);
                        break;
                    case 'D':
                        startPositions.add(new Pod(new Coordinate(x, y), 1000));
                        tiles[y][x] = new Tile(new Coordinate(x, y), 0);
                        break;
                    default:
                }
            }
        }
        rooms.values().stream().flatMap(Arrays::stream).forEach(t -> tiles[t.pos.y][t.pos.x] = t);
        this.initializeNeighbours();
    }

    private void initializeNeighbours() {
        int h = tiles.length;
        int w = tiles[0].length;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Tile tile = tiles[y][x];
                if (tile != null) {
                    int fx = x, fy = y;
                    List<Tile> neighbours = List.of(
                                    new Coordinate(-1, 0),
                                    new Coordinate(1, 0),
                                    new Coordinate(0, 1),
                                    new Coordinate(0, -1))
                            .stream().map(l -> new Coordinate(l.x + fx, l.y + fy))
                            .map(l -> (l.x >= 0 && l.x < w && l.y >= 0 && l.y < h) ? l : null)
                            .filter(Objects::nonNull)
                            .map(l -> tiles[l.y][l.x])
                            .filter(t -> t != null && t.type != '.').collect(Collectors.toList());
                    tile.neighbours = neighbours.toArray(Tile[]::new);
                }
            }
        }
    }
}
