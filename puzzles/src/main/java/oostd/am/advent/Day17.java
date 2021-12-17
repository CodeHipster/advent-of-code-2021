package oostd.am.advent;

import java.util.List;
import oostd.am.advent.tools.FileReader;

public class Day17 {

    static class Position{
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
        void add(int x, int y){
            this.x += x;
            this.y += y;
        }
    }

    static class Target{
        Position lt;
        Position rb;

        public Target(Position lt, Position rb) {
            this.lt = lt;
            this.rb = rb;
        }
    }

    static class Sensor{
        int xVelocity;
        int yVelocity;
        Position position;
        int maxHeight;

        public Sensor(int xVelocity, int yVelocity, Position position) {
            this.xVelocity = xVelocity;
            this.yVelocity = yVelocity;
            this.position = position;
            this.maxHeight = position.y;
        }
        void step(){
            position.add(xVelocity, yVelocity);
            // drag
            if(xVelocity > 0) xVelocity--;
            else if(xVelocity < 0 ) xVelocity++;
            // gravity
            yVelocity--;
            if(position.y > maxHeight) maxHeight = position.y;
        }

        boolean hit(Target target){
            return (position.x >= target.lt.x && position.x <= target.rb.x
            && position.y <= target.lt.y && position.y >= target.rb.y );
        }
        // won't ever hit target
        boolean missed(Target target){
            // x mis
            boolean xMis = (xVelocity > 0 && position.x > target.rb.x);
            xMis |= (xVelocity < 0 && position.x < target.lt.x);
            xMis |= (xVelocity == 0 && (position.x < target.lt.x || position.x > target.rb.x));

            boolean yMis = yVelocity < 0 && position.y < target.rb.y;
            return xMis || yMis;
        }
    }

    public static void main(String[] args) {
        List<String> input = new FileReader().readInput("day17-input");
        Target testTarget = new Target(new Position(20, -5), new Position(30, -10));

        // target area: x=240..292, y=-90..-57
        Target target = new Target(new Position(240, -57), new Position(292, -90));

        int xVel = 0;
        boolean xHit = false;
        while(!xHit){
            int temp = xVel;
            int dist = 0;
            while(temp > 0){
                dist += temp;
                temp--;
            }
            if(dist >= 240 && dist <= 292){
                xHit = true;
            }else{
                xVel++;
            }
        }

        Sensor sensor = new Sensor(xVel, 89, new Position(0,0));
        boolean hit = false;
        boolean missed = false;
        while(!hit && !missed){
            sensor.step();
            hit = sensor.hit(target);
            missed = sensor.missed(target);
        }



//        int highestHit = -1;
//        for (int yVel = 0; yVel < 1000; yVel++) {
//            Sensor sensor = new Sensor(6, yVel, new Position(0, 0));
//            boolean hit = false;
//            boolean missed = false;
//            while(!hit || missed){
//                sensor.step();
//                hit = sensor.hit(testTarget);
//                missed = sensor.missed(testTarget);
//            }
//
//        }

        // - 89

        int debug = 0;
    }


}
