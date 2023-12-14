package MlodyPucekIndustries.model.maps;

package agh.ics.oop.model;


import MlodyPucekIndustries.model.utils.Vector2d;
import MlodyPucekIndustries.model.elements.WorldElement;
import java.util.List;


public interface WorldMap extends MoveValidator {

    void place(WorldElement object) throws Exception;

    void move(Animal object, MoveDirection direction);

    boolean isOccupied(Vector2d position);

    WorldElement objectAt(Vector2d position);

    List<WorldElement> getElements();

    String toString();

}