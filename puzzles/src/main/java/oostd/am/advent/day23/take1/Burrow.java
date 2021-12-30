package oostd.am.advent.day23.take1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class Burrow {
        Tile[][] tiles;
        List<Pod> amphis = new ArrayList();
        HashMap<Tile.Type,List<Tile>> rooms = new HashMap<>();

        Burrow(List<String> input) {
            // setup rooms
            rooms.put(Tile.Type.A, new ArrayList<>());
            rooms.put(Tile.Type.B, new ArrayList<>());
            rooms.put(Tile.Type.C, new ArrayList<>());
            rooms.put(Tile.Type.D, new ArrayList<>());

            Integer maxX = input.stream().map(String::length).max(Integer::compare).get();
            tiles = new Tile[input.size()][maxX];

            for (int y = 0; y < input.size(); y++) {
                String row = input.get(y);
                for (int x = 0; x < row.length(); x++) {
                    switch (row.charAt(x)) {
                        case '#':
                            tiles[y][x] = new Tile(x, y, Tile.Type.WALL);
                            break;
                        case '.':
                            tiles[y][x] = new Tile(x, y, Tile.Type.HALL);
                            break;
                        case 'A':
                            tiles[y][x] = new Tile(x, y, null);
                            amphis.add(new Pod(x, y, Tile.Type.A, 1));
                            break;
                        case 'B':
                            tiles[y][x] = new Tile(x, y, null);
                            amphis.add(new Pod(x, y, Tile.Type.B, 10));
                            break;
                        case 'C':
                            tiles[y][x] = new Tile(x, y, null);
                            amphis.add(new Pod(x, y, Tile.Type.C, 100));
                            break;
                        case 'D':
                            tiles[y][x] = new Tile(x, y, null);
                            amphis.add(new Pod(x, y, Tile.Type.D, 1000));
                            break;
                        default:
                    }
                }
            }
        }

        // Add a room tile;
        void addRoom(int x, int y, Tile.Type type) {
            List<Tile> room = rooms.get(type);
            Tile tile = tiles[y][x];
            tile.type = type;
            room.add(tile);
        }

        boolean occupied(Tile.Type room, List<Pod> pods){
            return this.rooms.get(room).stream()
                    .anyMatch(r -> pods.stream().anyMatch(p -> p.x == r.x && p.y == r.y && p.destination != r.type));
        }

        void initializeNeighbours() {
            int h = tiles.length;
            int w = tiles[0].length;
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    Tile tile = tiles[y][x];
                    if (tile != null && tile.type != Tile.Type.WALL) {
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
                                .filter(t -> t != null && t.type != Tile.Type.WALL).collect(Collectors.toList());
                        tiles[y][x].neighbours = neighbours;
                    }
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int y = 0; y < tiles.length; y++) {
                for (int x = 0; x < tiles[y].length; x++) {
                    if (tiles[y][x] == null) {
                        sb.append(" ");
                        continue;
                    }
                    switch (tiles[y][x].type) {
                        case WALL:
                            sb.append("#");
                            break;
                        case HALL:
                            sb.append(".");
                            break;
                        case A:
                            sb.append("A");
                            break;
                        case B:
                            sb.append("B");
                            break;
                        case C:
                            sb.append("C");
                            break;
                        case D:
                            sb.append("D");
                            break;
                        default:
                    }
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }