package oostd.am.advent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day22Plus {

    static class Cuboid {
        int xFrom;
        int xTo;
        int yFrom;
        int yTo;
        int zFrom;
        int zTo;
        boolean state;
        List<Cuboid> intersections = new ArrayList<>();

        static Cuboid fromString(String input) {
            Pattern pattern = Pattern.compile("^(?<state>(on|off)) x=(?<xfrom>-?\\d+)\\.\\.(?<xto>-?\\d+),y=(?<yfrom>-?\\d+)\\.\\.(?<yto>-?\\d+),z=(?<zfrom>-?\\d+)\\.\\.(?<zto>-?\\d+)$");
            Matcher matcher = pattern.matcher(input);
            matcher.find();

            return new Cuboid(Integer.parseInt(matcher.group("xfrom")),
                    Integer.parseInt(matcher.group("xto")),
                    Integer.parseInt(matcher.group("yfrom")),
                    Integer.parseInt(matcher.group("yto")),
                    Integer.parseInt(matcher.group("zfrom")),
                    Integer.parseInt(matcher.group("zto")),
                    matcher.group("state").equals("on"));
        }

        public Cuboid(int xFrom, int xTo, int yFrom, int yTo, int zFrom, int zTo, boolean state) {
            this.xFrom = xFrom;
            this.xTo = xTo;
            assert (this.xFrom < this.xTo);
            this.yFrom = yFrom;
            this.yTo = yTo;
            assert (this.yFrom < this.yTo);
            this.zFrom = zFrom;
            this.zTo = zTo;
            this.state = state;
            assert (this.zFrom < this.zTo);
        }

        public void intersect(Cuboid that) {
            this.intersections.forEach(t -> t.intersect(that));
            int iXMin = Math.max(this.xFrom, that.xFrom);
            int iXMax = Math.min(this.xTo, that.xTo);
            if (iXMin > iXMax) return;
            int iYMin = Math.max(this.yFrom, that.yFrom);
            int iYMax = Math.min(this.yTo, that.yTo);
            if (iYMin > iYMax) return;
            int iZMin = Math.max(this.zFrom, that.zFrom);
            int iZMax = Math.min(this.zTo, that.zTo);
            if (iZMin > iZMax) return;
            this.intersections.add(new Cuboid(iXMin, iXMax, iYMin, iYMax, iZMin, iZMax, that.state));
        }

        long volume() {
            long volume = (long) ((xTo + 1) - xFrom) * ((yTo + 1) - yFrom) * ((zTo + 1) - zFrom);
            for (Cuboid c : intersections) {
                volume -= c.volume();
            }
            return volume;
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<String> input = new FileReader().readInput("day22-input");
        List<Cuboid> cuboids = input.stream().map(Cuboid::fromString).collect(Collectors.toList());

        int minx, maxx, miny, maxy, minz, maxz;
        minx = cuboids.stream().map(c -> c.xFrom).min(Integer::compare).get();
        maxx = cuboids.stream().map(c -> c.xTo).max(Integer::compare).get();
        miny = cuboids.stream().map(c -> c.yFrom).min(Integer::compare).get();
        maxy = cuboids.stream().map(c -> c.yTo).max(Integer::compare).get();
        minz = cuboids.stream().map(c -> c.zFrom).min(Integer::compare).get();
        maxz = cuboids.stream().map(c -> c.zTo).max(Integer::compare).get();

        Cuboid reactor = new Cuboid(minx, maxx, miny, maxy, minz, maxz, false);

        cuboids.forEach(reactor::intersect);

        long on = reactor.intersections.stream().filter(c -> c.state).mapToLong(Cuboid::volume).sum();

        System.out.println((System.currentTimeMillis() - start));
        System.out.println(on);


        int debug = 0;
    }
}
