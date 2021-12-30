package oostd.am.advent.day23.take3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StateProcessor {
    // <index, indexes of neighbours>
    Map<Integer, Integer[]> neighbourMap = new HashMap<>();
    // <pod letter, indexes of room>
    Map<Character, Integer[]> rooms = new HashMap<>();
    Map<Character, Integer> energyMap = new HashMap<>();

    //            #############
    //            #01234567890#
    //            ###1#3#5#7###
    //              #2#4#6#8#
    //              #########
    StateProcessor() {
        neighbourMap.put(0, new Integer[]{1});
        neighbourMap.put(1, new Integer[]{0, 2});
        neighbourMap.put(2, new Integer[]{1, 3, 11});
        neighbourMap.put(3, new Integer[]{2, 4});
        neighbourMap.put(4, new Integer[]{3, 5, 13});
        neighbourMap.put(5, new Integer[]{4, 6});
        neighbourMap.put(6, new Integer[]{5, 7, 15});
        neighbourMap.put(7, new Integer[]{6, 8});
        neighbourMap.put(8, new Integer[]{7, 9, 17});
        neighbourMap.put(9, new Integer[]{8, 10});
        neighbourMap.put(10, new Integer[]{9});
        neighbourMap.put(11, new Integer[]{2, 12});
        neighbourMap.put(12, new Integer[]{11});
        neighbourMap.put(13, new Integer[]{4, 14});
        neighbourMap.put(14, new Integer[]{13});
        neighbourMap.put(15, new Integer[]{6, 16});
        neighbourMap.put(16, new Integer[]{15});
        neighbourMap.put(17, new Integer[]{8, 18});
        neighbourMap.put(18, new Integer[]{17});

        rooms.put('A', new Integer[]{11, 12});
        rooms.put('B', new Integer[]{13, 14});
        rooms.put('C', new Integer[]{15, 16});
        rooms.put('D', new Integer[]{17, 18});

        energyMap.put('A', 1);
        energyMap.put('B', 10);
        energyMap.put('C', 100);
        energyMap.put('D', 1000);
    }

    State[] process(State state) {
        List<Move> moves = new ArrayList<>();
        char[] map = state.map.toCharArray();
        for (int i = 0; i < map.length; i++) {
            // Find pods.
            if (map[i] != '.' && map[i] != '_') {
                char pod = map[i];
                Integer[] room = rooms.get(pod);
                // only move if we are not in position.
                if(inPosition(i, map, room)) continue;
                Integer[] allowed = allowed(map, i, room);
                // traverse the burrow, adding only allowed moves.
                List<Move> podMoves = traverse(-1, i, i, map, 0, allowed);
                moves.addAll(podMoves);
            }
        }
        State[] states = new State[moves.size()];

        for (int i = 0; i < moves.size(); i++) {
            char[] newMap = Arrays.copyOf(map, map.length);
            Move move = moves.get(i);
            int from = move.from;
            newMap[from] = '.'; // leaving start empty
            newMap[move.destination] = map[from]; // filling destination with pod.
            states[i] = new State(new String(newMap), state.energy + energyMap.get(map[from]) * move.steps);
        }

        return states;
    }

    boolean inPosition(int pod, char[] map, Integer[] room) {
        return pod == room[1] || // in last spot in room
                (pod == room[0] // in first room spot
                        && map[room[1]] == map[pod]); // and last spot is occupied by correct pod.
    }

    // pod is not in position.
    // -1 for previous means no previous. (nothing matches -1)
    private List<Move> traverse(int previous, int start, int index, char[] map, int steps, Integer[] allowed) {
        List<Move> moves = new ArrayList<>(10);
        Integer[] neighbours = neighbourMap.get(index);
        for (int next : neighbours) {
            if(next == previous) continue;
            if (map[next] == '.' || map[next] == '_') {
                for (int a: allowed) {
                    if(next == a){
                        moves.add(new Move(start, steps +1, next));
                        break; // no need to go over other allowed.
                    }
                }
                moves.addAll(traverse(index, start, next, map, steps + 1, allowed));
            }
        }
        return moves;
    }

    // pod is not in position
    private Integer[] allowed(char[] map, int pod, Integer[] room) {
        // determine which tiles are allowed.
        boolean occupied = occupied(map,map[pod],room);
        Integer[] allowed;
        if(occupied){
            allowed = new Integer[0];
        }else{ // only allow the last place of the room.
            if(map[room[1]] == '.') allowed = new Integer[]{room[1]};
            else allowed = new Integer[]{room[0]};
        }
        // from room, hall is allowed.
        if (pod > 10){
            allowed = concat(allowed, new Integer[]{0, 1, 3, 5, 7, 9, 10});
        }
        return allowed;
    }

    private <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    private boolean occupied(char[] map, char pod, Integer[] room) {
        for (int r : room) {
            if (map[r] != '.' && map[r] != pod) return true;
        }
        return false;
    }

    static class Move {
        int steps;
        int from;
        int destination;

        public Move(int start, int steps, int destination) {
            this.from = start;
            this.steps = steps;
            this.destination = destination;
        }
    }
}
