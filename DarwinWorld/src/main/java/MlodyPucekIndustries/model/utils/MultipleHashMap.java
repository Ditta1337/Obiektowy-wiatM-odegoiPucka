package MlodyPucekIndustries.model.utils;

import java.util.ArrayList;
import java.util.HashMap;

import MlodyPucekIndustries.model.elements.Animal;
public class MultipleHashMap {
    HashMap<Vector2d, ArrayList<Animal>> animalsmap = new HashMap<>();
    public MultipleHashMap(int height, int width){
        for(int i = 0;i < width;i += 1){
            for(int j = 0; j < height; j += 1){
                animalsmap.put(new Vector2d(i,j),new ArrayList<>());
            }
        }
    }
    public void put(Animal animal){
        (animalsmap.get(animal.getPosition())).add(animal);
    }
    public void remove(Animal animal, Vector2d position){
        (animalsmap.get(position)).remove(animal);
    }
    public ArrayList<Animal> get(Vector2d position){
        return animalsmap.get(position);
    }
    public ArrayList<Animal> values(){
        ArrayList<Animal> elements = new ArrayList<>();
        for(ArrayList<Animal> animals: animalsmap.values()){
            elements.addAll(animals);
        }
        return elements;
    }

    public boolean containsKey(Vector2d position){
        return !(animalsmap.get(position)).isEmpty();
    }
}
