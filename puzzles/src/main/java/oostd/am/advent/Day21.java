package oostd.am.advent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day21 {
    static class Pair {
        Player p;
        int position;

        public Pair(Player p, int position) {
            this.p = p;
            this.position = position;
        }
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day21-input");

        Dice d = new Dice();

        List<Pair> players = input.stream().map(s -> new Pair(
                        new Player(Integer.parseInt(s.substring(7, 8))),
                        Integer.parseInt(s.substring(28, 29))))
                .collect(Collectors.toList());

        Board board = new Board(d, players);

        board.play();

        Player loser = board.players.keySet().stream().min((o1, o2) -> Integer.compare(o1.score, o2.score)).get();
        int totalRolls = board.players.keySet().stream().mapToInt(p -> p.rolls).sum();

        long answer = loser.score * totalRolls;
        int debug = 0;
    }

    static class Player {
        int id;
        int score;
        int rolls;

        public Player(int id) {
            this.id = id;
            this.score = 0;
            this.rolls = 0;
        }

        boolean score(int boardValue){
            this.score += boardValue;
            return score >= 1000;
        }

        int roll(Dice d){
            rolls++;
            return d.roll();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Player player = (Player) o;
            return id == player.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    static class Dice {
        int previousValue = 100;

        int roll() {
            if (previousValue < 100) ++previousValue;
            else previousValue = 1;
            return previousValue;
        }

    }

    static class Board {
        Dice dice;
        // player and their position on the board
        Map<Player, Integer> players = new HashMap<>();
        Player turn;

        public Board(Dice dice, List<Pair> players) {
            this.dice = dice;
            // map position from 1-10 to 0-9;
            this.players = players.stream().collect(Collectors.toMap(p -> p.p, p -> p.position));
            this.turn = this.players.keySet().stream().filter(p -> p.id == 1).findFirst().get();
        }

        void play() {
            boolean finished = false;
            while(!doTurn()){}
        }

        // return true if a player has won.
        private boolean doTurn() {
            int steps = 0;
            for (int i = 0; i < 3; i++) {
                steps += turn.roll(dice);
            }
            final int s = steps;
            players.compute(turn, (p, pos) -> (((pos-1)+s)%10)+1);
            int boardValue = players.get(turn);
            if(turn.score(boardValue)){
                return true;
            }else{
                turn = players.keySet().stream().filter(p -> !p.equals(turn)).findFirst().get();
                return false;
            }
        }
    }
}
