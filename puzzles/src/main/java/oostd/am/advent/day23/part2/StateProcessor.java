package oostd.am.advent.day23.part2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    //            ###1#5#9#3###
    //              #2#6#0#4#
    //              #3#7#1#5#
    //              #4#8#2#6#
    //              #########

    StateProcessor() {
        neighbourMap.put(0, new Integer[]{1});
        neighbourMap.put(1, new Integer[]{0, 2});
        neighbourMap.put(2, new Integer[]{1, 3, 11});
        neighbourMap.put(3, new Integer[]{2, 4});
        neighbourMap.put(4, new Integer[]{3, 5, 15});
        neighbourMap.put(5, new Integer[]{4, 6});
        neighbourMap.put(6, new Integer[]{5, 7, 19});
        neighbourMap.put(7, new Integer[]{6, 8});
        neighbourMap.put(8, new Integer[]{7, 9, 23});
        neighbourMap.put(9, new Integer[]{8, 10});
        neighbourMap.put(10, new Integer[]{9});
        neighbourMap.put(11, new Integer[]{2, 12});
        neighbourMap.put(12, new Integer[]{11, 13});
        neighbourMap.put(13, new Integer[]{12, 14});
        neighbourMap.put(14, new Integer[]{13});
        neighbourMap.put(15, new Integer[]{4, 16});
        neighbourMap.put(16, new Integer[]{15, 17});
        neighbourMap.put(17, new Integer[]{16, 18});
        neighbourMap.put(18, new Integer[]{17});
        neighbourMap.put(19, new Integer[]{6, 20});
        neighbourMap.put(20, new Integer[]{19, 21});
        neighbourMap.put(21, new Integer[]{20, 22});
        neighbourMap.put(22, new Integer[]{21});
        neighbourMap.put(23, new Integer[]{8, 24});
        neighbourMap.put(24, new Integer[]{23, 25});
        neighbourMap.put(25, new Integer[]{24, 26});
        neighbourMap.put(26, new Integer[]{25});

        rooms.put('A', new Integer[]{11, 12, 13, 14});
        rooms.put('B', new Integer[]{15, 16, 17, 18});
        rooms.put('C', new Integer[]{19, 20, 21, 22});
        rooms.put('D', new Integer[]{23, 24, 25, 26});

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
                if (inPosition(i, map, room)) continue;
                Integer[] allowed = allowed(map, i, room);
                if (allowed.length > 0) {
                    // traverse the burrow, adding only allowed moves.
                    List<Move> podMoves = traverse(-1, i, i, map, 0, allowed);
                    moves.addAll(podMoves);
                }// else nowhere to move too. continue.
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

    // can return true, even if room is occupied by a wrong type.
    boolean inPosition(int pod, char[] map, Integer[] room) {
        if (pod < room[0] || pod > room[3]) return false; // not in the room
        for (int i = 3; i >= 0; i--) {
            // if room is occupied by wrong type. We are not in position.
            if (map[room[i]] != map[pod]) return false;
            // if pod in room we are in position
            // (even if towards the exit is occupied by a wrong type.)
            if (room[i] == pod) return true;
            // Room position occupied by correct type.
            // continue to move towards exit.
        }
        throw new RuntimeException("should not get here.");
    }

    // pod is not in position.
    // -1 for previous means no previous. (nothing matches -1)
    private List<Move> traverse(int previous, int start, int index, char[] map, int steps, Integer[] allowed) {
        List<Move> moves = new ArrayList<>(10);
        Integer[] neighbours = neighbourMap.get(index);
        for (int next : neighbours) {
            if (next == previous) continue;
            if (map[next] == '.' || map[next] == '_') {// if empty or exit, we keep moving.
                if(map[next] == '.'){ // if empty, we might add it as a move.
                    for (int a : allowed) {
                        if (next == a) {
                            moves.add(new Move(start, steps + 1, next));
                            break; // no need to go over other allowed.
                        }
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
        boolean occupied = occupied(map, map[pod], room);
        Integer[] allowed = new Integer[0];
        if (!occupied) { // room is allowed.
            // only allow the last free place of the room.
            for (int i = 3; i >= 0; i--) {
                // find first place from the back that is empty.
                if (map[room[i]] == '.') {
                    allowed = new Integer[]{room[i]};
                    break;
                }
            }
        }

        if (pod > 10) {// pod is in a room, from room, hall is allowed.
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
