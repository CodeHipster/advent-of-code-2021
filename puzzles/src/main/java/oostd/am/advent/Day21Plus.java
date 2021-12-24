package oostd.am.advent;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day21Plus {

    static class Universe {
        int[] scores;
        int[] positions;
        int[] rolls;
        int turn;

        public Universe(
                int[] s,
                int[] p,
                int[] r,
                int d,
                int t) {
            positions = Arrays.copyOf(p, 2);
            scores = Arrays.copyOf(s, 2);
            rolls = Arrays.copyOf(r, r.length + 1);
            rolls[rolls.length - 1] = d;
            turn = t;

            if (rolls.length == 3) {
                // calculate scores
                int steps = rolls[0] + rolls[1] + rolls[2];
                positions[t] = (((positions[t] - 1) + steps) % 10) + 1;
                scores[t] += positions[t];

                turn = (t == 0) ? 1 : 0;
                rolls = new int[0];
            }
        }

        int winner() {
            if (rolls.length == 0) {
                int lastPoints = (turn == 0) ? 1 : 0;
                if (scores[lastPoints] >= 21) return lastPoints;
            }
            return -1;
        }

        List<Universe> roll() {
            return Arrays.asList(
                    new Universe(scores, positions, rolls, 3, turn),
                    new Universe(scores, positions, rolls, 2, turn),
                    new Universe(scores, positions, rolls, 1, turn));
        }
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day21-test-input");

        int p0Pos = Integer.parseInt(input.get(0).substring(28, 29));
        int p1Pos = Integer.parseInt(input.get(1).substring(28, 29));

        Universe start1 = new Universe(new int[]{0,0}, new int[]{p0Pos, p1Pos}, new int[0], 1,0);
        Universe start2 = new Universe(new int[]{0,0}, new int[]{p0Pos, p1Pos}, new int[0], 2,0);
        Universe start3 = new Universe(new int[]{0,0}, new int[]{p0Pos, p1Pos}, new int[0], 3,0);

        Deque<Universe> universes = new ArrayDeque<>(Arrays.asList(start3, start2, start1));

        BigInteger[] universeWins = new BigInteger[]{BigInteger.valueOf(0), BigInteger.valueOf(0)};

        while(!universes.isEmpty()){
            Universe universe = universes.removeLast();
            int winner = universe.winner();
            if(winner >= 0){
                universeWins[winner] = universeWins[winner].add(BigInteger.ONE);
                continue;
            }
            universes.addAll(universe.roll());
        }

        System.out.println(universeWins[0].toString());
        System.out.println(universeWins[1].toString());

        int debug = 0;

    }
}
