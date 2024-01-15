package MlodyPucekIndustries.model.maps;

import MlodyPucekIndustries.model.elements.Grass;
import MlodyPucekIndustries.model.elements.WorldElement;
import MlodyPucekIndustries.model.utils.AnimalTree;
import MlodyPucekIndustries.model.utils.MultipleHashMap;
import MlodyPucekIndustries.model.utils.Vector2D;

import java.util.HashMap;

public interface WorldMap extends MoveValidator {
    MultipleHashMap getAnimals();

    HashMap<Vector2D, Grass> getGrasses();

    int getWidth();

    int getHeight();

    Vector2D getJungleLowerLeft();

    Vector2D getJungleUpperRight();

    AnimalTree getAnimalTree();

    void placeGrass(Grass grass);

    void modifyTideState();

    boolean isOccupied(Vector2D position);

    WorldElement objectAt(Vector2D position);

    void initiate();
}

