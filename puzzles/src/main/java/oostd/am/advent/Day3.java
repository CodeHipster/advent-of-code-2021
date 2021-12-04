package oostd.am.advent;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day3 {


    public static void main(String[] args) {

        List<String> input = new FileReader().readInput("day3-input");
        int total = input.size();

        AtomicInteger[] gammaRateArray = new AtomicInteger[12];
        Arrays.setAll(gammaRateArray, (a) -> new AtomicInteger());

        input.stream().forEach(binString -> {
            char[] bits = binString.toCharArray();
            for(int i = 0; i < bits.length; i++){
                if(bits[i] == '1'){
                    gammaRateArray[i].addAndGet(1);
                }
            }
        });

        String gammaRateString = Arrays.stream(gammaRateArray).map(s -> s.get() > 500? "1": "0").collect(Collectors.joining());
        long gammaRateLong = Long.parseLong(gammaRateString,2);
        BitSet gammaRate = BitSet.valueOf(new long[]{gammaRateLong});
        BitSet epsilonRate = BitSet.valueOf(new long[]{gammaRateLong});
        epsilonRate.flip(0, gammaRateString.length());

        long answer = gammaRate.toLongArray()[0] * epsilonRate.toLongArray()[0];
        int debug = 1;
    }
}
