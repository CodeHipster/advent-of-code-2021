package oostd.am.advent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day7Plus {

    //<distance, fuelRequired>
    static HashMap<Integer, Integer> fuelCache = new HashMap<>();

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day7-input");

        // days and count of fish

        var positions = Arrays.stream(input.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        var stats = positions.stream().mapToInt(i -> i).summaryStatistics();

        //<position, totalDistance>
        Map<Integer, Long> distanceMap = new HashMap<>();

        for(int i = stats.getMin(); i < stats.getMax(); i++){
            distanceMap.put(i, fuelConsumption(i, positions));
        }

        long answer = distanceMap.values().stream().mapToLong(l -> l).min().getAsLong();
        int debug = 0;
    }

    private static long fuelConsumption(int position, List<Integer> positions){
        return positions.stream().mapToInt(i -> Math.abs(i - position)).map(Day7Plus::fuel).sum();
    }

    private static int fuel(int distance){
        return fuelCache.computeIfAbsent(distance, d -> {
            int fuelConsumption = 0;
            for (int i = 1; i < d+1; i++) {
                fuelConsumption += i;
            }
            return fuelConsumption;
        });

    }

}
