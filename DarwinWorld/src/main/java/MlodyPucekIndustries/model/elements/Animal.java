package MlodyPucekIndustries.model.elements;

import java.util.Objects;
import MlodyPucekIndustries.model.utils.*;
import MlodyPucekIndustries.model.maps.MoveValidator;

public class Animal implements WorldElement {
    private MapDirection direction;
    private Vector2d position;
    private int tick;
    private int energy;

    public Animal(int tick, int energy) {
        this.tick = tick;
        this.energy = energy;
        direction = MapDirection.N;
        position = new Vector2d(2,2);
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public void setDirection(MapDirection direction) {
        this.direction = direction;
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Animal)) {
            return false;
        }
        Animal animal = (Animal) other;
        return this.getPosition() == animal.getPosition() && this.getDirection() == animal.getDirection();
    }

    @Override
    public int hashCode() {
        return Objects.hash(direction, position);
    }

    @Override
    public String toString() {
        return direction.toString() + " " + position.toString();
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(int gene, MoveValidator validator) {
        direction = direction.spin(gene);
        if (validator.canMoveTo(position.add(direction.toUnitVector()))) {
            // TODO: remove .add and replace it with method form move validator that translates new position
            // that is next to old or on the other side of the map
            position = position.add(direction.toUnitVector());
        }
        Vector2d newPosition = position.add(direction.toUnitVector());
    }
}

