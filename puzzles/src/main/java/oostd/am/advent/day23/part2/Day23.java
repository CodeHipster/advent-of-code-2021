package oostd.am.advent.day23.part2;

import java.util.ArrayDeque;
import java.util.Deque;

public class Day23 {

    public static void main(String[] args) {
//        State initial = new State(".._._._._..BDDACCBDBBACDACA", 0); // test input

//  ###B#B#D#D###
//    #D#C#B#A#
//    #D#B#A#C#
//    #C#A#A#C#

        State initial = new State(".._._._._..BDDCBCBADBAADACC", 0);

        StateProcessor processor = new StateProcessor();

        String target = ".._._._._..AAAABBBBCCCCDDDD";

        Deque<State> states = new ArrayDeque<>();
        states.push(initial);

        long statesVisited = 0;
        int cost = Integer.MAX_VALUE;
        while (!states.isEmpty()) {
            State state = states.pop();
            statesVisited++;
//            System.out.println(state.map);
            State[] nextStates = processor.process(state);
            for (State s : nextStates) {
                if (s.energy < cost) {
                    if (s.map.equals(target)) {
                        cost = s.energy;
                        System.out.println(cost);
                    } else {
                        states.push(s);
                    }
                }
            }
        }
        int debug = 0;
    }
}
