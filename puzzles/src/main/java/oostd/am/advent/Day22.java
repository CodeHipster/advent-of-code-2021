package oostd.am.advent;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import oostd.am.advent.tools.FileReader;

public class Day22 {

    static class Reactor{
        boolean[][][] cores = new boolean[101][101][101]; //default all off.

        void apply(Cuboid c){
            for (int z = c.zFrom; z <= c.zTo; z++) {
                for (int y = c.yFrom; y <= c.yTo; y++) {
                    for (int x = c.xFrom; x <= c.xTo; x++) {
                        cores[z][y][x] = c.state;
                    }
                }
            }
        }
    }

    static class Cuboid {
        int xFrom;
        int xTo;
        int yFrom;
        int yTo;
        int zFrom;
        int zTo;

        boolean state;

        Cuboid(String input) {
            Pattern pattern = Pattern.compile("^(?<state>(on|off)) x=(?<xfrom>-?\\d{1,2})\\.\\.(?<xto>-?\\d{1,2}),y=(?<yfrom>-?\\d{1,2})\\.\\.(?<yto>-?\\d{1,2}),z=(?<zfrom>-?\\d{1,2})\\.\\.(?<zto>-?\\d{1,2})$");
            Matcher matcher = pattern.matcher(input);
            matcher.find();
            // offset to start at 0;
            xFrom = Integer.parseInt(matcher.group("xfrom")) +50;
            xTo = Integer.parseInt(matcher.group("xto")) +50;
            yFrom = Integer.parseInt(matcher.group("yfrom")) +50;
            yTo = Integer.parseInt(matcher.group("yto")) +50;
            zFrom = Integer.parseInt(matcher.group("zfrom")) +50;
            zTo = Integer.parseInt(matcher.group("zto")) +50;
            state = matcher.group("state").equals("on");
        }
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day22-test-input");
        List<Cuboid> cuboids = input.stream().map(Cuboid::new).collect(Collectors.toList());

        Reactor r = new Reactor();

        cuboids.forEach(c -> r.apply(c));

        long on = Arrays.stream(r.cores)
                .flatMap(Arrays::stream)
                .flatMap(d -> IntStream.range(0, d.length).mapToObj(idx -> d[idx]))
                .filter(b -> b == true)
                .count();

        int debug = 0;
    }
}
