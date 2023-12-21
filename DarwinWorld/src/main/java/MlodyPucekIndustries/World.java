package MlodyPucekIndustries;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.maps.*;
import MlodyPucekIndustries.model.utils.*;

public class World {
    public static void main(String[] args) {
        RegularMap map = new RegularMap(5, 5, 3, 3, 2, 10, 10, 8);
        MapVisualizer visualizer = new MapVisualizer(map);
        //System.out.println(visualizer.draw(new Vector2d(0, 0), new Vector2d(4, 4)));
        RegularMapManager manager = new RegularMapManager(5, 20, false, map);
        for (int i = 0; i < 100; i++) {
            System.out.println(visualizer.draw(new Vector2d(0, 0), new Vector2d(4, 4)));
            manager.tickAnimalMove();
            manager.tickEnF();
            manager.tickSpawnGrass();
            manager.increaseTick();
        }
    }
}

