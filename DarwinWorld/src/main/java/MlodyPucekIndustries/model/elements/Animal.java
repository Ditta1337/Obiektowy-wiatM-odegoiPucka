package MlodyPucekIndustries.model.elements;

import java.util.Objects;
import MlodyPucekIndustries.model.utils.*;
import MlodyPucekIndustries.model.maps.MoveValidator;

public class Animal implements WorldElement {
    private MapDirection direction;
    private Vector2d position;
    private int[] genome;
    private final short baseTick;
    private final long birthTick;
    private int energy;

    public Animal(int tick, int energy, int[] genome, Vector2d position) {
        this.baseTick = (short) (Math.random() * genome.length);
        this.birthTick = tick;
        this.energy = energy;
        this.genome = genome;
        this.position = position;
        direction = MapDirection.N.spin((short) (Math.random() * 8));
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
        return direction.toString();
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(int tick, MoveValidator validator) {
        direction = direction.spin(genome[(tick + baseTick) % genome.length]);
        Vector2d newPosition = position.add(direction.toUnitVector());
        if (validator.canMoveTo(newPosition)) {
            position = validator.validPosition(newPosition);
        }
    }
}

