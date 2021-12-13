package oostd.am.advent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import oostd.am.advent.tools.FileReader;

public class Day10Plus {

    static boolean isOpenChar(String c) {
        switch (c) {
            case "{":
            case "[":
            case "(":
            case "<":
                return true;
            default:
                return false;
        }
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day10-input");

        Map<String, String> openCloseMap = new HashMap<>();
        openCloseMap.put("{", "}");
        openCloseMap.put("[", "]");
        openCloseMap.put("(", ")");
        openCloseMap.put("<", ">");

        Map<String, String> closeOpenMap = new HashMap<>();
        closeOpenMap.put("}", "{");
        closeOpenMap.put("]", "[");
        closeOpenMap.put(")", "(");
        closeOpenMap.put(">", "<");

        Map<String, Integer> pointsMap = new HashMap<>();
        pointsMap.put("(", 1);
        pointsMap.put("[", 2);
        pointsMap.put("{", 3);
        pointsMap.put("<", 4);

        var answer = input.stream().mapToLong((line) ->{
            String[] split = line.split("");
            Stack<String> openTagStack = new Stack<>();
            for (String c: split) {
                if(isOpenChar(c)){
                    openTagStack.push(c);
                }else{
                    if(!c.equals(openCloseMap.get(openTagStack.pop()))) return -1;
                }
            }
            long points = 0;
            while(!openTagStack.isEmpty()){
                points *= 5;
                points += pointsMap.get(openTagStack.pop());
            }
            return points;
        }).filter(i -> i >= 0).toArray();

        Arrays.sort(answer);

        long beep = answer[answer.length/2];

      System.out.println(answer);
    }

}
