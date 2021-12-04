package oostd.am.advent.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import oostd.am.advent.Day1;

public class FileReader {
    public List<String> readInput(String fileName) {
        InputStream is = Day1.class.getClassLoader().getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        ArrayList<String> input = new ArrayList<>();
        try {
            while (reader.ready()) {
                input.add(reader.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failing to read input.");
        }

        return input;
    }
}
