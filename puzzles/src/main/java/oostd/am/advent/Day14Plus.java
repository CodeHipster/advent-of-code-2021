package oostd.am.advent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day14Plus {

    static class Polymer{
        HashMap<Combination, Long> combinations = new HashMap<>();
        String lastElement;

        Polymer(String template){
            String[] elements = template.split("");
            lastElement = elements[elements.length-1];

            for (int i = 0; i < elements.length -1; i++) {
                Combination combination = new Combination(elements[i], elements[i + 1]);
                combinations.merge(combination, 1L, Long::sum);
            }
        }

        void apply(List<InsertionRule> rules, int times) {
            for (int i = 0; i < times; i++) {
                HashMap<Combination, Long> swap = new HashMap<>();

                combinations.forEach((combination, count) -> {
                    Optional<InsertionRule> match = rules.stream()
                            .filter(r -> r.combo.equals(combination)).findAny();
                    if(match.isPresent()){
                        InsertionRule rule = match.get();
                        rule.apply().forEach(c -> swap.merge(c, count, Long::sum));
                    }else{
                        swap.merge(combination, count, Long::sum);
                    }
                });
                combinations = swap;
            }
        }

    }

    static class Combination{
        String left;
        String right;

        public Combination(String left, String right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Combination that = (Combination) o;
            return left.equals(that.left) && right.equals(that.right);
        }

        @Override
        public int hashCode() {
            return Objects.hash(left, right);
        }
    }

    static class InsertionRule {
        Combination combo;
        String value;

        public InsertionRule(String input) {
            String[] split = input.split(" -> ");
            String[] split1 = split[0].split("");
            this.combo = new Combination(split1[0], split1[1]);
            this.value = split[1];
        }

        List<Combination> apply(){
            return List.of(new Combination(combo.left, value), new Combination(value, combo.right));
        }
    }

    public static void main(String[] args) {

        List<String> input = new FileReader().readInput("day14-input");

        Iterator<String> iterator = input.iterator();
        Polymer polymer = new Polymer(iterator.next());

        iterator.next(); //blank line

        List<InsertionRule> insertionRules = new ArrayList<>();
        while (iterator.hasNext()) {
            insertionRules.add(new InsertionRule(iterator.next()));
        }

        polymer.apply(insertionRules, 40);

        Map<String, Long> counts = new HashMap<>();
        for (Map.Entry<Combination, Long> entry: polymer.combinations.entrySet() ) {
            counts.merge(entry.getKey().left, entry.getValue(), Long::sum);
        }
        counts.merge(polymer.lastElement, 1L, Long::sum);

        var stats = counts.values().stream().mapToLong(l -> l).summaryStatistics();
        long answer = stats.getMax() - stats.getMin();
        System.out.println(answer);
        int debug = 0;
    }


}
