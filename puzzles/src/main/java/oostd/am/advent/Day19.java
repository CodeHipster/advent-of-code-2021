package oostd.am.advent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import oostd.am.advent.tools.FileReader;

public class Day19 {

    static class Translation {
        int x, y, z;

        public Translation(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Translation that = (Translation) o;
            return x == that.x && y == that.y && z == that.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

    static class Rotations {
        Beacon rotateOverX90(Beacon beacon) {
            int x = beacon.x, y = beacon.y, z = beacon.z;
            beacon.x = x;
            beacon.y = -z;
            beacon.z = y;
            return beacon;
        }

        Beacon flipOverZ(Beacon beacon) {
            int x = beacon.x, y = beacon.y, z = beacon.z;
            beacon.x = -x;
            beacon.y = -y;
            beacon.z = z;
            return beacon;
        }

        Beacon rotateZToX(Beacon beacon) {
            int x = beacon.x, y = beacon.y, z = beacon.z;
            beacon.x = z;
            beacon.y = y;
            beacon.z = -x;
            return beacon;
        }

        Beacon rotateYToX(Beacon beacon) {
            int x = beacon.x, y = beacon.y, z = beacon.z;
            beacon.x = y;
            beacon.y = -x;
            beacon.z = z;
            return beacon;
        }
    }

    static class Scanner {
        int id;
        List<Beacon> beacons;
        Rotations r = new Rotations();

        public Scanner(int id, List<Beacon> beacons) {
            this.id = id;
            this.beacons = beacons;
        }

        // returns aligned scanner or null if no overlap is found.
        Scanner align(Scanner other) {
            System.out.println("Checking alignment between: " + this.id + " & scanner: " + other.id);

            List<Beacon> copy = other.beacons.stream().map(Beacon::copy).collect(Collectors.toList());
            for (int axis = 0; axis < 3; axis++) {
                List<Beacon> axisCopy;
                switch (axis) {
                    case 0: // x, no rotating to x-axis required
                        axisCopy = copy.stream().map(Beacon::copy).collect(Collectors.toList());
                        break;
                    case 1: // y
                        axisCopy = copy.stream().map(Beacon::copy).map(r::rotateYToX).collect(Collectors.toList());
                        break;
                    case 2: // z
                        axisCopy = copy.stream().map(Beacon::copy).map(r::rotateZToX).collect(Collectors.toList());
                        break;
                    default:
                        throw new RuntimeException();
                }
                List<Beacon> rotateCopy = axisCopy.stream().map(Beacon::copy).collect(Collectors.toList());
                for (int i = 0; i < 8; i++) {
                    if (!(i == 0 || i == 4)) { // at 0 and 4 we do not rotate.
                        rotateCopy = rotateCopy.stream().map(r::rotateOverX90).collect(Collectors.toList());
                    }
                    if (i == 4) { // flip copy of axis, so we can rotate again;
                        rotateCopy = axisCopy.stream().map(Beacon::copy).map(r::flipOverZ).collect(Collectors.toList());
                    }
                    Translation align = aligns(rotateCopy);
                    if (align != null) {
                        other.beacons = rotateCopy.stream().map(b -> b.translate(align)).collect(Collectors.toList());
                        return other;
                    }
                }
            }
            return null;
        }

        private Translation aligns(List<Beacon> other) {
            // create a list of translations per beacon
            List<List<Translation>> translations = beacons.stream().map(b -> other.stream()
                            .map(b::subtract).collect(Collectors.toList()))
                    .collect(Collectors.toList());

            // create a list of counts how often a translation occurs in translations for other beacons.
            for (int i = 0; i < translations.size() - 11; i++) {
                List<Translation> tl = translations.get(i); // translations for beacon
                for (int j = 0; j < tl.size(); j++) {
                    Translation t = tl.get(j);
                    int count = 1;
                    // count how often translation occurs in other sets
                    for (int k = i + 1; k < translations.size(); k++)
                        if (translations.get(k).contains(t))
                            count++;
                    if (count >= 12)
                        return t;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return "Scanner: " + id
                    + "\n"
                    + beacons.stream().map(Object::toString).collect(Collectors.joining("\n"));
        }
    }

    static class Beacon {
        int x;
        int y;
        int z;

        public Beacon(String input) {
            String[] split = input.split(",");
            this.x = Integer.parseInt(split[0]);
            this.y = Integer.parseInt(split[1]);
            this.z = Integer.parseInt(split[2]);
        }

        public Beacon(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        Translation subtract(Beacon other) {
            return new Translation(x - other.x, y - other.y, z - other.z);
        }

        Beacon copy() {
            return new Beacon(x, y, z);
        }

        Beacon translate(Translation translation) {
            this.x += translation.x;
            this.y += translation.y;
            this.z += translation.z;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Beacon beacon = (Beacon) o;
            return x == beacon.x && y == beacon.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return Stream.of(x, y, z).map(Object::toString).collect(Collectors.joining(","));
        }
    }

    static class Map {
        Set<Beacon> completeMap = new HashSet<>();
        List<Scanner> scanners = new ArrayList<>();

        Map(Scanner initial) {
            scanners.add(initial);
            completeMap.addAll(initial.beacons);
        }

        // return true if overlap was found.
        boolean add(Scanner other) {
            System.out.println("Trying to add scanner: " + other.id);
            // find overlap with any existing scanners.
            Optional<Scanner> alignedScanner = scanners.stream().map(s -> s.align(other)).filter(Objects::nonNull).findFirst();
            if (alignedScanner.isPresent()) {
                Scanner aligned = alignedScanner.get();
                scanners.add(aligned);
                completeMap.addAll(aligned.beacons);
                System.out.println("Succesfully aligned and added scanner: " + other.id);
                return true;
            }
            System.out.println("Could not align scanner: " + other.id);
            return false;
        }
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day19-input");
        Iterator<String> itr = input.iterator();

        List<Scanner> scanners = new ArrayList<>();
        while (itr.hasNext()) {
            String line = itr.next();
            if (line.startsWith("---")) {
                int scannerId = Integer.parseInt(line.substring(12, 14).trim());
                List<Beacon> beacons = new ArrayList<>();
                while (itr.hasNext()) {
                    line = itr.next();
                    if (line.isBlank()) break;
                    beacons.add(new Beacon(line));
                }
                scanners.add(new Scanner(scannerId, beacons));
            }
        }

        Map map = new Map(scanners.remove(0));

        while (!scanners.isEmpty()) {
            Scanner scanner = scanners.remove(0);
            boolean added = map.add(scanner);
            if (!added) { // add to back of the list.
                scanners.add(scanner);
            }
        }


        //TODO: output incorrect, 127 instead of 79
        int debug = 0;
    }


}
