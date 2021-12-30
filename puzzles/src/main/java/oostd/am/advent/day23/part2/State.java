package oostd.am.advent.day23.part2;

public class State {
    //hall+rooms
    String map;
//    int heuristic;
    int energy;

    public State(String map, int energy) {
        this.map = map;
//        this.heuristic = heuristic;
        this.energy = energy;
    }
}
