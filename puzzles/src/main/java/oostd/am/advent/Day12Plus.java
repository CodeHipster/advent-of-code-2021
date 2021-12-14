package oostd.am.advent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day12Plus {

    enum CaveType {
        start,
        end,
        big,
        small
    }

    static class Line {
        String left;
        String right;

        Line(String left, String right) {
            this.left = left;
            this.right = right;
        }
    }

    static boolean doubleSmallCave(Collection<Cave> visited){
        List<String> smallList = visited.stream().filter(c -> c.type == CaveType.small).map(c -> c.name).collect(Collectors.toList());
        Set<String> smallSet = new HashSet<>(smallList);
        return smallList.size() != smallSet.size();
    }

    static class Route {
        Route(Deque<Cave> visited) {
            this.visitedCaves = visited;
        }

        boolean finished = false;
        boolean deadEnd = false;
        Deque<Cave> visitedCaves = new ArrayDeque<>();

        List<Route> branch() {
            Cave current = visitedCaves.peekLast();
            if (current.type == CaveType.end) return new ArrayList<>();
            List<Cave> skip = visitedCaves.stream()
                    .filter(c -> (c.type == CaveType.small && doubleSmallCave(visitedCaves))
                            || c.type == CaveType.start
                            || c.equals(current))
                    .collect(Collectors.toList());
            return current.connections.stream()
                    .filter(c -> !skip.contains(c))
                    .map(c -> {
                        Route route = new Route(new ArrayDeque<>(visitedCaves));
                        route.visitedCaves.add(c);
                        return route;
                    })
                    .collect(Collectors.toList());
        }

        @Override
        public String toString() {
            return "Route: "+ visitedCaves.stream().sequential().map(c -> c.name).collect(Collectors.joining("-"));
        }
    }

    static class Cave {
        String name;
        CaveType type;
        List<Cave> connections = new ArrayList<>();

        Cave(String name) {
            this.name = name;
            if (name.equals("start")) {
                this.type = CaveType.start;
            } else if (name.equals("end")) {
                this.type = CaveType.end;
            } else if (name.toLowerCase().equals(name)) {
                this.type = CaveType.small;
            } else {
                this.type = CaveType.big;
            }
        }

        Cave add(Cave cave) {
            connections.add(cave);
            return this;
        }
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day12-input");

        List<Line> lines = input.stream().map(s -> {
            String[] split = s.split("-");
            return new Line(split[0], split[1]);
        }).collect(Collectors.toList());

        HashMap<String, Cave> caveMap = new HashMap<>();
        lines.forEach(l -> {
            Cave left = new Cave(l.left);
            Cave right = new Cave(l.right);
            left.add(right);
            right.add(left);
            caveMap.merge(l.left, left, (old, value) -> old.add(right));
            caveMap.merge(l.right, right, (old, value) -> old.add(left));
        });

        //fix connections
        caveMap.values().forEach(cave -> {
            cave.connections = cave.connections.stream().map(c -> caveMap.get(c.name)).collect(Collectors.toList());
        });

        List<Route> routing = new ArrayList<>();
        ArrayDeque<Cave> start = new ArrayDeque<>();
        start.add(caveMap.get("start"));
        routing.add(new Route(start));

        List<Route> finished = new ArrayList<>();

        while (!routing.isEmpty()) {
            List<Route> swap = new ArrayList<>();
            for (Route r : routing) {
                if(r.visitedCaves.peekLast().type == CaveType.end){
                    finished.add(r);
                }else{
                    swap.addAll(r.branch());
                }
            }
            System.out.println("routing------");
            swap.forEach(System.out::println);
            System.out.println("finished------");
            finished.forEach(System.out::println);
            System.out.println("------");

            routing = swap;
        }

        System.out.println(finished.size());
        int debug = 0;
    }


}
