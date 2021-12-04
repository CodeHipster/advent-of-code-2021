package oostd.am.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;



class Game2 extends Game{

    @Override
    public long play() {
        int index = 0;
        while(index < numbers.length){
            int nr = numbers[index];
            Arrays.stream(boards).filter(board-> !board.bingo).forEach(board -> board.apply(nr));
            List<Board> winners = Arrays.stream(boards).filter(board-> !board.bingo).filter(Board::bingo).collect(Collectors.toList());
            long totalWinners = Arrays.stream(boards).filter(board -> board.bingo).count();
            if(totalWinners == boards.length){
                return winners.get(0).calculateScore(nr);
            }
            index++;
        }
        throw new RuntimeException("not all boards get bingo.");
    }

    Game2(String file){
        super(file);
    }
}

public class Day4Plus {
    public static void main(String[] args) {
        Game2 game = new Game2("day4-input");

        long winner = game.play();


        int debug = 0;

    }


}
