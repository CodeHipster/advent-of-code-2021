package oostd.am.advent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day8 {

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day8-input");

        var answer = input.stream()
                .map(s -> s.split("\\|")[1].trim())
                .flatMap(s -> Arrays.stream(s.split("\\s+")))
                .filter(s -> s.length() == 2 || s.length() == 3 || s.length() == 4 || s.length() == 7)
                .count();
        int debug = 0;
    }

}
