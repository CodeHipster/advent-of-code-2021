package oostd.am.advent.day25;

import oostd.am.advent.tools.Coordinate;

public class Cucumber {
    Coordinate position;
    Coordinate step;

    public Cucumber(Coordinate position, Coordinate step) {
        this.position = position;
        this.step = step;
    }

    boolean check(int[][] map){
        Coordinate p = position
                .add(step)
                .wrap(map[0].length, map.length);
        return map[p.y][p.x] == 0;
    }

    void move(int[][] map){
        Coordinate p = position
                .add(step)
                .wrap(map[0].length, map.length);

        map[p.y][p.x] = map[position.y][position.x];
        map[position.y][position.x] = 0;
        position = p;
    }
}
