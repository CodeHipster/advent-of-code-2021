package oostd.am.advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Day1Plus {
    public static void main(String[] args) throws IOException {
        List<Integer> input = readInput("day1-input");

        int count = 0;
        int previous = input.get(0) + input.get(1) + input.get(2);
        for (int i = 1; i < (input.size() - 2); i++) {
            int value = input.get(i) + input.get(i + 1) + input.get(i + 2);
            if (value > previous) count++;
            previous = value;
        }
        System.out.println(count);
    }

    private static List<Integer> readInput(String fileName) throws IOException {
        InputStream is = Day1Plus.class.getClassLoader().getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        ArrayList<Integer> input = new ArrayList<>();
        while (reader.ready()) {
            input.add(Integer.parseInt(reader.readLine()));
        }

        return input;
    }
}

