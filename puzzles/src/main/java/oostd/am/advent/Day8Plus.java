package oostd.am.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import oostd.am.advent.tools.FileReader;
import oostd.am.advent.tools.UniqueCombinationGenerator;

public class Day8Plus {

    public static void main(String[] args) {
        long start = System.nanoTime();
        List<String> input = new FileReader().readInput("day8-input");

//         aaaa
//        b    c
//        b    c
//         dddd
//        e    f
//        e    f
//         gggg

        String[] lines = {"a", "b", "c", "d", "e", "f", "g"};
        List<String> digits = List.of("abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf", "abcdefg", "abcdfg");

        int answer = input.stream()
                .map(s -> s.split("\\|"))
                .mapToInt(in -> {
                    var solution = Stream
                            .generate(new UniqueCombinationGenerator(7)).map(code -> {
                                var decoded = Arrays.stream(code).mapToObj(i -> lines[i - 1]).collect(Collectors.toList());
                                Map<String, String> decodeMap = new HashMap<>();
                                for (int i = 0; i < 7; i++) {
                                    decodeMap.put(lines[i], decoded.get(i));
                                }
                                return decodeMap;
                            })
                            .filter(decodeMap -> {
                                List<String> words = Arrays.stream(in[0].trim().split("\\s+")).collect(Collectors.toList());
                                List<String> decodedWords = words.stream()
                                        .map(w -> Arrays.stream(w.split("")).map(decodeMap::get).collect(Collectors.joining()))
                                        .collect(Collectors.toList());
                                for (int i = 0; i < 10; i++) {
                                    if (!contains(digits, decodedWords.get(i))) {
                                        return false;
                                    } else {
                                        boolean match = true;
                                    }
                                }
                                return true;
                            }).findFirst().get();
                    String[] display = in[1].trim().split("\\s+");
                    var decodedDisplay = Arrays.stream(display)
                            .map(segment -> Arrays.stream(segment.split(""))
                                    .map(solution::get)
                                    .collect(Collectors.joining()))
                            .map(segment -> index(digits, segment))
                            .map(d -> "" + d)
                            .collect(Collectors.joining());
//TODO: get the digits and return the number.
                    return Integer.parseInt(decodedDisplay);
                }).sum();


        int debug = 0;
        System.out.println((System.nanoTime() - start) / 1000000);
    }

    private static boolean contains(List<String> list, String segment) {
        return list.stream().anyMatch(s -> {
            if (s.length() == segment.length()) {
                List<String> split = Arrays.asList(s.split(""));
                Optional<String> mismatch = Arrays.stream(segment.split("")).filter(c -> !split.contains(c)).findFirst();
                return mismatch.isEmpty();
            }
            return false;
        });
    }

    private static int index(List<String> list, String segment) {
        return list.stream().map(s -> {
            if (s.length() == segment.length()) {
                List<String> split = Arrays.asList(s.split(""));
                Optional<String> mismatch = Arrays.stream(segment.split("")).filter(c -> !split.contains(c)).findFirst();
                if (mismatch.isEmpty()) {
                    return list.indexOf(s);
                }
            }
            return -1;
        }).filter(i -> i >= 0).findAny().orElse(-1);
    }


}
