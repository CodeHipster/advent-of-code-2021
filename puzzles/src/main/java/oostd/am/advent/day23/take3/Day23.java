package oostd.am.advent.day23.take3;

import java.util.ArrayDeque;
import java.util.Deque;

public class Day23 {

    public static void main(String[] args) {
//        State initial = new State(".._._._._..BACDBCDA", 0); // test input
        State initial = new State(".._._._._..BCBADADC", 0);

        StateProcessor processor = new StateProcessor();

        String target = ".._._._._..AABBCCDD";

        Deque<State> states = new ArrayDeque<>();
        states.push(initial);

        int cost = Integer.MAX_VALUE;
        while (!states.isEmpty()) {
            State state = states.pop();
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
