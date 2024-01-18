package MlodyPucekIndustries.model.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import MlodyPucekIndustries.model.utils.*;
import MlodyPucekIndustries.model.maps.MoveValidator;

public class Animal implements WorldElement {
    private MapDirection direction;
    private Vector2D position;
    private final int[] genome;
    private final short baseTick;
    private final long birthTick;
    private int energy;
    private int eatenGrass = 0;
    private long age = 0;
    private final ArrayList<Animal> childrenList = new ArrayList<>();
    private final ArrayList<Animal> parentsList = new ArrayList<>();


    public Animal(long tick, int energy, int[] genome, Vector2D position) {
        this.baseTick = (short) (Math.random() * genome.length);
        this.birthTick = tick;
        this.energy = energy;
        this.genome = genome;
        this.position = position;

        direction = MapDirection.values()[genome[baseTick]];
    }

    public void addChild(Animal child){
        childrenList.add(child);
    }

    public void addParent(Animal parent){
        parentsList.add(parent);
    }

    public void removeChild(Animal child){
        childrenList.remove(child);
    }

    public void removeParent(Animal parent){
        parentsList.remove(parent);
    }

    public void die() {
        for (Animal parent : parentsList) {
            parent.removeChild(this);
            for (Animal child : childrenList) {
                parent.addChild(child);
                child.addParent(parent);
            }
        }
        for (Animal child : childrenList) {
            child.removeParent(this);
        }
    }

    public int getDescendants(){
        HashSet<Animal> children = new HashSet<>();
        return getDescendantsRec(children, 0);
    }

    private int getDescendantsRec(HashSet<Animal> children, int count){
        for(Animal child : childrenList){
            if(!children.contains(child)){
                children.add(child);
                count = child.getDescendantsRec(children, count + 1);
            }
        }
        return count;

    }

    public int getChildrenNum(){
        return childrenList.size();
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

