package oostd.am.advent;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day20Plus {

    static int booleansToInt(List<Boolean> arr) {
        int n = 0;
        for (boolean b : arr)
            n = (n << 1) | (b ? 1 : 0);
        return n;
    }

    static List<Boolean> neighbours(boolean[][] pxls, int x, int y, boolean inf) {
        int w = pxls[0].length;
        int h = pxls.length;
        return List.of(
                        new Day11.Location(-1, -1),
                        new Day11.Location(0, -1),
                        new Day11.Location(1, -1),
                        new Day11.Location(-1, 0),
                        new Day11.Location(0, 0),
                        new Day11.Location(1, 0),
                        new Day11.Location(-1, 1),
                        new Day11.Location(0, 1),
                        new Day11.Location(1, 1))
                .stream().map(l -> new Day11.Location(l.x + x, l.y + y))
                .map(l -> (l.x >= 0 && l.x < w && l.y >= 0 && l.y < h) ? l : null)
                .map(l -> {
                    if (l == null) return inf;
                    else return pxls[l.y][l.x];
                })
                .collect(Collectors.toList());
    }

    static boolean[] parse(String input) {
        List<Boolean> collect = Arrays.stream(input.split("")).map(s -> s.equals("#")).collect(Collectors.toList());
        boolean[] array = new boolean[collect.size()];
        for (int i = 0; i < collect.size(); i++) {
            array[i] = collect.get(i);
        }
        return array;
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day20-input");
        Iterator<String> itr = input.iterator();

        Algorithm algorithm = new Algorithm(itr.next());

        itr.next(); //skip empty line

        boolean[][] pxl = new boolean[100][100];
        int i = 0;
        while (itr.hasNext()) {
            pxl[i] = parse(itr.next());
            i++;
        }

        Image image = new Image(pxl, false);
        for (int j = 0; j < 50; j++) {
            image = image.enhance(algorithm);
        }

        int count = 0;
        for (boolean[] r : image.pixels
        ) {
            for (boolean b : r
            ) {
                if (b) count++;
            }
        }
        int debug = 0;
    }

    static class Algorithm {
        boolean[] code;

        Algorithm(String input) {
            code = parse(input);
        }
    }

    static class Image {
        boolean[][] pixels;
        boolean inf; //the value for infinity

        public Image(boolean[][] pixels, boolean inf) {
            this.pixels = pixels;
            this.inf = inf;
        }

        Image enhance(Algorithm algorithm) {
            // enlarge grid
            Image enhanced = new Image(new boolean[pixels.length + 2][pixels[0].length + 2], !inf);
            Image expanded = new Image(new boolean[pixels.length + 2][pixels[0].length + 2], inf);

            // set the default value;
            for (int y = 0; y < expanded.pixels.length; y++) {
                for (int x = 0; x < expanded.pixels[0].length; x++) {
                    expanded.pixels[y][x] = inf;
                }
            }

            for (int y = 0; y < this.pixels.length; y++) {
                for (int x = 0; x < this.pixels[0].length; x++) {
                    expanded.pixels[y + 1][x + 1] = this.pixels[y][x];

                }
            }
            for (int y = 0; y < expanded.pixels.length; y++) {
                for (int x = 0; x < expanded.pixels[0].length; x++) {
                    int index = getIndex(expanded.pixels, x, y);
                    enhanced.pixels[y][x] = algorithm.code[index];
                }
            }

            inf = !inf;
            return enhanced;
        }

        int getIndex(boolean[][] map, int x, int y) {
            List<Boolean> neighbours = neighbours(map, x, y, inf);
            int i = booleansToInt(neighbours);
            return i;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Image:");
            Arrays.stream(pixels).forEach(r -> {
                sb.append("\n");
                for (boolean b : r) {
                    sb.append((b) ? '#' : '.');
                }
            });
            return sb.toString();
        }
    }


}
