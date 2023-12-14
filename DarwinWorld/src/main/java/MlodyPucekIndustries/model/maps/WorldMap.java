package MlodyPucekIndustries.model.maps;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.elements.WorldElement;
import MlodyPucekIndustries.model.utils.Vector2d;

import java.util.List;

public interface WorldMap extends MoveValidator {
    void placeAnimal(Animal animal);

    void placeDefaultGrass();

    void move(Animal animal, int tick);

    boolean isOccupied(Vector2d position);

    WorldElement objectAt(Vector2d position);

    List<WorldElement> getElements();

}

