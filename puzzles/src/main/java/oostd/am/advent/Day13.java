package oostd.am.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import oostd.am.advent.tools.FileReader;

public class Day13 {

    static class Fold {
        String axis;
        int line;

        public Fold(String axis, int line) {
            this.axis = axis;
            this.line = line;
        }
    }

    static class Position{
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int x;
        int y;
    }

    static class Paper{
        boolean[][] dotMap;

        Paper(List<Position> dots){
            int width = dots.stream().mapToInt(p -> p.x).max().getAsInt() + 1;
            int height = dots.stream().mapToInt(p -> p.y).max().getAsInt() + 1;
            this.dotMap = new boolean[height][width];

            dots.forEach(d -> dotMap[d.y][d.x] = true);
        }

        Paper(boolean[][] dotmap){
            this.dotMap = dotmap;
        }

        Paper fold(Fold fold){
            if(fold.axis.equals("y")){
                boolean[][] newMap = new boolean[fold.line][this.dotMap[0].length];
                for (int y = 0; y < fold.line; y++) {
                    for (int x = 0; x < dotMap[y].length; x++) {
                        newMap[y][x] = dotMap[y][x];
                    }
                }
                for (int y = fold.line + 1; y < dotMap.length; y++) {
                    int newY = fold.line + (fold.line - y);
                    for (int x = 0; x < dotMap[y].length; x++) {
                        if(dotMap[y][x]){
                            // fold over
                            newMap[newY][x] = true;
                        }
                    }
                }
                return new Paper(newMap);
            }else{ //over x
                boolean[][] newMap = new boolean[this.dotMap.length][fold.line];
                for (int x = 0; x < fold.line; x++) {
                    for (int y = 0; y < dotMap.length; y++) {
                        newMap[y][x] = dotMap[y][x];
                    }
                }
                for (int x = fold.line + 1; x < dotMap[0].length; x++) {
                    int newX = fold.line + (fold.line - x);
                    for (int y = 0; y < dotMap.length; y++) {
                        if(dotMap[y][x]){
                            // fold over
                            newMap[y][newX] = true;
                        }
                    }
                }
                return new Paper(newMap);
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int y = 0; y < dotMap.length; y++) {
                for (int x = 0; x < dotMap[y].length; x++) {
                    if(dotMap[y][x])sb.append("#");
                    else sb.append(".");
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day13-input");

        List<Position> dots = new ArrayList<>();

        int index = 0;
        while(!input.get(index).trim().isBlank()){
            String[] split = input.get(index).split(",");
            dots.add(new Position(Integer.parseInt(split[0]),Integer.parseInt(split[1])));
            index++;
        }
        List<Fold> folds = new ArrayList<>();
        ListIterator<String> itr = input.listIterator(++index);
        while(itr.hasNext()){
            String[] s = itr.next().split(" ");
            String[] s2 = s[2].split("=");
            folds.add(new Fold(s2[0], Integer.parseInt(s2[1])));
        }

        Paper paper = new Paper(dots);

        for (int f = 0; f < folds.size(); f++) {
            paper = paper.fold(folds.get(f));
        }



        long count = 0;
        for (int y = 0; y < paper.dotMap.length; y++) {
            for (int x = 0; x < paper.dotMap[y].length; x++) {
                if(paper.dotMap[y][x]) count++;
            }
        }

        int debug = 0;
    }


}
