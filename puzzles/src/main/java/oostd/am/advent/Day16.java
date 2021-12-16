package oostd.am.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oostd.am.advent.tools.FileReader;

public class Day16 {

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

    enum PacketType{
        operator, value
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
            int typeInt = Integer.parseInt(bits.substring(3,6),2);
            if(typeInt != 4){
                this.type = PacketType.operator;
                this.lengthTypeId = Integer.parseInt(bits.substring(6,7),2);
                if(lengthTypeId == 0){
                    this.nrOfBitsSubPackages = Integer.parseInt(bits.substring(7,22),2);
                    this.subPackageStart = 22;
                }else{
                    this.nrOfSubPackages = Integer.parseInt(bits.substring(7,18),2);
                    this.subPackageStart = 18;
                }
            }else{
                this.type = PacketType.value;
            }
        }
    }

    static class Packet {
        PacketInfo packetInfo;
        String bits;
        List<Packet> subPackets;

        public Packet(PacketInfo packetInfo, String bits, List<Packet> subPackets) {
            this.packetInfo = packetInfo;
            this.bits = bits;
            this.subPackets = subPackets;
        }
    }

    static boolean containsOnlyZero(String bits){
        return Arrays.stream(bits.split("")).allMatch(c -> c.equals("0"));
    }

    static Packet getPacket(String bits){
        PacketInfo packetInfo = new PacketInfo(bits);

        if(packetInfo.type == PacketType.operator){
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
            return new Packet(packetInfo, bits.substring(0, position), subPackets);
        }else {
            // value type.
            //grab 5 bits, until we get bits that start with a 0;
            int position = 6;
            boolean stop = false;
            while(!stop){
                int end = position + 5;
                String digitBits = bits.substring(position, position +5);
                stop = digitBits.charAt(0) == '0';
                position = end;
            }
            return new Packet(packetInfo, bits.substring(0, position),new ArrayList<>());
        }
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day16-input");

        String bits = bitsFromHex(input.get(0));

        //parse string
        Packet packet = getPacket(bits);

        List<Integer> versions = new ArrayList<>();
        ArrayList<Packet> packets = new ArrayList<>();
        packets.add(packet);
        while(!packets.isEmpty()){
            Packet remove = packets.remove(0);
            versions.add(remove.packetInfo.version);
            packets.addAll(remove.subPackets);
        }

        int answer = versions.stream().mapToInt(i -> i).sum();
        int debug = 0;
    }


}
