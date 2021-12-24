package oostd.am.advent;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oostd.am.advent.tools.FileReader;

public class Day21PlusTake2 {

    static class Move {
        List<Integer> p1Steps;
        List<Integer> p2Steps;

        int p1Score;
        int p2Score;
        int p1Position;
        int p2Position;

        public Move(int step, List<Integer> p1Steps, List<Integer> p2Steps, int p1Score, int p2Score, int p1Position, int p2Position) {
            this.p1Steps = new ArrayList<>(p1Steps);
            this.p2Steps = new ArrayList<>(p2Steps);
            this.p1Score = p1Score;
            this.p2Score = p2Score;
            this.p1Position = p1Position;
            this.p2Position = p2Position;

            if (this.p1Steps.size() == this.p2Steps.size()) {
                // p1 does the step
                this.p1Steps.add(step);
                this.p1Position = (((this.p1Position - 1) + step) % 10) + 1;
                this.p1Score += this.p1Position;
            } else {
                // p2 does the step
                this.p2Steps.add(step);
                this.p2Position = (((this.p2Position - 1) + step) % 10) + 1;
                this.p2Score += this.p2Position;
            }
        }

        int winner() {
            if (p1Score >= 21) return 1;
            else if (p2Score >= 21) return 2;
            else return 0;
        }

        Deque<Move> move() {
            Deque<Move> moves = new ArrayDeque<>();
            for (int i = 3; i < 10; i++) {
                moves.push(new Move(i, p1Steps, p2Steps, p1Score, p2Score, p1Position, p2Position));
            }
            return moves;
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<String> input = new FileReader().readInput("day21-input");

        int p1Pos = Integer.parseInt(input.get(0).substring(28, 29));
        int p2Pos = Integer.parseInt(input.get(1).substring(28, 29));

        Deque<Move> moves = new ArrayDeque<>(Arrays.asList());

        BigInteger p1Unis = BigInteger.valueOf(0);
        BigInteger p2Unis = BigInteger.valueOf(0);

        // steps, combinations
        Map<Integer, Long> combinations = new HashMap<>();
        for (int i = 3; i < 10; i++) {
            combinations.put(i, 0L);
            moves.push(new Move(i, new ArrayList<>(), new ArrayList<>(), 0, 0, p1Pos, p2Pos));
        }
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                for (int k = 1; k < 4; k++) {
                    int sum = i + j + k;
                    combinations.merge(sum, 1L, Long::sum);
                }
            }
        }

        while (!moves.isEmpty()) {
            Move move = moves.removeLast();
            int winner = move.winner();
            if (winner > 0) {
                long unis = 1L;
                for (int step : move.p1Steps) {
                    unis = unis * combinations.get(step);
                }
                for (int step : move.p2Steps) {
                    unis = unis * combinations.get(step);
                }
                if (winner == 1) {
                    p1Unis = p1Unis.add(BigInteger.valueOf(unis));
                } else {
                    p2Unis = p2Unis.add(BigInteger.valueOf(unis));
                }
                continue;
            }
            moves.addAll(move.move());
        }

        System.out.println(p1Unis);
        System.out.println(p2Unis);

        System.out.println((System.currentTimeMillis() - start)/1000);

        int debug = 0;

    }
}
