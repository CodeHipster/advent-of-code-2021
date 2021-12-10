package oostd.am.advent.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//TODO: we should be able to do this with a function for each digit?
public class UniqueCombinationGenerator implements Supplier<int[]> {

    private int[] set;
    private int[] finalState;

    private int[] array;
    private int incrementPos;

    public UniqueCombinationGenerator(int sequenceLength){
        List<Integer> range = IntStream.rangeClosed(1, sequenceLength).boxed().collect(Collectors.toList());
        array = range.stream().mapToInt(i -> i).toArray();
        Collections.reverse(range);
        set = finalState = range.stream().mapToInt(i -> i).toArray();
        incrementPos = array.length -1;
    }

    @Override
    public int[] get() {
        if(!Arrays.equals(array,finalState)){
            List<Integer> available = new ArrayList<>();
            for(int i = incrementPos; i < array.length; i++){
                available.add(array[i]);
            }
            if(array[incrementPos] == available.stream().max(Integer::compareTo).get()){
                while(array[incrementPos -1] > array[incrementPos]){
                    incrementPos--;
                }
                incrementNext(array, incrementPos);
                fillUp(array, incrementPos, set);
                incrementPos = array.length -1;
            }else{
                array[incrementPos] += 1;
                fillUp(array, incrementPos, set);
            }
            return Arrays.copyOf(array, array.length);
        }
        return null;
    }

    private static void incrementNext(int[] array, int incrementPos) {
        List<Integer> available = new ArrayList<>();
        for(int i = incrementPos; i < array.length; i++){
            available.add(array[i]);
        }
        array[incrementPos -1] = available.stream().sorted().filter(a -> a > array[incrementPos -1]).findFirst().get();
    }

    private static void fillUp(int[] array, int incrementPos, int[] set) {
        List<Integer> used = new ArrayList<>();
        for(int i = 0; i < incrementPos; i++){
            used.add(array[i]);
        }
        int[] available = Arrays.stream(set).filter(d -> !used.contains(d)).toArray();
        for(int i = available.length -1; i >= 0; --i){
            array[array.length -1 -i] = available[i];
        }
    }
}
