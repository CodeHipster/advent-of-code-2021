package oostd.am.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day11 {

    static class Octopus {
        static long totalFlashes = 0;

        private int energy;
        boolean flashed;
        List<Octopus> neighbours;

        Octopus(int energy) {
            this.energy = energy;
        }

        void incrementEnergy() {
            if (!flashed) {
                this.energy++;
                this.flash();
            }
        }

        void setNeighbours(List<Octopus> neighbours) {
            this.neighbours = neighbours;
        }

        public void flash() {
            if (!flashed && energy > 9) {
                this.flashed = true;
                totalFlashes++;
                this.energy = 0;
                neighbours.forEach(Octopus::incrementEnergy);
            }
        }
    }

    static class Location {
        Location(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int x;
        int y;
    }

    static List<Octopus> neighbours(Octopus[][] map, int x, int y) {
        int w = map[0].length;
        int h = map.length;
        return List.of(
                        new Location(-1, -1),
                        new Location(0, -1),
                        new Location(1, -1),
                        new Location(-1, 0),
                        new Location(1, 0),
                        new Location(-1, 1),
                        new Location(0, 1),
                        new Location(1, 1))
                .stream().map(l -> new Location(l.x + x, l.y + y))
                .filter(l -> l.x >= 0 && l.x < w && l.y >= 0 && l.y < h)
                .map(l -> map[l.y][l.x])
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day11-input");

        Octopus[][] octopi = input.stream()
                .map(l -> Arrays.stream(l.split(""))
                        .map(d -> new Octopus(Integer.parseInt(d)))
                        .toArray(Octopus[]::new))
                .toArray(Octopus[][]::new);

        for (int y = 0; y < octopi.length; y++) {
            for (int x = 0; x < octopi[y].length; x++) {
                octopi[y][x].setNeighbours(neighbours(octopi, x, y));
            }
        }

        for (int i = 0; i < 100; i++) {
            //increase by 1
            Arrays.stream(octopi).flatMap(Arrays::stream).forEach(o -> o.energy++);
            // flash
            Arrays.stream(octopi).flatMap(Arrays::stream).forEach(Octopus::flash);
            // reset
            Arrays.stream(octopi).flatMap(Arrays::stream).forEach(o -> o.flashed = false);
        }
        long answer = Octopus.totalFlashes;
        int debug = 0;
    }

}
