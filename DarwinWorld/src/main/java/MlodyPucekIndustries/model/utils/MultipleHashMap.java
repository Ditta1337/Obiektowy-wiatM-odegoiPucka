package MlodyPucekIndustries.model.utils;

import java.util.ArrayList;
import java.util.HashMap;

import MlodyPucekIndustries.model.elements.Animal;
public class MultipleHashMap {
    HashMap<Vector2D, ArrayList<Animal>> animalsMap = new HashMap<>();
    public MultipleHashMap(int height, int width){
        for(int i = 0;i < width;i += 1){
            for(int j = 0; j < height; j += 1){
                animalsMap.put(new Vector2D(i,j),new ArrayList<>());
            }
        }
    }
    public void put(Animal animal){
        (animalsMap.get(animal.getPosition())).add(animal);
    }

    public void remove(Animal animal, Vector2D position){
        (animalsMap.get(position)).remove(animal);
    }

    public ArrayList<Animal> get(Vector2D position) {
        return animalsMap.get(position);
    }

    public ArrayList<Animal> values(){
        ArrayList<Animal> elements = new ArrayList<>();
        for(ArrayList<Animal> animals: animalsMap.values()) {
            elements.addAll(animals);
        }
        return elements;
    }

    public boolean containsKey(Vector2D position){
        return !(animalsMap.get(position)).isEmpty();
    }
}
