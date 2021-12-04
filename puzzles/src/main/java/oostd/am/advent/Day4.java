package oostd.am.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

class Square {
    Square(int number) {
        this.number = number;
        this.checked = false;
    }

    int number;
    boolean checked;

    public void apply(int nr) {
        if (number == nr) checked = true;
    }
}

class Board {
    boolean bingo;
    Square[][] numbers = new Square[5][5];

    public void apply(int nr) {
        Arrays.stream(numbers).flatMap(Arrays::stream).forEach(square -> square.apply(nr));
    }

    public boolean bingo() {
        // check rows
        for(int r = 0; r < 5; r++){
            long count = Arrays.stream(numbers[r]).filter(square -> square.checked).count();
            if(count == 5)
                bingo = true;
        }
        // check columns
        for(int c = 0; c < 5; c++){
            final int colNr = c;
            long count = Arrays.stream(numbers).map(column -> column[colNr]).filter(square -> square.checked).count();
            if(count == 5)
                bingo = true;
        }
        return bingo;
    }

    public long calculateScore(int nr) {
        long sum = Arrays.stream(numbers).flatMap(Arrays::stream).filter(s -> !s.checked).mapToLong(s -> s.number).sum();
        return sum * nr;
    }
}

class Game {
    int[] numbers;
    Board[] boards;

    public long play() {
        int index = 0;
        while(true){
            int nr = numbers[index];
            Arrays.stream(boards).forEach(board -> board.apply(nr));
            List<Board> winners = Arrays.stream(boards).filter(Board::bingo).collect(Collectors.toList());
            if(!winners.isEmpty()){
                return winners.get(0).calculateScore(nr);
            }
            index++;
        }
    }
    Game(String file) {
        List<String> input = new FileReader().readInput(file);

        this.numbers = Arrays.stream(input.get(0).split(","))
                .mapToInt(Integer::parseInt).toArray();
        List<Board> boards = new ArrayList<>();

        int line = 1;
        while (line < input.size()) {
            if (input.get(line).trim().equals("")) {
                line++;
                continue;
            }
            Board board = new Board();
            for (int i = 0; i < 5; i++) {
                Square[] row = Arrays.stream(input.get(line).split("\\s+"))
                        .filter(s -> !s.isBlank())
                        .mapToInt(Integer::parseInt)
                        .mapToObj(Square::new).toArray(Square[]::new);
                board.numbers[i] = row;
                line++;
            }
            boards.add(board);
        }
        this.boards = boards.toArray(new Board[0]);
    }
}

public class Day4 {
    public static void main(String[] args) {
        Game game = new Game("day4-input");

        long winner = game.play();


        int debug = 0;

    }
}
