package oostd.am.advent;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day3Plus {


    public static void main(String[] args) {

        List<String> input = new FileReader().readInput("day3-input");
        int total = input.size();

        // go over each bit
        // determine most common
        // remove values where bit does not match criteria
        List<String> oxygenRatingArray = input;
        int bit = 0;
        while (oxygenRatingArray.size() > 1) {
            char mostCommon = mostCommon(oxygenRatingArray, bit);
            oxygenRatingArray = filter(oxygenRatingArray, mostCommon, bit);
            bit++;
        }

        long oxygenRating = Long.parseLong(oxygenRatingArray.get(0),2);

        // go over each bit
        // determine most common
        // remove values where bit does not match criteria
        List<String> co2RatingArray = input;
        bit = 0;
        while (co2RatingArray.size() > 1) {
            char leastCommon = leastCommon(co2RatingArray, bit);
            co2RatingArray = filter(co2RatingArray, leastCommon, bit);
            bit++;
        }

        long co2Rating = Long.parseLong(co2RatingArray.get(0),2);

        long answer = oxygenRating * co2Rating;
        int debug = 1;
    }

    private static List<String> filter(List<String> oxygenRating, char filterChar, int bit) {
        return oxygenRating.stream().filter(s -> {
            char c = s.charAt(bit);
            return c == filterChar;
        }).collect(Collectors.toList());
    }

    private static char mostCommon(List<String> input, int bit) {
        AtomicInteger countOnes = new AtomicInteger();

        input.stream().forEach(binString -> {
            char[] bits = binString.toCharArray();
            if (bits[bit] == '1') {
                countOnes.getAndIncrement();
            }
        });
        return (countOnes.get() * 2 >= input.size()) ? '1' : '0';
    }

    private static char leastCommon(List<String> input, int bit) {
        AtomicInteger countOnes = new AtomicInteger();

        input.stream().forEach(binString -> {
            char[] bits = binString.toCharArray();
            if (bits[bit] == '1') {
                countOnes.getAndIncrement();
            }
        });
        return (countOnes.get() * 2 < input.size()) ? '1' : '0';
    }
}
