package MlodyPucekIndustries.model.elements;

import java.util.Arrays;
import java.util.Objects;
import MlodyPucekIndustries.model.utils.*;
import MlodyPucekIndustries.model.maps.MoveValidator;

public class Animal implements WorldElement {
    private MapDirection direction;
    private Vector2D position;
    private int[] genome;
    private final short baseTick;
    private final long birthTick;
    private int energy;
    private int children = 0;
    private int eatenGrass = 0;
    private long age = 0;


    public Animal(long tick, int energy, int[] genome, Vector2D position) {
        this.baseTick = (short) (Math.random() * genome.length);
        this.birthTick = tick;
        this.energy = energy;
        this.genome = genome;
        this.position = position;

        // changed
        direction = MapDirection.values()[genome[baseTick]];
        //direction = MapDirection.NE;
    }

    public void modifyEnergy(int value){
        energy += value;
    }

    public int getEnergy() {
        return energy;
    }

    public long getBirthTick() {
        return birthTick;
    }

    public int getChildren() {
        return children;
    }

    public void addChild() {
        children++;
    }

    public void addEatenGrass() {
        eatenGrass++;
    }

    public int getEatenGrass() {
        return eatenGrass;
    }

    public long getAge() {
        return age;
    }

    public int getActiveGenome() {
        return genome[((short)(age % genome.length) + baseTick) % genome.length];
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setDirection(MapDirection direction) {
        this.direction = direction;
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public Vector2D getPosition() {
        return this.position;
    }

    public int[] getGenome() {
        return genome;
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
        return Objects.hash(Arrays.hashCode(genome), baseTick, birthTick);
    }

    @Override
    public String toString() {
        return direction.toString();
    }

    public boolean isAt(Vector2D position) {
        return this.position.equals(position);
    }

    // TODO: potencjalnie tick do wywalenia z tej metody, przez dodanie "age"
    public void move(long tick, MoveValidator validator) {
        age++;
        Vector2D newPosition = position.add(direction.toUnitVector());
        if (validator.canMoveTo(newPosition)) {
            energy--;
            position = validator.validPosition(newPosition);
        }
        direction = direction.spin(genome[((short)(tick % genome.length) + baseTick) % genome.length]);
    }
}

