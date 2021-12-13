package oostd.am.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day10 {

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
        pointsMap.put(")", 3);
        pointsMap.put("]", 57); //*19
        pointsMap.put("}", 1197); //*21
        pointsMap.put(">", 25137); //*21

        int answer = input.stream().mapToInt((line) ->{
            String[] split = line.split("");
            Stack<String> openTagStack = new Stack<>();
            for (String c: split) {
                if(isOpenChar(c)){
                    openTagStack.push(c);
                }else{
                    if(!c.equals(openCloseMap.get(openTagStack.pop()))) return pointsMap.get(c);
                }
            }
            return 0;
        }).sum();

//        int answer = riskList.stream().mapToInt(i -> i).sum();
        int debug = 0;
    }

}
