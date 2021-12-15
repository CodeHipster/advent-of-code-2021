package oostd.am.advent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Optional;
import oostd.am.advent.tools.FileReader;

public class Day14 {

    static class Node {
        Node next;
        Node previous;
        String value;

        Node(Node previous, String value) {
            this.previous = previous;
            this.value = value;
        }

        Node(Node previous, Node next, String value) {
            this.previous = previous;
            this.next = next;
            this.value = value;
        }

        public Node apply(List<InsertionRule> rules) {
            Node next = this.next;
            String left = value;
            String right = next.value;
            Optional<InsertionRule> match = rules.stream()
                    .filter(r -> r.left.equals(left) && r.right.equals(right)).findAny();
            if (match.isPresent()) {
                Node insert = new Node(this, next, match.get().value);
                this.next = insert;
                next.previous = insert;
            }
            return next;
        }
    }

    static class Polymer {
        Node head;

        Polymer(String template) {
            Node previous = null;
            for (String s : template.split("")) {
                Node current = new Node(previous, s);
                if (previous != null) {
                    previous.next = current;
                }
                previous = current;
            }
            // rewind
            while (previous.previous != null) {
                previous = previous.previous;
            }
            head = previous;
        }

        void apply(List<InsertionRule> rules, int times) {
            for (int i = 0; i < times; i++) {
                Node node = head;
                while (node.next != null) {
                    node = node.apply(rules);
                }
            }
        }
    }

    static class InsertionRule {
        String left;
        String right;
        String value;

        public InsertionRule(String input) {
            String[] split = input.split(" -> ");
            String[] split1 = split[0].split("");
            this.left = split1[0];
            this.right = split1[1];
            this.value = split[1];
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

        // value, count
        HashMap<String, Long> counts = new HashMap<>();
        Node node = polymer.head;
        while(node != null){
            counts.merge(node.value, 1L, Long::sum);
            node = node.next;
        }

        LongSummaryStatistics stats = counts.values().stream().mapToLong(l -> l).summaryStatistics();

        long answer = stats.getMax() - stats.getMin();
        System.out.println(answer);
        int debug = 0;
    }


}
