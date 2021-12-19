package oostd.am.advent;

import java.util.List;
import java.util.stream.Collectors;
import oostd.am.advent.tools.FileReader;

public class Day18 {

    static abstract class Element {
        Pair parent;

        abstract long magnitude();
    }

    static class Pair extends Element {
        Element left;
        Element right;

        public Pair(Element left, Element right) {
            this.left = left;
            this.right = right;
        }

        Pair add(Pair other) {
            Pair result = new Pair(this, other);
            // Set parent;
            this.parent = result;
            other.parent = result;
            result.reduce();
            return result;
        }

        long magnitude() {
            return 3 * left.magnitude() + 2 * right.magnitude();
        }

        protected void replace(Element existing, Element replacement) {
            replacement.parent = this;
            if (left.equals(existing)) {
                left = replacement;
            } else {
                right = replacement;
            }
        }

        private void reduce() {
            // find element 4 levels deep
            boolean exit = false;
            while (!exit) {
                // explode
                Pair nested = this.findNested(0, 4);
                if (nested != null) {
                    //explode pair
                    nested.explode();
                    continue; // first process all explodes.
                }
                Number split = this.findSplit();
                if (split != null) {
                    split.split();
                    continue; // split and check for explodes again.
                }
                exit = true; // no explosions or splits
            }
        }

        private Number findSplit() {
            // go from left to right to find numbers > 9
            // check left
            if (left instanceof Number) {
                if (((Number) left).value > 9) return (Number) left;
            } else {
                Number split = ((Pair) left).findSplit();
                if (split != null) return split;
            }
            // check right
            if (right instanceof Number) {
                if (((Number) right).value > 9) return (Number) right;
            } else {
                Number split = ((Pair) right).findSplit();
                if (split != null) return split;
            }
            return null;
        }

        private Number findNextLeft(Element previous) {
            if (previous.equals(right)) {
                if (left instanceof Number) return (Number) left;
                return ((Pair) left).findNextLeft(this);
            } else if (previous.equals(left)) {
                if (parent == null) return null;
                return parent.findNextLeft(this);
            } else { // new child
                if (right instanceof Number) return (Number) right;
                else return ((Pair) right).findNextLeft(this);
            }
        }

        private Number findNextRight(Element previous) {
            if (previous.equals(left)) {
                if (right instanceof Number) return (Number) right;
                return ((Pair) right).findNextRight(this);
            } else if (previous.equals(right)) {
                if (parent == null) return null;
                return parent.findNextRight(this);
            } else {
                if (left instanceof Number) return (Number) left;
                else return ((Pair) left).findNextRight(this);
            }
        }

        private void explode() {
            // this left && right must be numbers.
            if (!(left instanceof Number) || !(right instanceof Number)) {
                throw new RuntimeException("expected left and right to be numbers");
            }

            //find next left nr
            Number nextLeft = findNextLeft(left);
            // add this left.
            if (nextLeft != null)
                nextLeft.value += ((Number) left).value;

            // find next right nr
            Number nextRight = findNextRight(right);
            // add this right
            if (nextRight != null)
                nextRight.value += ((Number) right).value;

            parent.replace(this, new Number(0));
        }

        private Pair findNested(int level, int findLevel) {
            if (level == findLevel) return this;
            else {
                if (left instanceof Pair) {
                    Pair nested = ((Pair) left).findNested(level + 1, findLevel);
                    if (nested != null) return nested;
                }
                if (right instanceof Pair) {
                    Pair nested = ((Pair) right).findNested(level + 1, findLevel);
                    if (nested != null) return nested;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return "[" + left + "," + right + "]";
        }
    }

    static class Number extends Element {
        int value;

        public Number(int value) {
            this.value = value;
        }

        public void split() {
            // split number
            int left = value / 2;
            int right = value - left;

            Number leftNr = new Number(left);
            Number rightNr = new Number(right);
            Pair replacement = new Pair(leftNr, rightNr);
            leftNr.parent = replacement;
            rightNr.parent = replacement;
            parent.replace(this, replacement);
        }

        long magnitude(){
            return value;
        }

        @Override
        public String toString() {
            return "" + value;
        }
    }

    static int closingBracket(String input) {
        int level = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ']') {
                level--;
                if (level == 0) {
                    return i;
                }
            } else if (input.charAt(i) == '[') {
                level++;
            }
        }
        throw new RuntimeException("could not find closing bracket.");
    }

    static Element parse(String input) {
        Element left = null;
        Element right = null;
        int commaIndex;
        if (input.charAt(1) == '[') {
            // if nr 2 is [ it is left as pair.
            // find closing bracket and call this method.
            commaIndex = closingBracket(input.substring(1)) + 2;// +1 for starting at 1 and + 1 for the comma after ]
            left = parse(input.substring(1, commaIndex));
        } else {
            // if nr 2 is a nr, it is left as number.
            commaIndex = 2;
            left = new Number(Integer.parseInt(input.substring(1, commaIndex)));
        }

        // skip comma,
        int start = ++commaIndex;
        if (input.charAt(start) == '[') {
            // if nr 2 is [ it is left as pair.
            // find closing bracket and call this method.
            int end = closingBracket(input.substring(start)) + 1;
            right = parse(input.substring(start, end + start));
        } else {
            // if nr 2 is a nr, it is left as number.
            right = new Number(Integer.parseInt(input.substring(start, ++start)));
        }
        Pair result = new Pair(left, right);
        left.parent = result;
        right.parent = result;
        return result;
    }

    //15:44
    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day18-input");

        List<Element> pairs = input.stream().map(s -> parse(s)).collect(Collectors.toList());

        Pair pair = (Pair) pairs.get(0);
        for (int i = 1; i < pairs.size(); i++) {
            pair = pair.add(((Pair) pairs.get(i)));
        }

        long answer = pair.magnitude();

        System.out.println(answer);

        int debug = 0;
    }


}
