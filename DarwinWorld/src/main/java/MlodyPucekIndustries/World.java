package MlodyPucekIndustries;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.maps.*;
import MlodyPucekIndustries.model.utils.*;

public class World {
    public static void main(String[] args) {
        RegularMap map = new RegularMap(
                3,
                3,
                2,
                2,
                1,
                0,
                10,
                8);

        MapVisualizer visualizer = new MapVisualizer(map);

        int [] genome = {0, 0, 0};

        Animal ziomek2 = new Animal(0, 3, genome, new Vector2d(2, 0));
        map.placeAnimal(ziomek2);
        System.out.println(visualizer.draw(new Vector2d(0, 0), new Vector2d(2, 2)));

        for (int i = 0; i < 10; i++) {
            map.move(ziomek2);
            System.out.println(visualizer.draw(new Vector2d(0, 0), new Vector2d(2, 2)));
        }

    }
}

