package MlodyPucekIndustries;

import MlodyPucekIndustries.model.maps.*;
import MlodyPucekIndustries.model.utils.*;

public class World {
    public static void main(String[] args) {
        RegularMap map = new RegularMap(10, 10, 2, 2, 10, 10, 10, 16);
        MapVisualizer visualizer = new MapVisualizer(map);
        RegularMapManager manager = new RegularMapManager(5, 20, false, map);
        for (int i = 0; i < 100; i++) {
            System.out.println(visualizer.draw(new Vector2d(0, 0), new Vector2d(9, 9)));
            manager.tickAnimalMove();
            manager.tickEnF();
            manager.tickSpawnGrass();
            manager.increaseTick();
        }
    }
}
