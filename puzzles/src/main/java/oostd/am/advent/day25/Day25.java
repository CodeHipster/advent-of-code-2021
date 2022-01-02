package oostd.am.advent.day25;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import oostd.am.advent.tools.Coordinate;
import oostd.am.advent.tools.FileReader;

public class Day25 {


    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day25-test-input");

        List<Cucumber> east = new ArrayList<>();
        List<Cucumber> south = new ArrayList<>();

        int height = input.size();
        int[][] map = new int[height][];
        for (int y = 0; y < height; y++) {
            String[] split = input.get(y).split("");
            int width = split.length;
            map[y] = new int[width];
            for (int x = 0; x < width; x++) {
                switch (split[x]) {
                    case ".":
                        map[y][x] = 0;
                        break;
                    case ">":
                        map[y][x] = 1;
                        east.add(new Cucumber(new Coordinate(x, y), new Coordinate(1, 0)));
                        break;
                    case "v":
                        map[y][x] = 2;
                        south.add(new Cucumber(new Coordinate(x, y), new Coordinate(0, 1)));
                        break;
                    default:
                        throw new RuntimeException();
                }
            }
        }

        boolean moved = true;
        int steps = 0;
        while (moved) {
            List<Cucumber> eastMove = east.stream().filter(c -> c.check(map)).collect(Collectors.toList());
            eastMove.forEach(c -> c.move(map));
            List<Cucumber> southMove = south.stream().filter(c -> c.check(map)).collect(Collectors.toList());
            southMove.forEach(c -> c.move(map));
            steps++;
            printMap(map);
            if (eastMove.size() == 0 && southMove.size() == 0) moved = false;
        }


        int debug = 0;

    }

    static void printMap(int[][] map) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                switch (map[y][x]) {
                    case 0:
                        sb.append(".");
                        break;
                    case 1:
                        sb.append(">");
                        break;
                    case 2:
                        sb.append("v");
                        break;
                    default:
                        throw new RuntimeException();
                }
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

}
