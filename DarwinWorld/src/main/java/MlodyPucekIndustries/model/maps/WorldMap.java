package MlodyPucekIndustries.model.maps;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.elements.WorldElement;
import MlodyPucekIndustries.model.utils.Vector2d;

import java.util.List;

public interface WorldMap extends MoveValidator {
    void placeAnimal(Animal animal);

    void placeDefaultGrasses();

    void placeDefaultAnimals();

    // only for MapVisualizer
    boolean isOccupied(Vector2d position);

    // only for MapVisualizer
    WorldElement objectAt(Vector2d position);

    void move(Animal animal);

    List<WorldElement> getElements();

}

