package oostd.am.advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Day1 {
    public static void main(String[] args) throws IOException {
        List<Integer> input = readInput("day1-input");

        int count = 0;
        Integer previous = input.get(0);
        for (int i = 1; i < input.size(); i++) {
            Integer value = input.get(i);
            if (value > previous) count++;
            previous = value;
        }
        System.out.println(count);
    }

    private static List<Integer> readInput(String fileName) throws IOException {
        InputStream is = Day1.class.getClassLoader().getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        ArrayList<Integer> input = new ArrayList<>();
        while (reader.ready()) {
            input.add(Integer.parseInt(reader.readLine()));
        }

        return input;
    }
}

