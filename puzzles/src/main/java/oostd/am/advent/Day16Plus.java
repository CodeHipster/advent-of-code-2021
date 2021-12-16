package oostd.am.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import oostd.am.advent.tools.FileReader;

public class Day16Plus {

    static String bitsFromHex(String hex){
        String[] chars = hex.split("");
        StringBuilder sb = new StringBuilder();
        for (String c : chars) {
            switch (c) {
                case "0":
                    sb.append("0000");
                    continue;
                case "1":
                    sb.append("0001");
                    continue;
                case "2":
                    sb.append("0010");
                    continue;
                case "3":
                    sb.append("0011");
                    continue;
                case "4":
                    sb.append("0100");
                    continue;
                case "5":
                    sb.append("0101");
                    continue;
                case "6":
                    sb.append("0110");
                    continue;
                case "7":
                    sb.append("0111");
                    continue;
                case "8":
                    sb.append("1000");
                    continue;
                case "9":
                    sb.append("1001");
                    continue;
                case "A":
                    sb.append("1010");
                    continue;
                case "B":
                    sb.append("1011");
                    continue;
                case "C":
                    sb.append("1100");
                    continue;
                case "D":
                    sb.append("1101");
                    continue;
                case "E":
                    sb.append("1110");
                    continue;
                case "F":
                    sb.append("1111");
                    continue;
            }
        }
        return sb.toString();
    }

    static PacketType parse(int value){
        switch(value){
            case 0:
                return PacketType.sum;
            case 1:
                return PacketType.product;
            case 2:
                return PacketType.minimum;
            case 3:
                return PacketType.maximum;
            case 4:
                return PacketType.value;
            case 5:
                return PacketType.greater_then;
            case 6:
                return PacketType.less_then;
            case 7:
                return PacketType.equal_to;
            default:throw new RuntimeException();
        }
    }

    enum PacketType{
        sum,
        product,
        minimum,
        maximum,
        greater_then,
        less_then,
        equal_to,
        value
    }

    static class PacketInfo{
        Integer nrOfSubPackages;
        Integer subPackageStart;
        Integer nrOfBitsSubPackages;
        Integer lengthTypeId;
        int version;
        PacketType type;

        public PacketInfo(String bits) {
            this.version = Integer.parseInt(bits.substring(0,3),2);
            this.type = parse(Integer.parseInt(bits.substring(3,6),2));

            if(this.type != PacketType.value){
                this.lengthTypeId = Integer.parseInt(bits.substring(6,7),2);
                if(lengthTypeId == 0){
                    this.nrOfBitsSubPackages = Integer.parseInt(bits.substring(7,22),2);
                    this.subPackageStart = 22;
                }else{
                    this.nrOfSubPackages = Integer.parseInt(bits.substring(7,18),2);
                    this.subPackageStart = 18;
                }
            }
        }
    }

    static class Packet {
        PacketInfo packetInfo;
        String bits;
        List<Packet> subPackets;
        long value;

        public Packet(PacketInfo packetInfo, String bits, List<Packet> subPackets, long value) {
            this.packetInfo = packetInfo;
            this.bits = bits;
            this.subPackets = subPackets;
            this.value = value;
        }
    }

    static boolean containsOnlyZero(String bits){
        return Arrays.stream(bits.split("")).allMatch(c -> c.equals("0"));
    }

    static long resolveValue(List<Packet> packets, PacketType type){
        switch(type){
            case sum:
                return packets.stream().mapToLong(p -> p.value).sum();
            case product:
                return packets.stream().mapToLong(p -> p.value).reduce(1, (a, b) -> a * b);
            case maximum:
                return packets.stream().max(Comparator.comparingLong(p -> p.value)).get().value;
            case minimum:
                return packets.stream().min(Comparator.comparingLong(p -> p.value)).get().value;
            case greater_then:
                return (packets.get(0).value > packets.get(1).value)? 1: 0;
            case less_then:
                return (packets.get(0).value < packets.get(1).value)? 1: 0;
            case equal_to:
                return (packets.get(0).value == packets.get(1).value)? 1: 0;
            default: throw new RuntimeException();
        }
    }

    static Packet getPacket(String bits){
        PacketInfo packetInfo = new PacketInfo(bits);

        if(packetInfo.type != PacketType.value){
            // operator type
            List<Packet> subPackets = new ArrayList<>();
            int position = packetInfo.subPackageStart;
            if(packetInfo.nrOfSubPackages != null){ // iterate by nr of packages
                while(subPackets.size() < packetInfo.nrOfSubPackages){
                    Packet packet = getPacket(bits.substring(position));
                    position += packet.bits.length();
                    subPackets.add(packet);
                }
            }else{ //iterate by length
                int endPosition = packetInfo.subPackageStart + packetInfo.nrOfBitsSubPackages;
                while(position < endPosition) {
                    String substring = bits.substring(position, endPosition);
                    if(containsOnlyZero(substring)) { // exclude the zero's filled at the end.
                        position = endPosition;
                        continue;
                    }
                    Packet packet = getPacket(substring);
                    position += packet.bits.length();
                    subPackets.add(packet);
                }
            }
            long value = resolveValue(subPackets, packetInfo.type);
            return new Packet(packetInfo, bits.substring(0, position), subPackets, value);
        }else {
            // value type.
            // grab 5 bits, until we get bits that start with a 0;
            int position = 6;
            boolean stop = false;
            StringBuilder digit = new StringBuilder();
            while(!stop){
                int end = position + 5;
                String digitBits = bits.substring(position, position +5);
                stop = digitBits.charAt(0) == '0';
                digit.append(digitBits, 1, 5);
                position = end;
            }
            return new Packet(packetInfo, bits.substring(0, position),new ArrayList<>(), Long.parseLong(digit.toString(), 2));
        }
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day16-input");

        for (int i = 0; i < 1; i++) {
            String bits = bitsFromHex(input.get(i));
            Packet packet = getPacket(bits);
            System.out.println(packet.value);
        }

        int debug = 0;
    }


}
