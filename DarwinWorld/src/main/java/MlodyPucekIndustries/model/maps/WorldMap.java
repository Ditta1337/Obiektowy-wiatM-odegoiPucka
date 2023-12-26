package MlodyPucekIndustries.model.maps;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.elements.Grass;
import MlodyPucekIndustries.model.elements.WorldElement;
import MlodyPucekIndustries.model.utils.MultipleHashMap;
import MlodyPucekIndustries.model.utils.Vector2d;

import java.util.HashMap;
import java.util.List;

public interface WorldMap extends MoveValidator {
    void placeAnimal(Animal animal);

    void placeGrass(Grass grass);

    void placeDefaultGrasses();

    void placeDefaultAnimals();

    // only for MapVisualizer
    boolean isOccupied(Vector2d position);

    // only for MapVisualizer
    WorldElement objectAt(Vector2d position);

    List<WorldElement> getElements();

    MultipleHashMap getAnimals();

    HashMap<Vector2d, Grass> getGrasses();

    Vector2d getUpperRight();

    Vector2d getJungleLowerLeft();

    Vector2d getJungleUpperRight();

}

