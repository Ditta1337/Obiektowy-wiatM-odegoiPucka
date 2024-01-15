package MlodyPucekIndustries.model.utils;

import MlodyPucekIndustries.model.elements.Animal;

import java.util.ArrayList;

public class AnimalNode {
    private byte hasAncestors;
    private ArrayList<AnimalNode> children = new ArrayList<>();
    private final Animal animal;

    public AnimalNode(Animal animal, byte hasAncestors) {
        this.hasAncestors = hasAncestors;
        this.animal = animal;
    }

    public int getEnergy() {
        return animal.getEnergy();
    }

    public ArrayList<AnimalNode> getChildren(){
        return children;
    }

    public boolean hasAncestors(){
        return hasAncestors > 0;
    }

    public void addChild(AnimalNode child){
        children.add(child);
    }

    public Animal getAnimal(){
        return animal;
    }

    public void parentDeleted(){
        hasAncestors--;
    }

    @Override
    public String toString() {
        return "AnimalNode{" +
                "hasAncestors=" + hasAncestors +
                ", children=" + children +
                ", animal=" + animal +
                '}';
    }

}
